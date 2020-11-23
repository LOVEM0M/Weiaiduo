package com.miyin.zhenbaoqi.ui.mine.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseMvpFragment
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.callback.OnTabSelectCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.*
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.home.activity.SignInActivity
import com.miyin.zhenbaoqi.ui.home.adapter.RecommendAdapter
import com.miyin.zhenbaoqi.ui.message.activity.NewsActivity
import com.miyin.zhenbaoqi.ui.mine.activity.*
import com.miyin.zhenbaoqi.ui.mine.activity.address.AddressActivity
import com.miyin.zhenbaoqi.ui.mine.activity.order.OrderActivity
import com.miyin.zhenbaoqi.ui.mine.activity.wallet.WalletActivity
import com.miyin.zhenbaoqi.ui.mine.contract.MineContract
import com.miyin.zhenbaoqi.ui.mine.presenter.MinePresenter
import com.miyin.zhenbaoqi.ui.shop.activity.purse.PurseActivity
import com.miyin.zhenbaoqi.ui.message.activity.OnlineCustomerActivity
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.tencent.bugly.Bugly.applicationContext
import kotlinx.android.synthetic.main.fragment_mine1.*
import kotlinx.android.synthetic.main.fragment_mine1.smart_refresh_layout
import kotlinx.android.synthetic.main.layout_merchant_center.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@SuppressLint("SetTextI18n")
class MineFragment : BaseMvpFragment<MineContract.IView, MineContract.IPresenter>(), MineContract.IView {

    private var mOnTabSelectCallback: OnTabSelectCallback? = null
    private var mUserType = 1
    private var mMerchantName: String? = null
    private var mUserState = 0
    private var mIsMerchantInit = false
    private var mList = mutableListOf<HomeGoodsHotBean.DataBean>()
    private lateinit var mAdapter: RecommendAdapter
    companion object {
        fun newInstance() = MineFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnTabSelectCallback = context as OnTabSelectCallback
    }

    override fun useEventBus() = true

    override fun useImmersionBar() = true

    override fun getContentView() = R.layout.fragment_mine1

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        mImmersionBar.titleBar(fl_title_bar).init()

        visible(ll_user_center)
        smart_refresh_layout.setEnableRefresh(false)

        iv_avatar.loadImg(SPUtils.getString("head_img"))
        tv_name.text = SPUtils.getString("nick_name")

        recycler_view.run {
            mAdapter = RecommendAdapter(mList)
            mAdapter.setHeaderAndEmpty(true)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            mAdapter.setOnItemClickListener { _, _, position ->
                startActivityForResult<GoodsDetailActivity>(Constant.INTENT_REQUEST_CODE, "goodsId" to mList[position].goodsId)
            }
        }
        smart_refresh_layout.setOnRefreshListener {
            if (mUserState != 0) {
                mPresenter?.getMerchantInfo()
            } else {
                mPresenter?.getUserType()
            }
            smart_refresh_layout.finishRefresh(Constant.DELAY_TIME)
        }

        // 获取浏览足迹
        mPresenter?.getFootprintSize()

        setOnClickListener(mUserListener, tv_tip, iv_avatar, tv_sign_in,fl_apply_merchant, tv_all_order,ll_footprint,ll_collect,
                tv_wait_pay, tv_wait_ship, tv_wait_receipt, tv_after_sale, tv_customer, tv_help, tv_feedback, tv_setting,
                tv_receipt_address)


//        setOnClickListener(mMerchantListener, iv_merchant_switch, iv_merchant_avatar, tv_cumulative_benefit,
//                tv_shop_player_count, tv_shop_fan, tv_manager_shop, tv_manager_auction, tv_shop_home, tv_more_features,
//                tv_see, tv_merchant_wait_ship, tv_merchant_wait_receipt, tv_complete, tv_merchant_after_sale)
    }

    override fun initData() {
        mPresenter?.getUserType()
        mPresenter?.getHomeGoodsHotList()
    }

    override fun createPresenter() = MinePresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mUserType = 2
            mUserState = 1
