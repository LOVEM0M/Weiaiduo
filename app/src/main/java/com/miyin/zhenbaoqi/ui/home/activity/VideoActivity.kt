@file:Suppress("DEPRECATION")

package com.miyin.zhenbaoqi.ui.home.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.ArrayMap
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.placeholder
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.home.contract.VideoContract
import com.miyin.zhenbaoqi.ui.home.dialog.CommentDialog
import com.miyin.zhenbaoqi.ui.home.dialog.InputDialog
import com.miyin.zhenbaoqi.ui.home.dialog.VideoShareDialog
import com.miyin.zhenbaoqi.ui.home.presenter.VideoPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.MerchantMessageActivity
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.utils.WXOptionUtils
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.rtmp.ITXVodPlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXPlayerAuthBuilder
import com.tencent.rtmp.TXVodPlayer
import kotlinx.android.synthetic.main.activity_video.*
import java.net.URLEncoder

@SuppressLint("SetTextI18n")
class VideoActivity :BaseMvpActivity<VideoContract.IView, VideoContract.IPresenter>(), VideoContract.IView, OnDialogCallback {

    private val mList = mutableListOf<MultiItemEntity>()
    private var mCommentDialog: CommentDialog? = null
    private var mBean:HomeVideoBean.DataBean? = null
    private var mVideoDetailBean:VideoDetailBean? = null
    private var mVodPlayer: TXVodPlayer? = null
    private var mLikeState = 1
    private var mLikeCount = 0
    private var mMerchantId = 0
    private var mMerchantState = 0
    private var mCanSendMsg = false
    private lateinit var mWXAPI: IWXAPI

