package com.miyin.zhenbaoqi.ui.shop.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.ui.live.IMLiveUtils
import com.miyin.zhenbaoqi.ui.live.IMMessageMgr
import com.miyin.zhenbaoqi.ui.live.adapter.ChatAdapter
import com.miyin.zhenbaoqi.ui.live.callback.StandardCallback
import com.miyin.zhenbaoqi.ui.live.callback.StandardMemberCallback
import com.miyin.zhenbaoqi.ui.live.dialog.*
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.shop.contract.PushLiveContract
import com.miyin.zhenbaoqi.ui.shop.presenter.PushLivePresenter
import com.miyin.zhenbaoqi.utils.*
import com.orhanobut.logger.Logger
import com.tencent.imsdk.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit
import com.tencent.rtmp.ITXLivePushListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLivePushConfig
import com.tencent.rtmp.TXLivePusher
import kotlinx.android.synthetic.main.activity_push_live.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*

@Suppress("unused")
@SuppressLint("SetTextI18n")
class PushLiveActivity : BaseMvpActivity<PushLiveContract.IView, PushLiveContract.IPresenter>(), PushLiveContract.IView, OnDialogCallback, TIMMessageListener {

    private var mPushUrl: String? = null
    private var mRoomId: Int = 0
    private var mPeopleCount: Int = 0
    private var mAvatar: String? = null
    private var mLiveName: String? = null
    private var mFrontCamera = true
    private val mChatList = mutableListOf<LiveChatBean>()
    private var mLiveChatDialog: LiveChatDialog? = null
    private lateinit var mChatAdapter: ChatAdapter
    private val mLivePusher: TXLivePusher by lazy {
        TXLivePusher(this).apply {
            config = TXLivePushConfig().apply {
                setTouchFocus(false)
            }
            setPushListener(object : ITXLivePushListener {
                override fun onNetStatus(p0: Bundle?) {

                }

                override fun onPushEvent(event: Int, p1: Bundle?) {
                    if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT
                            || event == TXLiveConstants.PUSH_ERR_INVALID_ADDRESS
                            || event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL
                            || event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
                        closeRoom()
                    } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
                        // 您当前的网络环境不佳，请尽快更换网络保证正常直播
                        showToast("当前的网络环境不佳")
                    } else if (event == TXLiveConstants.PUSH_EVT_OPEN_CAMERA_SUCC) {
                        if (mFrontCamera) {
                            switchCamera()
                            mFrontCamera = false
                        }
                    }
                }
            })
        }
    }
    private val context: Context = this
    private var mWXAPI: IWXAPI? = null
    private val mHandler = Handler()
    private var mCountDownTimer: CountDownTimer? = null
    private var mPhoneListener: PhoneStateListener? = null

    override fun useEventBus() = true

    override fun getContentView(): Int {
        ImmersionBar.hideStatusBar(window)
        return R.layout.activity_push_live
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, true)
        TIMManager.getInstance().addMessageListener(this)

        // 初始化电话监听
        initListener()

        visible(cl_live_before)
        gone(cl_live)
        iv_cover.loadImg(mAvatar)
        mLiveName = et_live_before_name.text.toString()
        tv_shop_name.text = mLiveName
        tv_live_count.text = "在线$mPeopleCount"

        requestPermission(getString(R.string.permission_camera_audio), object : OnPermissionCallback {
            override fun onGranted() {
                // 设置本地预览View
                mLivePusher.startCameraPreview(video_view)
            }

            override fun onDenied() {
                showToast(getString(R.string.permission_camera_audio))
                finish()
            }
        }, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

        rv_chat_live.run {
            mChatAdapter = ChatAdapter(mChatList)
            adapter = mChatAdapter
            layoutManager = LinearLayoutManager(context)
            scrollToPosition(mChatList.size - 1)
        }
        setOnClickListener(ll_share_list, iv_close, fl_commodity, tv_chat_live, ll_hot, iv_share, iv_more,
                btn_live_before_start, iv_live_before_avater, tv_live_before_flip, live_before_close)
    }

    override fun initData() {
        mPresenter?.liveRoomCreate()
        mPresenter?.obtainSharedList(1, 10)
        mPresenter?.obtainPeopleLevel()
    }

    override fun createPresenter() = PushLivePresenter()

    override fun onResume() {
        super.onResume()
        if (video_view != null) {
            video_view.onResume()
            mLivePusher.resumePusher()
            mLivePusher.resumeBGM()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mLivePusher.stopPusher()
        mLivePusher.stopCameraPreview(true)
        TIMManager.getInstance().removeMessageListener(this)
        EventBus.getDefault().unregister(this)
        unInitPhoneListener()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_share_list -> {
                val dialog = ShareListDialog.newInstance()
                dialog.show(supportFragmentManager, "shareList")
            }
            R.id.iv_close -> {
                closeRoom()
            }
            R.id.fl_commodity -> {
                val dialog = GoodsLiveDialog.newInstance(mRoomId)
                dialog.show(supportFragmentManager, "goods")
            }
            R.id.tv_chat_live -> {
                mLiveChatDialog = LiveChatDialog.newInstance()
                mLiveChatDialog?.show(supportFragmentManager, "liveChat")
            }
            R.id.ll_hot -> {
                val dialog = LiveHotDialog.newInstance()
                dialog.show(supportFragmentManager, "hotList")
            }
            R.id.iv_share -> {
                val bean = LiveShareDataBean(mLiveName, mAvatar, mAvatar, "0", mPeopleCount)
                val dialog = LiveShareDialog.newInstance(bean)
                dialog.show(supportFragmentManager, "goodsShare")
                dialog.setOnDialogCallback(this)
            }
            R.id.iv_more -> {
                mLivePusher.switchCamera()
            }
            R.id.tv_live_before_flip -> {
                mLivePusher.switchCamera()
            }
            R.id.btn_live_before_start -> {
                if (mAvatar == null) {
                    showToast("请上传封面图片")
                    return
                }
                if (et_live_before_name.text.toString().isEmpty()) {
                    showToast("请填写您的直播标题")
                    return
                }
                mPresenter?.liveRoomOpen(mRoomId, mAvatar, et_live_before_name.text.toString())
            }
            R.id.iv_live_before_avater -> {
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        SelectPhotoDialog(this@PushLiveActivity).builder().setSelectNum(1).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.live_before_close -> {
                finish()
            }
        }
    }

    private fun initIM() {
        requestPermission(getString(R.string.permission_camera_audio), object : OnPermissionCallback {
            override fun onGranted() {
                val ret = mLivePusher.startPusher(mPushUrl?.trim { it <= ' ' })
                if (ret == -5) {
                    showToast("startRTMPPush: license 校验失败")
                } else {
                    createIMGroup()
                }
            }

            override fun onDenied() {
                showToast(getString(R.string.permission_camera_audio))
                finish()
            }
        }, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

    }

    private fun createIMGroup() {
        val groupName = mLiveName ?: "直播"
        IMLiveUtils.getIMLiveUtils(context).createIMGroup(context, mRoomId.toString(), groupName, object : StandardCallback {
            override fun onError(errCode: Int, errInfo: String?) {
                Log.e("创建群聊===》", errInfo + "")
                AlertDialog.Builder(this@PushLiveActivity)
                        .setTitle("提示")
                        .setMessage("聊天室创建失败，是否退出直播间？")
                        .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                            mPresenter?.closeLiveRoom(mRoomId)
                            dialog.dismiss()
                        }
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show()
            }

            override fun onSuccess() {
                obtainGroupMembers()
            }
        })
    }

    private fun obtainGroupMembers() {
        IMLiveUtils.getIMLiveUtils(context).obtainGroupMembers(mRoomId.toString(), object : StandardMemberCallback {
            override fun onError(errCode: Int, errInfo: String?) {
            }

            override fun onSuccess(size: Any?) {
                Logger.d("size == $size")
                mPeopleCount = if (size != null && size != 0) {
                    size.toString().toInt() + 900
                } else {
                    901
                }
                tv_live_count.text = "在线$mPeopleCount"
            }
        })
    }

    override fun onBackPressed() {
        if (cl_live_before.visibility == View.VISIBLE) {
            finish()
            return
        }
        closeRoom()
    }

    private fun closeRoom() {
        AlertDialog.Builder(this)
                .setTitle("退出")
                .setMessage("您确定要退出直播间吗？")
                .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                    val bean = IMMessageMgr.CommonJson<EnterRoomTipBean>()
                    bean.cmd = "CustomQuitRoom"
                    bean.data = EnterRoomTipBean(SPUtils.getString("nick_name"), "", SPUtils.getString("level_img"), "")
                    val content: String? = Gson().toJson(bean, object : TypeToken<IMMessageMgr.CommonJson<EnterRoomTipBean>>() {}.type)
                    IMLiveUtils.getIMLiveUtils(context).sendGroupCustomMessage(content, object : StandardCallback {
                        override fun onError(errCode: Int, errInfo: String?) {
                            Log.e("IM退出$errCode", errInfo + "")
                        }

                        override fun onSuccess() {
                            IMLiveUtils.getIMLiveUtils(context).destroyGroup(mRoomId.toString(), object : StandardCallback {
                                override fun onError(errCode: Int, errInfo: String?) {
                                    ConversationManagerKit.getInstance().deleteConversation(mRoomId.toString(), true)
                                    mRoomId.let { mPresenter?.closeLiveRoom(it) }
                                    dialog.dismiss()
                                }

                                override fun onSuccess() {
                                    ConversationManagerKit.getInstance().deleteConversation(mRoomId.toString(), true)
                                    mRoomId.let { mPresenter?.closeLiveRoom(it) }
                                    dialog.dismiss()
                                }
                            })

                            finish()
                        }
                    })

                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()

    }

    private fun getContent(message: TIMMessage): String? {
        val element = message.getElement(0)
        return when (element.type) {
            TIMElemType.Custom -> {
                val customElement = element as TIMCustomElem
                val data = customElement.data
                String(data, Charset.forName("UTF-8"))
            }
            else -> {
                null
            }
        }
    }

    override fun onNewMessages(list: MutableList<TIMMessage>): Boolean {
        val data = list[0]
        val json = getContent(data)
        Logger.d("json == $json")
        if (!json.isNullOrEmpty()) {
            val obj = JSONObject(json)
            if (!obj.has("data")) {
                return false
            }
            val innerJSON = obj.getJSONObject("data")
            when (obj.getString("cmd")) {
                "customEnterRoom" -> {
                    val bean = LiveChatBean(innerJSON.getString("nickName"), innerJSON.getString("content"), innerJSON.getString("levelImg"), "room")
                    mChatList.add(bean)
                    rv_chat_live.scrollToPosition(mChatList.size - 1)
                }
                "CustomText" -> {
                    val bean = LiveChatBean(innerJSON.getString("nickName"), innerJSON.getString("content"), innerJSON.getString("levelImg"))
                    mChatList.add(bean)
                    rv_chat_live.scrollToPosition(mChatList.size - 1)
                }
                "customAuctionBid" -> {
                    visible(cl_auction)

                    val animation = ScaleAnimation(0f, 1.0f, 0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                    animation.duration = 500  // 动画时间
                    animation.repeatCount = 0  // 动画的反复次数
                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(animation: Animation?) {

                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            mHandler.postDelayed({
                                gone(cl_auction)
                            }, 2500)
                        }

                        override fun onAnimationStart(animation: Animation?) {
                            mHandler.removeCallbacksAndMessages(null)
                        }
                    })
                    cl_auction.startAnimation(animation) // 開始动画

                    live_auction_people.text = innerJSON.getString("auction_name")
                    live_auction_price.text = SpanUtils()
                            .append("¥").setFontSize(30, true)
                            .append(innerJSON.getString("auction_price"))
                            .create()
                    tv_goods_price.text = "¥${innerJSON.getString("auction_price")}"
                }
                "CustomReleaseGoods" -> {
                    visible(ll_count_down_container, cl_add_price_container)
                    val bean = JSONUtils.fromJSON<GoodsDetailBean.DataBean.GoodsBean>(innerJSON.toString())
                    showAuctionGoods(bean)
                }
                "CustomAuctionFailure" -> {
                    gone(cl_add_price_container, ll_count_down_container)
                    showToast("该商品无人加价")
                }
                "CustomPeopleCount" -> {
                    if (innerJSON.has("people_count")) {
                        val peopleCount = innerJSON.get("people_count").toString()
                        Logger.d("people_count == $peopleCount")
                        tv_live_count.text = "在线$peopleCount"
                    }
                }
                else -> {
                }
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.run {
                PictureSelector.obtainMultipleResult(this).forEach {
                    if (it.isCompressed) {
                        val path = it.compressPath
                        mPresenter?.uploadMerchantHead(path)
                    }
                }
            }
        }
    }

    private fun showAuctionGoods(bean: GoodsDetailBean.DataBean.GoodsBean) {
        bean.run {
            val img = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img!!
            }
            val transform = RoundCornersTransform(getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.LEFT)
            iv_goods_img.transform(img, transform)
            tv_goods_name.text = goods_name
            tv_goods_price.text = "¥${FormatUtils.formatNumber(goods_amount / 100f)}"

            mCountDownTimer = object : CountDownTimer(end_time_timestamp - service_time, 1000) {
                override fun onFinish() {
                    gone(ll_count_down_container, cl_add_price_container)
                    if (is_live == "1") {
                        mHandler.postDelayed({
                            val goodsImg = when {
                                goods_img.isNullOrEmpty() -> ""
                                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                                else -> goods_img!!
                            }
                            val goodsName = goods_name ?: ""
                            mPresenter?.getAuctionList(goods_id, goodsImg, goodsName, goods_freight)
                        }, 60 * 1000)
                    }
                }

                override fun onTick(millisUntilFinished: Long) {
                    val millis2String = TimeUtils.millis2String(millisUntilFinished - TimeZone.getDefault().rawOffset, "HH:mm:ss")
                    val timeArray = millis2String.split(":")
                    tv_count_down.text = millis2String

                    timeArray.forEachIndexed { index, s ->
                        if (index == 1) {
                            tv_minute.text = s
                        } else if (index == 2) {
                            tv_seconds.text = s
                        }
                    }
                }
            }
            mCountDownTimer?.start()
        }
    }

    override fun uploadLiveRoomCoverSuccess() {

    }

    override fun closeLiveRoomSuccess() {
        EventBus.getDefault().post("refreshTXIMList")
        finish()
    }

    override fun liveRoomCreateSuccess(bean: LiveRoomCreateBean) {
        visible(cl_live_before)
        gone(cl_live)
        bean.run {
            mAvatar = face_url
            mLiveName = room_name
            mRoomId = room_id

            iv_live_before_avater.loadImg(mAvatar)
            et_live_before_name.setText(mLiveName)
        }

    }

    override fun liveRoomOpenSuccess(bean: LiveRoomOpenBean) {
        gone(cl_live_before)
        visible(cl_live)

        bean.run {
            mLiveName = room_name
            mPushUrl = push_url
            mRoomId = room_id

            iv_cover.loadImg(mAvatar)
            tv_shop_name.text = mLiveName
            initIM()
        }
    }

    override fun obtainShareListSuccess(data: LiveShareBean) {
        data.list?.forEachIndexed { index, it ->
            when (index) {
                0 -> iv_left.loadImg(it.icon_url)
                1 -> iv_middle.loadImg(it.icon_url)
                2 -> iv_right.loadImg(it.icon_url)
            }
        }
    }

    override fun uploadMerchantHeadSuccess(path: String) {
        mAvatar = path
        iv_live_before_avater.loadImg(path)
    }

    override fun obtainPeopleLevelSuccess(data: UserGradeBean) {
        data.data?.icon?.let { SPUtils.putString("level_img", it) }
    }

    override fun shareLiveRoomSuccess(isFriendCircle: Boolean) {
        val nickName = SPUtils.getString("nick_name")
        val shareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=${SPUtils.getString("head_img")}"
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo)
        WXOptionUtils.share(mWXAPI!!, shareUrl, "【唯爱多】向你推荐", "唯爱严选，匠心好物。", bitmap, isFriendCircle)

    }

    override fun getAuctionListSuccess(bean: AuctionGoodsRecordBean, goodsId: Int, goodsImg: String, goodsName: String, goodsFreight: Int) {
        context.let {
            val list = bean.list
            Logger.d("size == ${list?.size}")
            if (list == null || list.isEmpty()) {
                // 没有人加价
                val json = "{\"cmd\":\"CustomAuctionFailure\", \"data\":${JSONUtils.gson.toJson(ArrayMap<String, String>())}}}"
                IMLiveUtils.getIMLiveUtils(it).sendRoomCustomMsg(it, json, object : StandardCallback {
                    override fun onError(errCode: Int, errInfo: String?) {
                        AlertDialog.Builder(it, R.style.RtmpRoomDialogTheme).setMessage(errInfo)
                                .setTitle("发送消息失败")
                                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                                .show()
                    }

                    override fun onSuccess() {
                        gone(cl_add_price_container, ll_count_down_container)
                        showToast("该产品无人加价")
                    }
                })
            } else {
                // 有人加价
                val arrayMap = ArrayMap<String, Any>().apply {
                    put("goods_id", goodsId)
                    put("goods_img", goodsImg)
                    put("goods_name", goodsName)
                    put("goods_freight", goodsFreight)
                    put("add_price_data", JSONUtils.gson.toJson(list[0]))
                }
                val json = "{\"cmd\":\"CustomAuctionSuccess\", \"data\":${JSONUtils.gson.toJson(arrayMap)}}"
                IMLiveUtils.getIMLiveUtils(it).sendRoomCustomMsg(it, json, object : StandardCallback {
                    override fun onError(errCode: Int, errInfo: String?) {
                        AlertDialog.Builder(it, R.style.RtmpRoomDialogTheme).setMessage(errInfo)
                                .setTitle("发送消息失败")
                                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                                .show()
                    }

                    override fun onSuccess() {
                        list[0].run {
                            if (state == 2) {
                                visible(cl_auction)
                                live_auction_people.text = "恭喜 $nick_name  中拍"
                                live_auction_price.text = SpanUtils()
                                        .append("¥").setFontSize(30, true)
                                        .append(FormatUtils.formatNumber(auction_bid_amount / 100f))
                                        .create()

                                mHandler.postDelayed({
                                    gone(cl_auction, cl_add_price_container, ll_count_down_container)
                                }, 3000)
                            }
                        }
                    }
                })
            }
        }
    }

    /**
     * 初始化电话监听
     */
    private fun initListener() {
        mPhoneListener = TXPhoneStateListener(mLivePusher)
        val telephonyManager = applicationContext.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    /**
     * 销毁
     */
    private fun unInitPhoneListener() {
        val telephonyManager = applicationContext.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE)
    }

    /**
     * 电话监听
     */
    private inner class TXPhoneStateListener(pusher: TXLivePusher) : PhoneStateListener() {
        val mPusher = WeakReference(pusher)

        override fun onCallStateChanged(state: Int, phoneNumber: String) {
            super.onCallStateChanged(state, phoneNumber)
            val pusher = mPusher.get()
            when (state) {
                // 电话等待接听
                TelephonyManager.CALL_STATE_RINGING -> {
                    pusher?.run {
                        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_live_bg)
                        config.setPauseImg(bitmap)
                        config.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO or TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO)
                        pausePusher()
                    }
                }
                // 电话接听
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    pusher?.run {
                        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_live_bg)
                        config.setPauseImg(bitmap)
                        config.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO or TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO)
                        pausePusher()
                    }
                }
                // 电话挂机
                TelephonyManager.CALL_STATE_IDLE -> {
                    pusher?.run {
                        resumePusher()
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun dialogResults(data: DiaLogResultDataBean) {
        if (data.content.isNullOrEmpty()) {
            return
        }
        val content = data.content
        context.let {
            val bean = LiveChatBean(SPUtils.getString("nick_name"), content, SPUtils.getString("level_img"))
            val beanData = JSONUtils.gson.toJson(bean)
            val json = "{\"cmd\":\"CustomText\", \"data\":$beanData}"
            IMLiveUtils.getIMLiveUtils(it).sendRoomCustomMsg(context, json, object : StandardCallback {
                override fun onError(errCode: Int, errInfo: String?) {
                    AlertDialog.Builder(it, R.style.RtmpRoomDialogTheme).setMessage(errInfo)
                            .setTitle("发送消息失败")
                            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                            .show()
                }

                override fun onSuccess() {
                    mChatList.add(bean)
                    rv_chat_live.scrollToPosition(mChatList.size - 1)
                }
            })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReleaseAuctionGoodsEvent(map: ArrayMap<String, String>) {
        val type = map["type"].toString()
        if (type == "auction") {
            context.let {
                val innerJSON = map["data"].toString()
                val json = "{\"cmd\":\"CustomReleaseGoods\", \"data\":$innerJSON}"
                Logger.d("onReleaseAuctionGoodsEvent json == $json")
                IMLiveUtils.getIMLiveUtils(it).sendRoomCustomMsg(it, json, object : StandardCallback {
                    override fun onError(errCode: Int, errInfo: String?) {
                        AlertDialog.Builder(it, R.style.RtmpRoomDialogTheme).setMessage(errInfo)
                                .setTitle("发送消息失败")
                                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                                .show()
                    }

                    override fun onSuccess() {
                        visible(ll_count_down_container, cl_add_price_container)
                        val bean = JSONUtils.fromJSON<GoodsDetailBean.DataBean.GoodsBean>(innerJSON)
                        showAuctionGoods(bean)
                    }
                })
            }
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            if (obj == "shareFriend") {
                mPresenter?.shareLiveRoom(mRoomId, false)
            } else if (obj == "shareFriendCircle") {
                mPresenter?.shareLiveRoom(mRoomId, true)
            }
        }
    }

}
