package com.miyin.zhenbaoqi.ui.live.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.ArrayMap
import android.view.*
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseMvpFragment
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.*
import com.miyin.zhenbaoqi.ui.live.IMLiveUtils
import com.miyin.zhenbaoqi.ui.live.IMMessageMgr
import com.miyin.zhenbaoqi.ui.live.adapter.ChatAdapter
import com.miyin.zhenbaoqi.ui.live.callback.StandardCallback
import com.miyin.zhenbaoqi.ui.live.callback.StandardMemberCallback
import com.miyin.zhenbaoqi.ui.live.contract.LiveInteractiveContract
import com.miyin.zhenbaoqi.ui.live.dialog.*
import com.miyin.zhenbaoqi.ui.live.presenter.LiveInteractivePresenter
import com.miyin.zhenbaoqi.ui.message.activity.OnlineCustomerActivity
import com.miyin.zhenbaoqi.ui.message.activity.ReportActivity
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsPayActivity
import com.miyin.zhenbaoqi.utils.*
import com.miyin.zhenbaoqi.widget.PopWindow
import com.orhanobut.logger.Logger
import com.tencent.imsdk.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit
import kotlinx.android.synthetic.main.fragment_live_interactive.*
import kotlinx.android.synthetic.main.popup_live_more.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*