    override fun getContentView(): Int {
        mBean = intent.getSerializableExtra("bean") as HomeVideoBean.DataBean
        return R.layout.activity_video
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).init()
        mWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, true)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mVodPlayer = TXVodPlayer(this).apply {
            setPlayerView(video_view)
            val authBuilder = TXPlayerAuthBuilder().apply {
                appId = if (BuildConfig.DEBUG) 1500000120 else 1300597750
                fileId = mBean?.file_id
            }
            enableHardwareDecode(true)
            startPlay(authBuilder)
            setAutoPlay(true)
            setVodListener(object : ITXVodPlayListener {
                override fun onPlayEvent(player: TXVodPlayer?, event: Int, params: Bundle?) {
                    if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                        gone(iv_cover)
                    } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                        player?.run {
                            seek(0)
                            resume()
                        }
                    }
                }

                override fun onNetStatus(p0: TXVodPlayer?, p1: Bundle?) {

                }
            })
        }

        setOnClickListener(iv_back, tv_attention, tv_attention_count, tv_comment, tv_share, tv_reply, tv_shop)
    }

    override fun initData() {
        mPresenter?.getVideoDetail(mBean?.id!!, mBean?.merchants_id!!)
    }

    override fun createPresenter() = VideoPresenter()

    override fun onResume() {
        super.onResume()
        mVodPlayer?.resume()
    }

    override fun onPause() {
        super.onPause()
        mVodPlayer?.pause()
    }

    override fun onDestroy() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (null != mCommentDialog) {
            mCommentDialog = null
        }
        mVodPlayer?.stopPlay(true)
        video_view.onDestroy()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()
            R.id.tv_attention -> mPresenter?.updateMerchantState(mBean?.merchants_id!!, mMerchantState)
            R.id.tv_attention_count -> {
                if (mLikeState == 1) {
                    mPresenter?.addLikes(mBean?.id!!)
                } else {
                    mPresenter?.deleteLike(mBean?.id!!)
                }
            }
            R.id.tv_reply, R.id.tv_comment -> {
                mList.clear()
                mBean?.run {
                    showDialog("正在获取...", false)
                    mPresenter?.getReplyList(id, 1, 20)
                }
            }
            R.id.tv_share -> {
                val bean = MerchantInfoBean.DataBean().apply {
                    head_img = mVideoDetailBean?.data?.head_img
                    merchants_back = mBean?.cover_url
                    merchants_name = mVideoDetailBean?.data?.merchants_name
                    merchants_subtitle = mVideoDetailBean?.data?.video_describe
                }
//                val dialog = ShopShareDialog.newInstance(bean, mBean?.merchants_id!!)
                val headImg = mVideoDetailBean?.data?.head_img ?: ""
                val merchantName = mVideoDetailBean?.data?.merchants_name ?: ""
                val desc = "${mVideoDetailBean?.data?.fans_no}粉丝"
                val cover = mBean?.cover_url ?: ""
                val videoDesc = mVideoDetailBean?.data?.video_describe ?: ""
                val dialog = VideoShareDialog.newInstance(mBean?.merchants_id!!, headImg, merchantName, desc, cover, videoDesc)
                dialog.show(supportFragmentManager, "videoShare")
            }
            R.id.tv_shop -> {
                startActivity<MerchantMessageActivity>("merchant_id" to mMerchantId)
            }
        }
    }

    override fun getVideoDetailSuccess(bean:VideoDetailBean) {
        mVideoDetailBean = bean
        bean.data?.run {
            mMerchantId = merchants_id

            val merchantName = if (merchants_name.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else merchants_name
            iv_avatar.placeholder(head_img, R.drawable.ic_merchant_header_default)
            tv_title.text = merchantName
            tv_fans_count.text = "${fans_no}粉丝"
            mMerchantState = focus_state

            tv_desc.text = SpanUtils()
                    .appendLine("@$merchantName")
                    .append(video_describe ?: "")
                    .create()

            mLikeCount = like_amount
            mLikeState = likes_state
            tv_attention_count.isSelected = (likes_state == 0)

            tv_attention_count.text = like_amount.toString()
            updateMerchantStateSuccess(focus_state)
        }
    }

    override fun checkBlackListSuccess(bean: CheckBlackListBean) {
        bean.data?.run {
            mCanSendMsg = check_status
        }
    }

    override fun getReplyListSuccess(bean: VideoReplyBean) {
        if (null != bean.data) {
            bean.data!!.forEach {
                val firstBean = VideoCommentFirstBean().apply {
                    comment = it.reply_content
                    headImg = it.head_img
                    replyTime = it.reply_time
                    nickName = it.nick_name
                    id = it.id
                }
                mList.add(firstBean)

                it.replys?.forEach {
                    val secondBean = VideoCommentSecondBean().apply {
                        comment = it.reply_content
                        headImg = it.head_img
                        replyTime = it.reply_time
                        nickName = it.nick_name
                        parentNickName = firstBean.nickName
                        id = it.id
                    }
                    mList.add(secondBean)
                }
            }
        }
        mCommentDialog = CommentDialog.newInstance(mList)
        mCommentDialog?.show(supportFragmentManager, "comment")
        /*mCommentDialog?.setOnBehaviorChangedListener(object : CommentDialog.OnBehaviorChangedListener {
            override fun changedState(bottomSheet: View?, state: Int) {
                val width = DensityUtils.getScreenWidth()
                val height = DensityUtils.getScreenHeight()
                if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    val x = width / 2f
                    val scale = height / 3f
                    video_view.scaleX = scale / height
                    video_view.scaleY = scale / height
                    video_view.pivotX = x
                    video_view.pivotY = 0f
                } else if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                    video_view.scaleX = 1.0f
                    video_view.scaleY = 1.0f
                    video_view.pivotX = 0f
                    video_view.pivotY = 0f
                }
            }

            override fun changedOffset(bottomSheet: View?, slideOffset: Float) {
                Logger.d("changed == cccc")
                val width = DensityUtils.getScreenWidth()
                val height = DensityUtils.getScreenHeight()
                val x = width / 2f
                val py = (bottomSheet?.y!! + ImmersionBar.getStatusBarHeight(this@VideoActivity)) / height
                video_view.scaleX = py
                video_view.scaleY = py
                video_view.pivotX = x
                video_view.pivotY = 0f
            }
        })*/
    }

    override fun addReplySuccess(bean: SingleCommentBean) {
        mCommentDialog?.updateComment(bean)
    }

    override fun getChildReplyListSuccess(bean: VideoReplyBean) {

    }

    override fun addChildReplySuccess(bean: SingleCommentBean) {
        mCommentDialog?.updateChildComment(bean)
    }

    override fun updateLikesSuccess(flag: Int) {
        mLikeState = flag
        tv_attention_count.isSelected = (flag == 0)

        if (flag == 0) {
            mLikeCount++
        } else {
            mLikeCount--
        }
        tv_attention_count.text = mLikeCount.toString()
    }

    override fun updateMerchantStateSuccess(focusState: Int) {
        mMerchantState = focusState
        if (focusState == 0) {
            tv_attention!!.text = "已关注"
        } else {
            tv_attention!!.text = "+ 关注"
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDialog(obj: Any, flag: Int) {
        if (obj is ArrayMap<*, *>) {
            val arrayMap = obj as ArrayMap<String, Any>
            if (flag == 0) {
                val inputDialog = InputDialog.newInstance(arrayMap["reply_id"].toString().toInt(), arrayMap["name"].toString())
                inputDialog.show(supportFragmentManager, "input")
            } else if (flag == 1) {
                Logger.d("${arrayMap["reply_id"].toString().toInt()}, ${arrayMap["name"].toString()}")
                if (mCanSendMsg) {
                    mPresenter?.addChildReply(arrayMap["reply_id"].toString().toInt(), arrayMap["name"].toString())
                } else {
                    showToast("您暂时不能在此视频下评论")
                }
            }
        } else {
            when (flag) {
                1 -> {
                    if (mCanSendMsg) {
                        mPresenter?.addReply(mBean?.id!!, obj.toString())
                    } else {
                        showToast("您暂时不能在此视频下评论")
                    }
                }
                2 -> {
                    mCommentDialog?.resetScrollDistance()
                }
                3 -> {
                    when (obj) {
                        "shareFriend" -> {
                            Glide.with(applicationContext).asBitmap().load(mBean?.media_url).into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    val merchantName = if (mVideoDetailBean?.data?.merchants_name.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else mVideoDetailBean?.data?.merchants_name!!
                                    val merchantDesc = if (mVideoDetailBean?.data?.video_describe.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else mVideoDetailBean?.data?.video_describe!!
                                    WXOptionUtils.openProgram(mWXAPI, "gh_e93b10fb159e", "/pages/store/store?id=${mMerchantId}&inviteCode=${SPUtils.getInt("user_id")}",
                                            merchantName, merchantDesc, resource)
                                }
                            })
                        }
                        "shareFriendCircle" -> {
                            Glide.with(applicationContext).asBitmap().load(mBean?.media_url).into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    val headImg = SPUtils.getString("head_img")
                                    val nickName = SPUtils.getString("nick_name")
                                    val shareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
                                    mVideoDetailBean?.data?.run {
                                        WXOptionUtils.share(mWXAPI, shareUrl, merchants_name!!, video_describe!!, resource, true)
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }

}
