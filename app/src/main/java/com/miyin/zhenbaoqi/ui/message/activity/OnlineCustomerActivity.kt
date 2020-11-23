package com.miyin.zhenbaoqi.ui.message.activity

import android.app.Activity
import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.tencent.imsdk.TIMConversationType
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil
import kotlinx.android.synthetic.main.activity_online_customer.*
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.UserTypeBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.*
import com.miyin.zhenbaoqi.ui.message.contract.OnlineCustomerContract
import com.miyin.zhenbaoqi.ui.message.dialog.CustomerOperateDialog
import com.miyin.zhenbaoqi.ui.message.presenter.OnlineCustomerPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.AuctionDetailActivity
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import com.miyin.zhenbaoqi.ui.sort.activity.MerchantMessageActivity
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.orhanobut.logger.Logger
import com.tencent.imsdk.TIMCustomElem
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageCustomHolder
import kotlinx.android.synthetic.main.layout_goods_info.view.*
import org.json.JSONObject

class OnlineCustomerActivity : BaseMvpActivity<OnlineCustomerContract.IView, OnlineCustomerContract.IPresenter>(), OnlineCustomerContract.IView, OnDialogCallback {

    private var mTitle: String? = null
    private var mUserId = 0
    private var mAccountName: String? = null
    private var mData: String? = null
    private var mMerchantId = 0
    private var mType = 1
    private var mCanSendMsg = false

    override fun getContentView(): Int {
        with(intent) {
            mTitle = getStringExtra("title")
            mUserId = getIntExtra("user_id", 0)
            mAccountName = getStringExtra("account_name")
            mData = getStringExtra("data")
        }
        return R.layout.activity_online_customer
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(fl_container).statusBarDarkFont(true).keyboardEnable(true).init()
        chat_layout.titleBar.visibility = View.GONE

        Logger.d("account == $mAccountName, user_id == $mUserId")

        iv_back.setOnClickListener { finish() }
        tv_title.text = mTitle
        tv_right_title.setOnClickListener {
            val merchantId = if (mType == 1) {
                SPUtils.getInt("merchant_id", 0)
            } else {
                mMerchantId
            }
            startActivity<MerchantMessageActivity>("merchant_id" to merchantId)
        }
        iv_right_image.setOnClickListener {
            val dialog = CustomerOperateDialog.newInstance(mType)
            dialog.show(supportFragmentManager, "customerOperate")
        }

        chat_layout.initDefault()
        val chatInfo = ChatInfo().apply {
            id = if (mAccountName.isNullOrEmpty()) mUserId.toString() else mAccountName
            type = TIMConversationType.C2C
            chatName = mTitle

        }
        chat_layout.setChatInfo(chatInfo)

        chat_layout.messageLayout.run {
            avatarRadius = 50
            avatarSize = intArrayOf(48, 48)

            setOnCustomMessageDrawListener { parent, info ->
                var view: View? = null
                // 获取到自定义消息的 JSON 数据
                val elem = info.timMessage.getElement(0) as TIMCustomElem
                // 自定义的 JSON 数据，需要解析成 bean 实例
                val json = String(elem.data)
                val jsonObject = JSONObject(json)
                // 通过类型来创建不同的自定义消息展示 View
                if (jsonObject.has("type")) {
                    when (jsonObject.get("type").toString()) { // 商品信息
                        "1" -> {
                            view = View.inflate(this@OnlineCustomerActivity, R.layout.layout_goods_info, null)
                            parent.addMessageContentView(view)

                            if (parent is MessageCustomHolder) {
                                parent.msgContentFrame.background = null
                            }
                        }
                        else -> {
                            view = View.inflate(this@OnlineCustomerActivity, R.layout.layout_goods_info, null)
                            parent.addMessageContentView(view)

                            if (parent is MessageCustomHolder) {
                                parent.msgContentFrame.background = null
                            }
                        }
                    }
                } else {
                    view = View.inflate(this@OnlineCustomerActivity, R.layout.layout_goods_info, null)
                    parent.addMessageContentView(view)

                    if (parent is MessageCustomHolder) {
                        parent.msgContentFrame.background = null
                    }
                }

                view?.run {
                    val params = iv_cover.layoutParams
                    params.width = getDimensionPixelSize(R.dimen.dp_175)
                    iv_cover.layoutParams = params

                    if (jsonObject.has("goods_img")) {
                        val cover = jsonObject.getString("goods_img")
                        val goodsImg = when {
                            cover.isNullOrEmpty() -> ""
                            cover.contains(",") -> cover.split(",")[0]
                            else -> cover
                        }
                        val transform = RoundCornersTransform(getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.TOP)
                        iv_cover.transform(goodsImg, transform)
                    }

                    if (jsonObject.has("goods_name")) {
                        tv_title.text = jsonObject.getString("goods_name")
                    }
                    if (jsonObject.has("goods_amount")) {
                        tv_price.text = SpanUtils()
                                .append("¥").setFontSize(12, true)
                                .append(FormatUtils.formatNumber(jsonObject.getLong("goods_amount")  ))
                                .create()
                    }

                    setOnClickListener {
                        if (jsonObject.has("goods_id") && jsonObject.has("goods_type")) {
                            val goodsType = jsonObject.getString("goods_type")
                            if (goodsType == "1") {
                                startActivity<GoodsDetailActivity>("goods_id" to jsonObject.getInt("goods_id"))
                            } else {
                                startActivity<AuctionDetailActivity>("goods_id" to jsonObject.getInt("goods_id"))
                            }
                        }
                    }
                }
            }
        }

        chat_layout.inputLayout.setMessageHandler {
            if (mCanSendMsg) {
                chat_layout.sendMessage(it, false)
            } else {
                showToast("您不能与此用户聊天")
            }
        }

    }

    override fun initData() {
        if (mAccountName.isNullOrEmpty()) {
            mPresenter?.getMerchantId(mUserId)
        } else {
            mPresenter?.getSubMerchantId(mAccountName!!)
        }
    }

    override fun createPresenter() = OnlineCustomerPresenter()

    override fun getMerchantIdSuccess(bean: UserTypeBean) {
        with(bean) {
            mType = user_type
            mMerchantId = merchants_id
            mCanSendMsg = check_status

            if (null != mData && mCanSendMsg) {
                val messageInfo = MessageInfoUtil.buildCustomMessage(mData)
                chat_layout.sendMessage(messageInfo, true)
            }
        }
    }

    override fun addBlackListSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            when (obj) {
                "report" -> {
                    startActivity<ReportActivity>("merchant_id" to mMerchantId)
                }
                "addBlackList" -> {
                    AlertDialog.Builder(this)
                            .setTitle("确认拉黑")
                            .setMessage(SpanUtils().append("确认拉黑").setForegroundColor(ContextCompat.getColor(this, R.color.text_66))
                                    .append(mTitle
                                            ?: "").setForegroundColor(ContextCompat.getColor(this, R.color.text_66))
                                    .append("，拉黑后您将收不到他的消息！").setForegroundColor(ContextCompat.getColor(this, R.color.text_66))
                                    .create())
                            .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                                mPresenter?.addBlackList(mUserId)
                                dialog.dismiss()
                            }
                            .setNegativeButton(getString(R.string.cancel), null)
                            .show()
                }
            }
        }
    }

}