@SuppressLint("SetTextI18n")
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class LiveInteractiveFragment : BaseMvpFragment<LiveInteractiveContract.IView, LiveInteractiveContract.IPresenter>(), LiveInteractiveContract.IView, TIMMessageListener, OnDialogCallback {

    private var mRoomId = 0
    private val mChatList = mutableListOf<LiveChatBean>()
    private lateinit var mChatAdapter: ChatAdapter
    private val mHandler = Handler()
    private var mLiveChatDialog: LiveChatDialog? = null
    private lateinit var mWXAPI: IWXAPI
    private var coverUrl: String? = ""
    private var mIconUrl: String? = ""
    private var mLiveName: String? = ""
    private var name: String? = ""
    private var fansCount: Int = 0
    private var peopleCount: Int = 0
    private var popularityCount: Int = 900
    private var mIsFocus: Boolean = false
    private var mBean: LiveShareDataBean? = null
    private var mCanSendMessage = true
    private var mMerchantId = 0
    private var mUserId = 0
    private var mAccountName: String? = null
    private var mPosition = 0
    private var mIsSubAccount = false
    private var mIsShowLiveGoods = true
    private var mGoodsDialog: GoodsDialog? = null
    private var mGoodsLiveDialog: GoodsLiveDialog? = null
    private var mCountDownTimer: CountDownTimer? = null

    companion object {
        fun newInstance(roomId: Int, cover_url: String?, name: String?, icon_url: String?, fans_count: Int, is_focus: Boolean,
                        merchantId: Int, userId: Int, accountName: String?, position: Int, isSubAccount: Boolean = false,
                        isShowLiveGoods: Boolean) = LiveInteractiveFragment().apply {
            arguments = Bundle().apply {
                putInt("room_id", roomId)
                putString("cover_url", cover_url)
                putString("icon_url", icon_url)
                putString("name", name)
                putInt("fans_count", fans_count)
                putBoolean("is_focus", is_focus)
                putInt("merchant_id", merchantId)
                putInt("user_id", userId)
                putString("account_name", accountName)
                putInt("position", position)
                putBoolean("is_sub_account", isSubAccount)
                putBoolean("is_show_live_goods", isShowLiveGoods)
            }
        }
    }

    override fun useEventBus() = true

    override fun getContentView(): Int {
        arguments?.run {
            mRoomId = getInt("room_id")
            coverUrl = getString("cover_url")
            mIconUrl = getString("icon_url")
            mLiveName = getString("name")
            fansCount = getInt("fans_count")
            mIsFocus = getBoolean("is_focus")
            mMerchantId = getInt("merchant_id")
            mUserId = getInt("user_id")
            mAccountName = getString("account_name")
            mPosition = getInt("position")
            mIsSubAccount = getBoolean("is_sub_account")
            mIsShowLiveGoods = getBoolean("is_show_live_goods")
        }
        return R.layout.fragment_live_interactive
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        mWXAPI = WXAPIFactory.createWXAPI(activity, BuildConfig.WX_APP_ID, true)
        TIMManager.getInstance().addMessageListener(this)

        Logger.d("account == $mAccountName, user_id == $mUserId")
        if (mIsFocus) {
            gone(tv_attention)
        } else {
            visible(tv_attention)
        }

        rv_chat.run {
            mChatAdapter = ChatAdapter(mChatList)
            adapter = mChatAdapter
            layoutManager = LinearLayoutManager(context)
            scrollToPosition(mChatList.size - 1)

            mChatAdapter.setOnItemClickListener { _, _, position ->
                val bean = mChatList[position]
                if (bean.type == null && mIsSubAccount) {
                    if (bean.user_id == SPUtils.getString("account_name")) {
                        showToast("不能和自己聊天")
                        return@setOnItemClickListener
                    }
                    Logger.d("title == ${bean.nickName}, user_id == ${bean.user_id}")
                    startActivity<OnlineCustomerActivity>("title" to bean.nickName, "user_id" to bean.user_id.toInt())
                }
            }
        }

        iv_cover.loadImg(coverUrl)
        name = mLiveName
        tv_shop_name.text = name
        tv_live_count.text = "在线$popularityCount"

        SoftKeyBoardListener.setListener(activity!!, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                // 输入文字时的界面退出动画
                val animatorSetHide = AnimatorSet()
                val topOutAnim: ObjectAnimator = ObjectAnimator.ofFloat(title_bar, "translationY", 0f, -title_bar.height.toFloat())
                val leftOutAnim: ObjectAnimator = ObjectAnimator.ofFloat(tv_tip, "translationX", 0f, -(tv_tip.width.toFloat() + context?.getDimension(R.dimen.dp_12)!!))
                val rightOutAnim = ObjectAnimator.ofFloat(ll_hot, "translationX", 0f, ll_hot.width.toFloat() + context?.getDimension(R.dimen.dp_10)!!)
                animatorSetHide.playTogether(topOutAnim, leftOutAnim, rightOutAnim)
                animatorSetHide.duration = 300
                animatorSetHide.start()

                dynamicChangeRecyclerViewH(height + context!!.getDimension(R.dimen.dp_75).toInt(), context!!.getDimension(R.dimen.dp_90).toInt())
            }

            override fun keyBoardHide(height: Int) {
                mLiveChatDialog?.dismiss()

                // 输入文字时的界面进入时的动画
                val animatorSetShow = AnimatorSet()
                val topInAnim = ObjectAnimator.ofFloat(title_bar, "translationY", -title_bar.height.toFloat(), 0f)
                val leftInAnim = ObjectAnimator.ofFloat(tv_tip, "translationX", -(tv_tip.width.toFloat() + context?.getDimension(R.dimen.dp_12)!!), 0f)
                val rightInAnim = ObjectAnimator.ofFloat(ll_hot, "translationX", ll_hot.width.toFloat() + context?.getDimension(R.dimen.dp_10)!!, 0f)
                animatorSetShow.playTogether(topInAnim, leftInAnim, rightInAnim)
                animatorSetShow.duration = 300
                animatorSetShow.start()

                dynamicChangeRecyclerViewH(0, context!!.getDimension(R.dimen.dp_150).toInt())
            }
        })

        setOnClickListener(tv_attention, iv_cover, ll_share_list, tv_chat, iv_close, fl_share, fl_commodity, ll_hot, fl_more)
    }

    override fun initData() {
        mPresenter?.obtainSharedList()
        mPresenter?.obtainPeopleLevel()
        mPresenter?.checkBlackList(SPUtils.getInt("user_id"), mMerchantId)
    }

    override fun createPresenter() = LiveInteractivePresenter()

    override fun onDestroyView() {
        if (null != mLiveChatDialog) {
            mLiveChatDialog = null
        }
        if (null != mGoodsDialog) {
            mGoodsDialog = null
        }
        if (null != mCountDownTimer) {
            mCountDownTimer!!.cancel()
            mCountDownTimer = null
        }
        mHandler.removeCallbacksAndMessages(null)
        SoftKeyBoardListener.removeListener()
        TIMManager.getInstance().removeMessageListener(this)
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_attention -> {
                mPresenter?.getMerchantList(mMerchantId, 0)
            }
            R.id.iv_cover -> {
                mPresenter?.obtainLiveInfoData(mMerchantId)
            }
            R.id.ll_share_list -> {
                val dialog = ShareListDialog.newInstance()
                dialog.show(fragmentManager!!, "shareList")
            }
            R.id.tv_chat -> {
                if (mCanSendMessage) {
                    mLiveChatDialog = LiveChatDialog.newInstance()
                    mLiveChatDialog?.show(activity!!.supportFragmentManager, "liveChat")
                } else {
                    showToast("您不能在次直播间发送消息")
                }
            }
            R.id.fl_commodity -> {
                if (mIsSubAccount && mIsShowLiveGoods) {
                    mGoodsLiveDialog = GoodsLiveDialog.newInstance(mRoomId)
                    mGoodsLiveDialog!!.show(fragmentManager!!, "goods")
                } else if (mIsShowLiveGoods) {
                    mGoodsDialog = GoodsDialog.newInstance(mRoomId)
                    mGoodsDialog!!.show(fragmentManager!!, "goods")
                }
            }
            R.id.fl_share -> {
                mBean = LiveShareDataBean(name, mIconUrl, coverUrl, "", fansCount, peopleCount)
                val dialog = LiveShareDialog.newInstance(mBean!!)
                dialog.show(childFragmentManager, "goodsShare")
                dialog.setOnDialogCallback(this)
            }
            R.id.iv_close -> {
                IMLiveUtils.getIMLiveUtils(context!!).quitIMGroup(context!!, mRoomId.toString(), object : StandardCallback {
                    override fun onError(errCode: Int, errInfo: String?) {
                        ConversationManagerKit.getInstance().deleteConversation(mRoomId.toString(), true)
                        EventBus.getDefault().post("refreshTXIMList")
                        activity?.finish()
                    }

                    override fun onSuccess() {
                        ConversationManagerKit.getInstance().deleteConversation(mRoomId.toString(), true)
                        EventBus.getDefault().post("refreshTXIMList")
                        activity?.finish()
                    }
                })
            }
            R.id.ll_hot -> {
                val dialog = LiveHotDialog.newInstance()
                dialog.show(fragmentManager!!, "hotList")
            }
            R.id.fl_more -> {
                if (!mIsSubAccount) {
                    val view = View.inflate(context, R.layout.popup_live_more, null)
                    val popupWindow = PopWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true).apply {
                        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        isOutsideTouchable = true
                        isFocusable = true

                        val dimen = context?.getDimensionPixelSize(R.dimen.dp_10)!!
                        val measuredWidth = contentView.measuredWidth
                        val measuredHeight = contentView.measuredHeight
                        val offsetX = (DensityUtils.getScreenWidth() - measuredWidth + dimen) / 2

                        Logger.d("height == ${fl_more.height}")
                        showAtLocation(fl_more, Gravity.BOTTOM, offsetX, measuredHeight + fl_more.height + dimen)
                    }

                    view.tv_refresh.setOnClickListener {
                        popupWindow.dismiss()
                    }
                    view.tv_customer.setOnClickListener {
                        startActivity<OnlineCustomerActivity>("title" to mUserId.toString(), "user_id" to mUserId, "account_name" to mAccountName)
                        popupWindow.dismiss()
                    }
                    view.tv_complaint.setOnClickListener {
                        startActivity<ReportActivity>("merchant_id" to mMerchantId)
                        popupWindow.dismiss()
                    }
                }
            }
        }
    }

    override fun getAuctionGoodsSuccess(bean: GoodsDetailBean) {
        bean.data?.goods?.run {
            visible(ll_count_down_container, cl_add_price_container)
            showAuctionGoods(this)
        }
    }

    override fun getLiveRoomPopularitySuccess(popularityCount: Int) {
        this.popularityCount = popularityCount + 900
        if (null != tv_live_count) {
            tv_live_count.text = "在线$popularityCount"
        }

        sendCustomPeopleCountMsg()
    }

    private fun sendCustomPeopleCountMsg() {
        val map = ArrayMap<String, String>().apply {
            put("people_count", popularityCount.toString())
        }
        val json = "{\"cmd\":\"CustomPeopleCount\", \"data\": ${JSONUtils.gson.toJson(map)}}"
        IMLiveUtils.getIMLiveUtils(context!!).sendGroupCustomMessage(json, object : StandardCallback {
            override fun onError(errCode: Int, errInfo: String?) {

            }

            override fun onSuccess() {

            }
        })
    }

    private fun auctionBid(price: Int) {
        val bean = IMMessageMgr.CommonJson<LiveAuctionCustomBean>()
        bean.cmd = "customAuctionBid"
        bean.data = LiveAuctionCustomBean(SPUtils.getString("nick_name"), FormatUtils.formatNumber(price / 100f))
        val content: String? = Gson().toJson(bean, object : TypeToken<IMMessageMgr.CommonJson<LiveAuctionCustomBean>>() {}.type)
        context?.let {
            IMLiveUtils.getIMLiveUtils(it).sendGroupCustomMessage(content, object : StandardCallback {
                override fun onError(errCode: Int, errInfo: String?) {
                }

                override fun onSuccess() {
                    mGoodsDialog?.dismiss()
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

                    live_auction_people.text = bean.data?.auction_name
                    live_auction_price.text = SpanUtils()
                            .append("¥").setFontSize(30, true)
                            .append(bean.data?.auction_price.toString())
                            .create()
                    tv_goods_price.text = "¥" + bean.data?.auction_price.toString()
                }

            })
        }
    }

    override fun closeLiveRoomSuccess() {
        activity?.finish()
    }

    /**
     * 动态的修改 RecyclerView 的高度
     */
    private fun dynamicChangeRecyclerViewH(marginBottom: Int, heightPX: Int) {
        val layoutParams = rv_chat.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = heightPX
        layoutParams.bottomMargin = marginBottom
        rv_chat.layoutParams = layoutParams
    }

    override fun getMerchantListSuccess() {
        gone(tv_attention)
        val map = ArrayMap<String, Any>().apply {
            put("type", "refreshLiveList")
            put("position", mPosition)
        }
        EventBus.getDefault().post(map)
    }

    override fun obtainPeopleLevelSuccess(data: UserGradeBean) {
        data.data?.icon?.let { SPUtils.putString("level_img", it) }
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
        if (!json.isNullOrEmpty()) {
            val obj = JSONObject(json)
            val innerJSON = obj.getJSONObject("data")
            Logger.d("json == $innerJSON")
            when (obj.getString("cmd")) {
                "customEnterRoom" -> {
                    val bean = LiveChatBean(innerJSON.getString("nickName"), innerJSON.getString("content"), innerJSON.getString("levelImg"), "room")
                    mChatList.add(bean)
                    rv_chat.scrollToPosition(mChatList.size - 1)
                }
                "CustomText" -> {
                    val userId = if (innerJSON.has("user_id")) innerJSON.getString("user_id") else ""
                    val bean = LiveChatBean(innerJSON.getString("nickName"), innerJSON.getString("content"), innerJSON.getString("levelImg"), user_id = userId)
                    mChatList.add(bean)
                    rv_chat.scrollToPosition(mChatList.size - 1)
                }
                "customAuctionBid" -> {
                    visible(cl_auction, cl_add_price_container, ll_count_down_container)

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
                    val addPrice = innerJSON.getString("auction_price")
                    live_auction_price.text = SpanUtils()
                            .append("¥").setFontSize(30, true)
                            .append(addPrice)
                            .create()

                    tv_goods_price.text = "¥${addPrice}"

                    tv_add_price.text = SpanUtils()
                            .appendLine("加一手")
                            .append(addPrice)
                            .create()
                }
                "CustomReleaseGoods" -> {
                    visible(ll_count_down_container, cl_add_price_container)
                    val bean = JSONUtils.fromJSON<GoodsDetailBean.DataBean.GoodsBean>(innerJSON.toString())
                    showAuctionGoods(bean)
                }
                "CustomAuctionSuccess" -> {
                    gone(cl_add_price_container)

                    val arrayMap = JSONUtils.fromJSON<ArrayMap<*, *>>(innerJSON.toString()) as ArrayMap<String, Any>
                    val bean = JSONUtils.fromJSON<AuctionGoodsRecordBean.ListBean>(arrayMap["add_price_data"].toString())
                    bean.run {
                        if (state == 2) {
                            if (SPUtils.getInt("user_id") == user_id) {
                                val goodsId = arrayMap["goods_id"].toString()
                                val goodsImg = arrayMap["goods_img"].toString()
                                val goodsName = arrayMap["goods_name"].toString()
                                val goodsPrice = FormatUtils.formatNumber(auction_bid_amount / 100f)
                                val goodsFreight = arrayMap["goods_freight"].toString().toDouble().toInt()
                                val orderNumber = bean.order_number ?: ""

                                val dialog = LiveAuctionSuccessDialog.newInstance(goodsId.toDouble().toInt(), goodsImg, goodsName, goodsPrice, goodsFreight, orderNumber)
                                dialog.show(fragmentManager!!, "auctionSuccess")
                                dialog.setOnDialogCallback(this@LiveInteractiveFragment)
                            } else {
                                visible(cl_auction)
                                live_auction_people.text = "恭喜 $nick_name  中拍"
                                live_auction_price.text = SpanUtils()
                                        .append("¥").setFontSize(30, true)
                                        .append(FormatUtils.formatNumber(auction_bid_amount / 100f))
                                        .create()

                                mHandler.postDelayed({
                                    gone(cl_auction)
                                }, 3000)
                            }
                        }
                    }
                }
                "CustomAuctionFailure" -> {
                    gone(cl_add_price_container, ll_count_down_container)
                }
                "CustomPeopleCount" -> {
                    if (innerJSON.has("people_count")) {
                        val peopleCount = innerJSON.get("people_count").toString()
                        tv_live_count.text = "在线$peopleCount"
                    }
                }
                else -> {
                    gone(cl_add_price_container, ll_count_down_container)
                }
            }
        }
        return false
    }

    private fun showAuctionGoods(bean: GoodsDetailBean.DataBean.GoodsBean) {
        bean.run {
            val goodsImg = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img
            }
            val transform = RoundCornersTransform(context?.getDimension(R.dimen.dp_5)!!, RoundCornersTransform.CornerType.LEFT)
            iv_goods_img.transform(goodsImg, transform)
            tv_goods_name.text = goods_name
            tv_goods_price.text = "¥" + FormatUtils.formatNumber(goods_amount / 100f)
            tv_add_price.text = SpanUtils()
                    .appendLine("加一手")
                    .append(FormatUtils.formatNumber(add_amount / 100f))
                    .create()
            tv_add_price.isEnabled = true
            tv_add_price.setOnClickListener {
                mPresenter?.auctionBindGoods(goods_id)
            }

            mCountDownTimer = object : CountDownTimer(end_time_timestamp - service_time, 1000) {
                override fun onFinish() {
                    gone(ll_count_down_container)
                    tv_count_down.text = "截拍中"
                    tv_add_price.text = "截拍中"
                    tv_add_price.isEnabled = false
                }

                override fun onTick(millisUntilFinished: Long) {
                    val time = TimeUtils.millis2String(millisUntilFinished - TimeZone.getDefault().rawOffset, "HH:mm:ss")
                    val timeArray = time.split(":")

                    tv_count_down.text = time
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun dialogResult(data: DiaLogResultDataBean) {
        if (data.content.isNullOrEmpty()) {
            return
        }
        val content = data.content
        context?.let {
            val userId = if (mIsSubAccount) SPUtils.getString("account_name") else SPUtils.getInt("user_id").toString()
            val bean = LiveChatBean(SPUtils.getString("nick_name"), content, SPUtils.getString("level_img"), user_id = userId)
            val beanData = JSONUtils.gson.toJson(bean)
            val json = "{\"cmd\":\"CustomText\", \"data\": $beanData}"
            IMLiveUtils.getIMLiveUtils(it).sendRoomCustomMsg(it, json, object : StandardCallback {
                override fun onError(errCode: Int, errInfo: String?) {
                    AlertDialog.Builder(it, R.style.RtmpRoomDialogTheme).setMessage(errInfo)
                            .setTitle("发送消息失败")
                            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                            .show()
                }

                override fun onSuccess() {
                    mChatList.add(bean)
                    rv_chat.scrollToPosition(mChatList.size - 1)
                }

            })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAddPriceEvent(map: ArrayMap<String, Any>) {
        val type = map["type"]
        if (type == "add_price") {
            mGoodsDialog?.dismiss()

            // val addPrice = map["add_price"].toString().toInt()
            val goodsId = map["goods_id"].toString().toInt()
            mPresenter?.auctionBindGoods(goodsId)
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

    override fun obtainLiveInfoSuccess(data: MerchantInfoBean) {
        val dialog = LiveInfoDialog.newInstance(data)
        dialog.show(fragmentManager!!, "liveInfo")
    }

    override fun checkBlackListSuccess(bean: CheckBlackListBean) {
        mCanSendMessage = bean.data?.check_status ?: true
    }

    override fun openRoomSuccess() {
        mPresenter?.getAuctionGoods(mRoomId)

        context?.let {
            IMLiveUtils.getIMLiveUtils(it).obtainGroupMembers(mRoomId.toString(), object : StandardMemberCallback {
                override fun onError(errCode: Int, errInfo: String?) {
                    mPresenter?.getLiveRoomPopularity(mRoomId)
                }

                override fun onSuccess(list: Any?) {
                    if (list != null && list.toString().toDouble().toInt() != 0) {
                        popularityCount = list.toString().toDouble().toInt() + 900
                        Logger.d("count == $popularityCount")
                        if (null != tv_live_count) {
                            tv_live_count.text = "在线$popularityCount"
                        }

                        sendCustomPeopleCountMsg()
                    } else {
                        mPresenter?.getLiveRoomPopularity(mRoomId)
                    }
                }

            })

            val bean = IMMessageMgr.CommonJson<EnterRoomTipBean>()
            bean.cmd = "customEnterRoom"
            bean.data = EnterRoomTipBean(SPUtils.getString("nick_name"), "", SPUtils.getString("level_img"), "")
            val content: String? = Gson().toJson(bean, object : TypeToken<IMMessageMgr.CommonJson<EnterRoomTipBean>>() {}.type)

            IMLiveUtils.getIMLiveUtils(it).sendGroupCustomMessage(content, object : StandardCallback {
                override fun onError(errCode: Int, errInfo: String?) {
                }

                override fun onSuccess() {
                    val tip = LiveChatBean("", "唯爱多直播倡导文明直播，诚信交易，将会对内容进行系统+人工24小时在线巡查。任何传播违法、违规、低俗、暴力等不良信息将会封停帐号。", "", "tip")
                    mChatList.add(tip)
                    val liveChatBean = LiveChatBean(SPUtils.getString("nick_name"), "", SPUtils.getString("level_img"), "room")
                    mChatList.add(liveChatBean)
                    if (null != rv_chat) {
                        rv_chat.scrollToPosition(mChatList.size - 1)
                    }
                }

            })
        }
    }

    override fun auctionBindGoodsSuccess(bean: BindOfferBean) {
        auctionBid(bean.data?.auction_bid_amount ?: 0)
    }

    override fun shareLiveRoomSuccess(isFriendCircle: Boolean) {
        val nickName = SPUtils.getString("nick_name")
        val shareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=${SPUtils.getString("head_img")}"
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo)
        WXOptionUtils.share(mWXAPI, shareUrl, "【唯爱多】向你推荐", "唯爱严选，匠心好物。", bitmap, isFriendCircle)
    }

    override fun getAuctionListSuccess(bean: AuctionGoodsRecordBean, goodsId: Int, goodsImg: String, goodsName: String, goodsFreight: Int) {
        context?.let {
            val list = bean.list
            Logger.d("size == ${list?.size}")
            if (list == null || list.isEmpty()) {
                // 没有人加价
                val json = "{\"cmd\":\"CustomAuctionFailure\", \"data\":${JSONUtils.gson.toJson(ArrayMap<String, String>())}}}"
                IMLiveUtils.getIMLiveUtils(it).sendRoomCustomMsg(it, json, object : StandardCallback {
                    override fun onError(errCode: Int, errInfo: String?) {
                        gone(cl_add_price_container, ll_count_down_container)
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
                        gone(cl_add_price_container, ll_count_down_container)
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

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            if (obj == "shareFriend") {
                mPresenter?.shareLiveRoom(mRoomId, false)
            } else if (obj == "shareFriendCircle") {
                mPresenter?.shareLiveRoom(mRoomId, true)
            }
        } else if (obj is ArrayMap<*, *>) {
            val map = obj as ArrayMap<String, Any>
            val type = map["type"].toString()
            if (type == "success") {
                val goodsId = map["goods_id"].toString().toInt()
                val goodsImg = map["goods_img"].toString()
                val goodsName = map["goods_name"].toString()
                val goodsPrice = (map["goods_price"].toString().toDouble() * 100).toLong()
                val orderNumber = map["order_number"].toString()
                val goodsFreight = map["goods_freight"].toString().toInt()
                startActivity<GoodsPayActivity>("price" to goodsPrice, "goods_img" to goodsImg, "goods_freight" to goodsFreight,
                        "goods_name" to goodsName, "goods_id" to goodsId, "from" to "auction", "order_number" to orderNumber,
                        "shop_name" to "", "is_seven" to false) // TODO 待更改 shop_name is_seven
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun obtainAddMsg(data: EventBusBean) {
        openRoomSuccess()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msg: String) {
        if (msg == "hideLiveGoodsDialog") {
            mGoodsDialog?.dismiss()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReleaseAuctionGoodsEvent(map: ArrayMap<String, String>) {
        val type = map["type"].toString()
        if (type == "auction") {
            context?.let {
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
                        val bean = JSONUtils.fromJSON<GoodsDetailBean.DataBean.GoodsBean>(innerJSON)
                        bean.run {
                            visible(ll_count_down_container, cl_add_price_container)
                            invisible(tv_add_price)

                            val img = when {
                                goods_img.isNullOrEmpty() -> ""
                                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                                else -> goods_img!!
                            }
                            val transform = RoundCornersTransform(it.getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.LEFT)
                            iv_goods_img.transform(img, transform)
                            tv_goods_name.text = goods_name
                            tv_goods_price.text = "¥${FormatUtils.formatNumber(goods_amount / 100f)}"

                            mCountDownTimer = object : CountDownTimer(end_time_timestamp - service_time, 1000) {
                                override fun onFinish() {
                                    gone(ll_count_down_container, cl_add_price_container)
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
                })
            }
        }
    }

}