//            nested_scroll_view.smoothScrollTo(0, 0)
//            nested_scroll_view.setType(1)

            gone(ll_user_center)

            mImmersionBar.reset().titleBar(fl_merchant_title_bar).init()
            smart_refresh_layout.setEnableRefresh(true)

            mPresenter?.getMerchantInfo()
        }
    }
    override fun getHomeGoodsHotListSuccess(bean: HomeGoodsHotBean) {
        with(bean) {
            if (current_page == 1) {
                mList = data!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(data!!)
            }

            val hasMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (hasMore) {
                mAdapter.removeAllFooterView()
            } else {
                mAdapter.addNoMoreDataFooter()
            }
        }
    }
    private val mUserListener = View.OnClickListener {
        when (it.id) {
            R.id.iv_level -> startActivity<UserLevelActivity>()
            R.id.tv_gold -> WebActivity.openActivity(context!!, "签到", "${Constant.INTEGRAL}?token=${SPUtils.getString("token")}")
            R.id.fl_apply_merchant -> mOnTabSelectCallback?.onTabClick(2)
            R.id.tv_sign_in -> startActivity<SignInActivity>()
            R.id.tv_tip -> startActivity<NewsActivity>()
            R.id.iv_avatar -> startActivity<UserInfoActivity>()
            R.id.tv_join_auction_record -> {
//                startActivity<VerifiedActivity>()
                startActivity<AuctionRecordActivity>()
            }
            R.id.ll_collect -> startActivity<CollectActivity1>()
            R.id.tv_verified -> startActivity<VerifiedActivity>()
            R.id.tv_balance, R.id.tv_golden_egg -> {
                if (mUserType == 1) {
                    startActivity<WalletActivity>()
                } else {
                    startActivity<PurseActivity>()
                }
            }
            R.id.tv_coupon, R.id.tv_header_coupon -> startActivity<CouponActivity>()
            R.id.tv_all_order -> startActivity<OrderActivity>("tag" to 0)
            R.id.tv_wait_pay -> startActivity<OrderActivity>("tag" to 1)
            R.id.tv_wait_ship -> startActivity<OrderActivity>("tag" to 2)
            R.id.tv_wait_receipt -> startActivity<OrderActivity>("tag" to 3)
            R.id.tv_after_sale -> startActivity<OrderActivity>("tag" to 4)
            R.id.tv_receipt_address -> startActivity<AddressActivity>()//TODO 收货地址
            R.id.tv_customer -> {
                WebActivity.openActivity(context!!, "官方客服", Constant.SYSTEM_CUSTOMER)
                //mPresenter?.getSystemCustomer()
            }
            R.id.tv_help -> startActivity<HelpCenterActivity>()
            R.id.tv_recommend -> WebActivity.openActivity(applicationContext, "邀请有礼", "${Constant.INVITE}?token=${SPUtils.getToken()}")
            R.id.tv_feedback -> startActivity<FeedbackActivity>()
            R.id.tv_setting -> startActivity<SettingActivity>()
            R.id.tv_integral -> {
                //WebActivity.openActivity(context!!, "签到", "${Constant.INTEGRAL}?token=${SPUtils.getString("token")}")
            }
            R.id.tv_lottery -> WebActivity.openActivity(context!!, "金币抽奖", "${Constant.ZHUAN_PAN}?token=${SPUtils.getString("token")}")
            R.id.ll_footprint -> startActivity<FootprintActivity>()
        }
    }

  /*  private val mMerchantListener = View.OnClickListener {
        when (it.id) {
            R.id.iv_merchant_switch -> {
                mUserState = 0
                nested_scroll_view.smoothScrollTo(0, 0)
                nested_scroll_view.setType(0)
                smart_refresh_layout.setEnableRefresh(false)

                visible(ll_user_center)

                mImmersionBar.reset().titleBar(fl_title_bar).init()
            }
            R.id.iv_merchant_avatar -> {
            }
            R.id.tv_cumulative_benefit -> {
                startActivity<MerchantReportActivity>()
            }
            R.id.tv_shop_player_count -> {
                startActivity<ManagerInviteActivity>("tag" to 0)
            }
            R.id.tv_shop_fan -> {
                startActivity<ManagerInviteActivity>("tag" to 1)
            }
            R.id.tv_manager_shop -> startActivity<ManagerShopActivity>()
            R.id.tv_manager_auction -> startActivity<ManagerAuctionActivity>()
            R.id.tv_shop_home -> startActivity<ShopDetailActivity>("title" to mMerchantName)
            R.id.tv_more_features -> startActivity<MoreFeaturesActivity>()
            R.id.tv_see -> startActivity<ShopOrderActivity>("tag" to 5)
            R.id.tv_merchant_wait_ship -> startActivity<ShopOrderActivity>("tag" to 1)
            R.id.tv_merchant_wait_receipt -> startActivity<ShopOrderActivity>("tag" to 2)
            R.id.tv_complete -> startActivity<ShopOrderActivity>("tag" to 3)
            R.id.tv_merchant_after_sale -> startActivity<ShopOrderActivity>("tag" to 4)
        }
    }*/

    override fun getUserInfoSuccess(bean: UserInfoBean) {
        bean.data?.run {
           /* tv_golden_egg.text = SpanUtils()
                    .appendLine(FormatUtils.formatNumber(balance  )).setFontSize(20, true)
                    .append("余额")
                    .create()
            tv_header_coupon.text = SpanUtils()
                    .appendLine("$coupon_number").setFontSize(20, true)
                    .append("优惠券")
                    .create()
            tv_integral.text = SpanUtils()
                    .appendLine("$point").setFontSize(20, true)
                    .append("唯爱分")
                    .create()
            tv_gold.text = SpanUtils()
                    .appendLine("$gold").setFontSize(20, true)
                    .append("金币")
                    .create()
*/
            if (waitPayNumber == 0) {
                invisible(tv_wait_pay_count)
            } else {
                visible(tv_wait_pay_count)
                tv_wait_pay_count.text = if (waitPayNumber > 99) "99+" else waitPayNumber.toString()
            }
            if (waitSendNumber == 0) {
                invisible(tv_wait_ship_count)
            } else {
                visible(tv_wait_ship_count)
                tv_wait_ship_count.text = if (waitSendNumber > 99) "99+" else waitSendNumber.toString()
            }
            if (waitReceivingNumber == 0) {
                invisible(tv_wait_receipt_count)
            } else {
                visible(tv_wait_receipt_count)
                tv_wait_receipt_count.text = if (waitReceivingNumber > 99) "99+" else waitReceivingNumber.toString()
            }
            if (waitAfterNumber == 0) {
                invisible(tv_after_sale_count)
            } else {
                visible(tv_after_sale_count)
                tv_after_sale_count.text = if (waitAfterNumber > 99) "99+" else waitAfterNumber.toString()
            }
        }
    }

    override fun getUserTypeSuccess(userType: Int) {
        mUserType = userType
        when (userType) {
            1 -> {
            }
            2, 3 -> {

                if (!mIsMerchantInit) {
                    mPresenter?.getMerchantInfo()
                    mIsMerchantInit = true
                }
            }
        }
    }

    override fun getUserLevelSuccess(bean: UserGradeBean) {
        bean.data?.run {
//            iv_level.loadImg(icon)
            bean.data!!.grade_name?.let { SPUtils.putString("level_name", it) }
            bean.data!!.icon?.let { SPUtils.putString("level_img", it) }
        }
    }

    override fun getMerchantInfoSuccess(bean: MerchantBean) {
        with(bean) {
            mMerchantName = merchants_name

            val avatar = if (head_img.isNullOrEmpty()) {
                SPUtils.getString("head_img")
            } else {
                head_img
            }
            iv_merchant_avatar.loadImg(avatar)
            tv_shop_name.text = merchants_name ?: SPUtils.getString("nick_name")

            SPUtils.putInt("merchant_id", merchants_id)
            SPUtils.putString("merchant_head_img", head_img ?: "")
            SPUtils.putString("merchant_name", merchants_name ?: "")
            SPUtils.putString("merchant_desc", merchants_subtitle ?: "")
        }
    }

    override fun getMerchantBaseInfoSuccess(bean: MerchantBaseInfoBean) {
        bean.data?.run {
            val whiteColor = ContextCompat.getColor(context!!, R.color.white)
            tv_cumulative_benefit.text = SpanUtils()
                    .append("累计收益\n")
                    .append(FormatUtils.formatNumber(amount  )).setFontSize(20, true).setForegroundColor(whiteColor)
                    .append("元").setFontSize(10, true).setForegroundColor(whiteColor)
                    .create()
            tv_shop_player_count.text = SpanUtils()
                    .appendLine("新秀数")
                    .append("$play_number").setFontSize(20, true).setForegroundColor(whiteColor)
                    .create()
            tv_shop_fan.text = SpanUtils()
                    .appendLine("专属粉丝")
                    .append("$fans_number").setFontSize(20, true).setForegroundColor(whiteColor)
                    .create()

            if (waitSendNumber == 0) {
                invisible(tv_merchant_wait_ship_count)
            } else {
                visible(tv_merchant_wait_ship_count)
                tv_merchant_wait_ship_count.text = if (waitSendNumber > 99) "99+" else waitSendNumber.toString()
            }
            if (waitReceivingNumber == 0) {
                invisible(tv_merchant_wait_receipt_count)
            } else {
                visible(tv_merchant_wait_receipt_count)
                tv_merchant_wait_receipt_count.text = if (waitReceivingNumber > 99) "99+" else waitReceivingNumber.toString()
            }
            if (successNumber == 0) {
                invisible(tv_complete_count)
            } else {
                visible(tv_complete_count)
                tv_complete_count.text = if (successNumber > 99) "99+" else successNumber.toString()
            }
            if (waitAfterNumber == 0) {
                invisible(tv_merchant_after_sale_count)
            } else {
                visible(tv_merchant_after_sale_count)
                tv_merchant_after_sale_count.text = if (waitAfterNumber > 99) "99+" else waitAfterNumber.toString()
            }
        }
    }

    override fun getMerchantShopDataSuccess(bean: MerchantShopDataBean) {
        bean.data?.run {
            tv_order_count.text = SpanUtils()
                    .appendLine("$today_order_num").setFontSize(14, true)
                    .setForegroundColor(ContextCompat.getColor(context!!, R.color.text_33))
                    .append("今日订单数")
                    .create()
            tv_order_refund.text = SpanUtils()
                    .appendLine("$today_refund_num").setFontSize(14, true)
                    .setForegroundColor(ContextCompat.getColor(context!!, R.color.text_33))
                    .append("退款中")
                    .create()
            tv_order_wait_pay.text = SpanUtils()
                    .appendLine("$today_wait_pay_num").setFontSize(14, true)
                    .setForegroundColor(ContextCompat.getColor(context!!, R.color.text_33))
                    .append("待付款")
                    .create()
            tv_order_wait_ship.text = SpanUtils()
                    .appendLine("$today_wait_send_num").setFontSize(14, true)
                    .setForegroundColor(ContextCompat.getColor(context!!, R.color.text_33))
                    .append("待发货")
                    .create()
            tv_yesterday_visitors.text = SpanUtils()
                    .appendLine("$yes_total_visitor_num").setFontSize(14, true)
                    .setForegroundColor(ContextCompat.getColor(context!!, R.color.text_33))
                    .append("昨日总访客")
                    .create()
            tv_yesterday_amount.text = SpanUtils()
                    .appendLine("$yes_total_amount").setFontSize(14, true)
                    .setForegroundColor(ContextCompat.getColor(context!!, R.color.text_33))
                    .append("昨日成交金额")
                    .create()
        }
    }

    override fun getSystemCustomerSuccess(chatId: String) {
        startActivity<OnlineCustomerActivity>("user_id" to chatId.toInt(), "title" to "系统客服")
    }

    override fun getFootprintSizeSuccess(size: Int) {

    }

    override fun onFailure(msg: String, type: Int) {
        when (type) {
            0 -> {
                visible(recycler_view)
                visible(ll_divider)
//                mAdapter.setEmptyView(R.layout.view_empty, recycler_view)
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateAvatarEvent(msg: String) {
        if ("updateAvatar" == msg) {
            iv_avatar.loadImg(SPUtils.getString("head_img"))
        } else if ("updateNickName" == msg) {
            tv_name.text = SPUtils.getString("nick_name")
        }
    }

}
