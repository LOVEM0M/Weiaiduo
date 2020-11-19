package com.miyin.zhenbaoqi.http

import com.miyin.zhenbaoqi.bean.HomeVideoBean
import com.miyin.zhenbaoqi.bean.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiService {

    /**
     * 种草页面商品接口 ***********************************************************************************
     */
    @FormUrlEncoded
    @POST("/shopapi/goods/articlelist")
    fun seedingGoodsList(@FieldMap  map: Map<String, Any>): Single<SeedingBean>
    /*
    * 新VIP用户的优惠
    * */
    @FormUrlEncoded
    @POST("/shopapi/goods/vipFirstFreegoods")
    fun vipFirstFreegoodsList(@FieldMap  map: Map<String, Any>): Single<VipFirstFreegoodsBean>

 /*
    * banner下面的五个按钮
    * */
    @POST("/shopapi/goods/bannercategory")
    fun bannerCategory(): Single<BannerCategoryBean>


    /* 首页所有的一级类目和分类页面所有的一级目录 */
    @POST("/shopapi/goods/firstcategory")
    fun firstCategoryList(): Single<FirstCategoryBean>


    /*
    * 首页一级目录下的查询的商品
    *
    * */
    @FormUrlEncoded
    @POST("/shopapi/goods/firstcategorygoods")
    fun firstcategorygoods(@FieldMap  map: Map<String, Any>): Single<FirstCategoryGoodsBean>


    /*
    * 首页banner下五个bt进入页面后的二级目录
    *
    * */
    @FormUrlEncoded
    @POST("/shopapi/goods/firstcategorySecond")
    fun firstCategorySecond(@FieldMap  map: Map<String, Any>): Single<FirstCategorySecondBean>


    /*
    * 首页banner下五个bt进入页面后的二级目录的商品
    *
    * */
    @FormUrlEncoded
    @POST("/shopapi/goods/secondcategorygoods")
    fun secondCategoryGoods(@FieldMap  map: Map<String, Any>): Single<SecondCategoryGoodsBean>


    /* 一口价|竞拍商品详情 */
    @FormUrlEncoded
    @POST("/shopapi/goods/detailGoods")
    fun goodsDetail(@FieldMap  map: Map<String, Any>): Single<GoodsDetailBean>

    /* 热门推荐商品列表 */
    @POST("/shopapi/goods/hotGoods")
    fun homeGoodsHotList(): Single<HomeGoodsHotBean>

    /*
    * 一周新品
    *
    * */
    @FormUrlEncoded
    @POST("/shopapi/goods/newsgoods")
    fun weekNewGoodsList(@FieldMap  map: Map<String, Any>): Single<WeekNewGoodsBean>

    /**
     * 优惠券接口 ***********************************************************************************
     */
    /* 查找下单可用优惠券列表 */
    @POST("/api/v1/coupons/available/list")
    fun couponAvailableList(@Body requestBody: RequestBody): Single<CouponBean>

    /* 优惠券列表 */
    @POST("/api/v1/coupons/list")
    fun couponList(@Body requestBody: RequestBody): Single<CouponBean>

    /* 领取新人优惠券 */
    @POST("/shopapi/user/takeThreeVip")
    fun takeThreeVipList(): Single<takeThreeVipBean>




    /**
     * 关注商铺接口 *********************************************************************************
     */
    /* 优选店铺 */
    @POST("/api/v1/optMerchants")
    fun homeMerchant(): Single<HomeMerchantBean>

    /* 关注商铺取消关注 */
    @POST("/api/v1/user/merchants/focus")
    fun merchantAttention(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 关注商铺列表 */
    @POST("/api/v1/user/merchants/focus/list")
    fun merchantList(@Body requestBody: RequestBody): Single<ShopAttentionBean>

    /**
     * 参加活动接口 *********************************************************************************
     */
    /* 参加活动 */
    @FormUrlEncoded
    @POST("/api/v1/user/joinActivity/add")
    fun joinActivity(@Field("activity_id") activityId: Int): Single<ResponseBean>

    /* 用户取消参加活动 */
    @FormUrlEncoded
    @POST("/api/v1/user/joinActivity/delete")
    fun cancelActivity(@Field("activity_id") activityId: Int): Single<ResponseBean>

    /**
     * 商品收藏接口 *********************************************************************************
     */
    /* 商品收藏取消收藏 */
    @POST("/api/v1/user/goods/collection")
    fun goodsCollect(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 收藏商品列表 */
    @POST("/api/v1/user/goods/collection/list")
    fun goodsCollectList(@Body requestBody: RequestBody): Single<CollectBean>

    /**
     * 商品竞拍接口 *********************************************************************************
     */
    /* 竞拍商品页面出价记录 */
    @POST("/api/v1/goods/auction/list")
    fun auctionGoodsList(@Body requestBody: RequestBody): Single<AuctionGoodsRecordBean>



    /* 首页上搜索商品列表 */
    @POST("/api/v1/goods/homesearch")
    fun goodsSearch(@Body requestBody: RequestBody): Single<GoodsSearchBean>


    /* 新人特惠商品列表 */
    @POST("/api/v1/goods/new/list")
    fun newGoodsList(@Body requestBody: RequestBody): Single<NewGoodsBean>

    /* 竞拍商品详情里推荐竞拍商品 */
    @FormUrlEncoded
    @POST("/api/v1/goods/recommendAuction")
    fun auctionGoodsRecommendList(@Field("goods_id") goodsId: Int): Single<AuctionGoodsBean>

    /* 搜索商品列表 */
    @POST("/api/v1/goods/search")
    fun searchGoodsList(@Body requestBody: RequestBody): Single<SearchGoodsBean>

    /* 搜索记录 */
    @POST("/api/v1/goods/serach/param/list")
    fun goodsSearchList(): Single<SearchBean>

    /* 添加竞拍商品 */
    @POST("/api/v1/merchants/goods/auction/insert")
    fun addAuctionMerchantGoods(@Body requestBody: RequestBody): Single<GoodsDetailBean>

    /* 商家竞拍商品列表 */
    @POST("/api/v1/merchants/goods/auction/list")
    fun merchantGoodsAuctionList(@Body requestBody: RequestBody): Single<MerchantGoodsBean>

    /* 上传商品图片(此接口为表单类型) */
    @Multipart
    @POST("/api/v1/merchants/goods/img")
    fun uploadMerchantGoodsImg(@Part list: List<MultipartBody.Part>): Single<ImageBean>

    /* 分享直播间 */
    @FormUrlEncoded
    @POST("/api/v1/live/room/share")
    fun shareLiveRoom(@Field("room_id") roomId: Int): Single<ResponseBean>

    /* 直播间秒杀商品列表 */
    @POST("/api/v1/goods/liveroomGoodslist")
    fun obtainLiveRoomGoodsList(@Body requestBody: RequestBody): Single<LiveGoodsBean>

    /* 类目里店铺搜索 */
    @POST("/api/v1/goods/merchantssearch")
    fun searchMerchant(@Body requestBody: RequestBody): Single<SearchShopBean>

    /* 添加商品 */
    @POST("/api/v1/merchants/goods/insert")
    fun addMerchantGoods(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 商家一口价列表 */
    @POST("/api/v1/merchants/goods/list")
    fun merchantGoodsList(@Body requestBody: RequestBody): Single<MerchantGoodsBean>

    /* 店铺商品列表 */
    @POST("/api/v1/merchants/goods/storeList")
    fun merchantGoodsStoreList(@Body requestBody: RequestBody): Single<MerchantGoodsStoreBean>

    /* 商家商品上架 下架 删除 置顶 取消置顶 */
    @POST("/api/v1/merchants/goods/update/state")
    fun updateMerchantGoodsState(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 上传商品视频(此接口为表单类型) */
    @Multipart
    @POST("/api/v1/merchants/goods/video")
    fun uploadVideo(@Part list: List<MultipartBody.Part>): Single<ImageBean>
    //fun uploadVideo(@Part part: MultipartBody.Part): Single<ImageBean>

    /**
     * 商品首页接口 **********************************************************************************
     */
    /* 商品首页列表 */
    @POST("/shopapi/user/bannerList")
    fun homeBanner(): Single<HomeBannerBean>

    /* 点击 Banner 接口 */
    @FormUrlEncoded
    @POST("/api/v1/banner/clickBanner")
    fun bannerClick(@Field("bannerId") bannerId: Int): Single<ResponseBean>

    /**
     * 商家订单相关接口 ******************************************************************************
     */
    /* 所有快递物流 */
    @POST("/api/v1/courierlist")
    fun courierList(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 商户对订单发货 */
    @POST("/api/v1/merchants/merchantssend")
    fun merchantOrderSend(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 商家查询订单详情 */
    @POST("/api/v1/merchants/order/detail")
    fun merchantOrderDetail(@Body requestBody: RequestBody): Single<MerchantOrderDetailBean>

    /* 商户订单列表 */
    @POST("/api/v1/merchants/order/list")
    fun merchantOrderList(@Body requestBody: RequestBody): Single<MerchantOrderBean>

    /* 商户搜索订单列表 */
    @POST("/api/v1/merchants/order/searchlist")
    fun merchantOrderSearch(@Body requestBody: RequestBody): Single<MerchantOrderBean>

    /* 商户修改快递单号 */
    @POST("/api/v1/merchants/updateCourierNo")
    fun updateCourierNo(@Body requestBody: RequestBody): Single<ResponseBean>

    /**
     * 商家黑名单接口 ********************************************************************************
     */
    /* 将用户添加进商家黑名单 */
    @FormUrlEncoded
    @POST("/api/v1/merchants/blanklist/add")
    fun merchantAddBlackList(@Field("user_id") userId: Int): Single<ResponseBean>

    /* 检验用户是否被拉黑 */
    @FormUrlEncoded
    @POST("/api/v1/merchants/blanklist/check")
    fun merchantCheckBlackList(@Field("user_id") userId: Int, @Field("merchants_id") merchantId: Int): Single<CheckBlackListBean>

    /* 获取商家黑名单列表 */
    @POST("/api/v1/merchants/blanklist/list")
    fun merchantBlackList(@Body requestBody: RequestBody): Single<BlackListBean>

    /* 将用户移除商家黑名单 */
    @FormUrlEncoded
    @POST("/api/v1/merchants/blanklist/remove")
    fun merchantRemoveBlackList(@Field("user_id") userId: Int): Single<ResponseBean>

    /**
     * 商户接口 *************************************************************************************
     */
    /* 商户添加保证金 */
    @POST("/api/v1/merchants/addqualitymoney")
    fun merchantAddQualityMoney(@Body requestBody: RequestBody): Single<PayResultBean>

    /* 商户认证上传图片(此接口为表单类型) */
    @Multipart
    @POST("/api/v1/merchants/auth/img")
    fun uploadMerchantAuthImg(@Part list: List<MultipartBody.Part>): Single<ImageBean>

    /* 上传店铺背景图(此接口为表单类型) */
    @Multipart
    @POST("/api/v1/merchants/back/img")
    fun uploadMerchantBackgroundImg(@Part list: List<MultipartBody.Part>): Single<ImageBean>

    /* 商户账户信息 */
    @POST("/api/v1/merchants/baseinfo")
    fun merchantBaseInfo(): Single<MerchantBaseInfoBean>

    /* 上传店铺头像(此接口为表单类型) */
    @Multipart
    @POST("/api/v1/merchants/head/img")
    fun uploadMerchantHeadImg(@Part list: List<MultipartBody.Part>): Single<ImageBean>

    /* 获取商户信息 */
    @POST("/api/v1/merchants/info")
    fun merchantInfo(): Single<MerchantBean>

    /* 修改商户信息 */
    @POST("/api/v1/merchants/info/update")
    fun updateMerchantInfo(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 商户是否认证 */
    @POST("/api/v1/merchants/isAuth")
    fun merchantIsAuth(): Single<MerchantHasAuthBean>

    /* 获取商户是否填写主营类目 */
    @POST("/api/v1/merchants/main/cate")
    fun merchantCate(): Single<ResponseBean>

    /* 修改商户主营类目 */
    @POST("/api/v1/merchants/main/cate/update")
    fun updateMerchantCate(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 商户提交认证 */
    @POST("/api/v1/merchants/submitAuth")
    fun merchantAuth(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 商户的店铺数据 */
    @POST("/api/v1/merchantsShopData")
    fun merchantShopData(): Single<MerchantShopDataBean>

    /* 商户统计报表 */
    @FormUrlEncoded
    @POST("/api/v1/merchantsStatistics")
    fun merchantReport(@Field("type") type: Int): Single<ReportBean>

    /* 商户子账号的添加 */
    @FormUrlEncoded
    @POST("/api/v1/merchantsSubAdd")
    fun merchantsSubAdd(@Field("type") type: Int, @Field("account_name") account: String, @Field("passward") password: String): Single<ResponseBean>

    /* 商户子账号的展示 */
    @POST("/api/v1/merchantsSubList")
    fun merchantsSubList(): Single<SubAccountInfoBean>

    /* 商户子账号的登录 */
    @FormUrlEncoded
    @POST("/api/v1/merchantsSubLogin")
    fun merchantsSubLogin(@Field("account_name") accountName: String, @Field("passward") password: String): Single<SubAccountLoginBean>

    /* 商户子账号的修改 */
    @FormUrlEncoded
    @POST("/api/v1/merchantsSubUpdate")
    fun merchantsSubUpdate(@Field("id") id: Int, @Field("account_name") account: String, @Field("passward") password: String): Single<ResponseBean>

    /* 用户查看商户评价信息 */
    @FormUrlEncoded
    @POST("/api/v1/searchMerchantsEvaluationInfo")
    fun seeMerchantEvaluate(@Field("merchants_id") merchantId: Int): Single<SeeMerchantEvalBean>

    /* 用户查看商户信息 */
    @FormUrlEncoded
    @POST("/api/v1/searchMerchantsInfo")
    fun seeMerchantInfo(@Field("merchants_id") merchantId: Int): Single<MerchantInfoBean>

    /**
     * 商户评价接口 *********************************************************************************
     */
    /* /api/v1/merchants/evaluation/list商户评价列表 */
    fun merchantEvalList(@Body requestBody: RequestBody): Single<MerchantEvalBean>

    /**
     * 商户钱包接口
     */
    /* 商户收益/货款发起提现 */
    @POST("/api/v1/wallet/merchants/cash/add")
    fun walletMerchantWithdraw(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 商户收益详情 */
    @POST("/api/v1/wallet/merchants/earndetail")
    fun walletMerchantEarnDetail(@Body requestBody: RequestBody): Single<MerchantEarnDetail>

    /* 商户收益列表 */
    @POST("/api/v1/wallet/merchants/earnlist")
    fun walletMerchantEarnList(@Body requestBody: RequestBody): Single<MerchantPayList>

    /* 商户货款详情 */
    @POST("/api/v1/wallet/merchants/paymentetail")
    fun walletMerchantDetail(@Body requestBody: RequestBody): Single<MerchantEarnDetail>

    /* 商户货款列表 */
    @POST("/api/v1/wallet/merchants/paymentlist")
    fun walletMerchantPayList(@Body requestBody: RequestBody): Single<MerchantPayList>

    /* 商户钱包总额 */
    @POST("/api/v1/wallet/merchants/totalAmount")
    fun walletMerchantTotalAmount(): Single<TotalAmountBean>

    /* 商户总收益总货款 */
    @POST("/api/v1/wallet/merchants/totalincome")
    fun walletMerchantTotalIncome(@Body requestBody: RequestBody): Single<MerchantTotalIncomeBean>

    /**
     * 地址接口 *************************************************************************************
     */
    /* 添加地址 */
    @POST("/shopapi/address/addressadd")
    fun addAddress(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 删除地址 */
    @FormUrlEncoded
    @POST("/shopapi/address/addressdelete")
    fun deleteAddress(@FieldMap  map: Map<String, Any>): Single<ResponseBean>

    /* 地址列表 */
    @FormUrlEncoded
    @POST("/shopapi/address/addresslist")
    fun addressList(@FieldMap  map: Map<String, Any>): Single<AddressBean>

    /* 编辑地址 */
    @POST("/shopapi/address/addressupdate")
    fun updateAddress(@Body requestBody: RequestBody): Single<ResponseBean>

    /**
     * 建议接口 *************************************************************************************
     */
    /* 添加建议记录 */
    @POST("/api/v1/proposal/insert")
    fun feedback(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 上传图片(反馈) */
    @Multipart
    @POST("/api/v1/proposal/photo/insert")
    fun uploadFeedbackImg(@Part list: List<MultipartBody.Part>): Single<ImageBean>

    /**
     * 提现接口 *************************************************************************************
     */
    /* 发起提现 */
    @POST("/api/v1/user/cash/add")
    fun withdraw(@Body requestBody: RequestBody): Single<ResponseBean>

    /**
     * 点赞接口 *************************************************************************************
     */
    /* 用户视频点赞 */
    @FormUrlEncoded
    @POST("/api/v1/user/videoLikes/add")
    fun addVideoLikes(@Field("like_id") replyId: Int): Single<ResponseBean>

    /* 用户取消视频点赞 */
    @FormUrlEncoded
    @POST("/api/v1/user/videoLikes/delete")
    fun deleteVideoLikes(@Field("like_id") replyId: Int): Single<ResponseBean>

    /**
     * 文章接口 *************************************************************************************
     */
    /* 商家活动列表 */
    @FormUrlEncoded
    @POST("/api/v1/article/activity")
    fun articleActivity(@Field("current_page") currentPage: Int, @Field("page_size") pageSize: Int): Single<ArticleBean>

    /* 商学院文章列表 */
    @POST("/api/v1/article/college")
    fun articleCollege(@Body requestBody: RequestBody): Single<CollegeBean>

    /* 帮助中心文章列表 */
    @POST("/api/v1/article/help")
    fun articleList(@Body requestBody: RequestBody): Single<ArticleBean>

    /* 官方公告文章列表 */
    @POST("/api/v1/article/notice")
    fun noticeList(@Body requestBody: RequestBody): Single<NoticeBean>

    /**
     * 用户接口 *************************************************************************************
     */
    /* 用户余额 */
    @POST("/api/v1/user/balance")
    fun balance(): Single<BalanceBean>

    /* 用户账户信息 */
    @POST("/api/v1/user/baseinfo")
    fun userInfo(): Single<UserInfoBean>

    /* 绑定手机号码 */
    @POST("/api/v1/user/bind/appphone")
    fun bindPhone(@Body requestBody: RequestBody): Single<LoginBean>

    /* 绑定微信 */
    @POST("/api/v1/user/decode/applogin")
    fun bindWX(@Body requestBody: RequestBody): Single<LoginBean>

    /* 修改头像(此接口为表单类型) */
    @Multipart
    @POST("/api/v1/user/head/img/update")
    fun updateHeadImg(@Part list: List<MultipartBody.Part>): Single<ImageBean>

    /* 修改用户基本信息 */
    @POST("/api/v1/user/info/update")
    fun updateUserInfo(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 手机登录 */
    @FormUrlEncoded
    @POST("/shopapi/user/login")
    fun login(@FieldMap  map: Map<String, Any>): Single<LoginBean>

    /* 用户推送开关 */
    @POST("/api/v1/user/push/switch")
    fun pushSwitch(): Single<PushSwitchBean>

    /* 用户商户信息 */
    @POST("/api/v1/user/merchantsinfo")
    fun userType(): Single<UserTypeBean>

    /* 通过用户 id 查看商户 id */
    @FormUrlEncoded
    @POST("/api/v1/user/searchmerchantsid")
    fun searchMerchantsId(@Field("user_id") userId: Int): Single<UserTypeBean>

    /* 通过子账户查看商户 id */
    @FormUrlEncoded
    @POST("/api/v1/user/searchMerchantsSub")
    fun searchMerchantsSub(@Field("account_name") accountName: String): Single<UserTypeBean>

    /* 修改用户推送开关 */
    @POST("/api/v1/user/push/switch/update")
    fun updatePushSwitch(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 实名认证 */
    @POST("/api/v1/user/real/name")
    fun realName(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 回显实名认证 */
    @POST("/api/v1/user/real/namelist")
    fun showRealName(): Single<RealNameBean>

    /* 用户推荐人信息 */
    @POST("/api/v1/user/referrer")
    fun referrerInfo(): Single<ReferrerBean>

    /* 用户举报 */
    @FormUrlEncoded
    @POST("/api/v1/user/report")
    fun report(@FieldMap map: Map<String, Any>): Single<ResponseBean>

    /* 发送验证码 */
    @FormUrlEncoded
    @POST("/shopapi/user/sendCode")
    fun sendMessage(@FieldMap  map: Map<String, Any>): Single<ResponseBean>

    /**
     * 用户消息接口 *********************************************************************************
     */
    /* 消息列表 */
    @POST("/api/v1/user/message/list")
    fun messageList(@Body requestBody: RequestBody): Single<MessageBean>

    /* 用户最新消息及未读条数 */
    @POST("/api/v1/user/message/size")
    fun notReadMessageSize(): Single<MessageBean>

    /**
     * 申请成为商户接口 *****************************************************************************
     */
    /* 申请成为商户 */
    @POST("/api/v1/user/apply/merchants")
    fun applyMerchant(@Body requestBody: RequestBody): Single<PayResultBean>

    /**
     * 直播接口 *************************************************************************************
     */
    /* 直播列表banner */
    @POST("/api/v1/live/banner/list")
    fun liveBanner(): Single<LiveBannerBean>

    /* 上传封面(此接口为表单类型) */
    @Multipart
    @POST("/api/v1/live/face/syncFace")
    fun liveRoomCover(@Part list: List<MultipartBody.Part>): Single<ResponseBean>

    /* 添加竞价商品 */
    @POST("/api/v1/live/goods/auction/insert")
    fun liveGoodsAuctionInsert(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 添加秒杀商品 */
    @POST("/api/v1/live/goods/ms/insert")
    fun liveGoodsSpikeInsert(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 关闭直播 */
    @FormUrlEncoded
    @POST("/api/v1/live/room/close")
    fun liveRoomClose(@Field("room_id") roomId: Int): Single<ResponseBean>

    /* 创建直播间 */
    @POST("/api/v1/live/room/create")
    fun liveRoomCreate(): Single<LiveRoomCreateBean>

    /* 进入直播间 */
    @FormUrlEncoded
    @POST("/api/v1/live/room/entry")
    fun liveRoomEntry(@Field("room_id") roomId: Int): Single<LiveEntryRoomBean>

    /* 直播列表 */
    @FormUrlEncoded
    @POST("/api/v1/live/room/list")
    fun liveRoomList(@Field("current_page") currentPage: Int, @Field("page_size") pageSize: Int, @Field("merchants_cat") merchantCate: Int): Single<LiveRoomBean>

    /* 直播间秒杀和拍卖商品列表 */
    @FormUrlEncoded
    @POST("/api/v1/live/liveroom_goods_list")
    fun obtainLiveGoodsList(@FieldMap map: Map<String, Any>): Single<LiveGoodsBean>

    /* 获取直播间正在拍卖的拍品 */
    @FormUrlEncoded
    @POST("/api/v1/live/room/auction_goods")
    fun liveRoomAuctionGoods(@Field("room_id") roomId: Int): Single<GoodsDetailBean>

    /* 直播间热度榜列表 */
    @FormUrlEncoded
    @POST("/api/v1/live/live/hot_list")
    fun obtainHotList(@Field("current_page") currentPage: Int, @Field("page_size") pageSize: Int): Single<LiveHotBean>

    /* 直播间分享棒列表 */
    @FormUrlEncoded
    @POST("/api/v1/live/room/share_list")
    fun obtainShareList(@Field("current_page") currentPage: Int, @Field("page_size") pageSize: Int): Single<LiveShareBean>

    /* 开启直播 */
    @FormUrlEncoded
    @POST("/api/v1/live/room/open")
    fun liveRoomOpen(@Field("room_id") roomId: Int, @Field("face_url") face_url: String?, @Field("room_name") room_name: String?): Single<LiveRoomOpenBean>

    /* 直播间详情 */
    @FormUrlEncoded
    @POST("/api/v1/live/room/detail")
    fun obtainLiveInfoData(@Field("room_id") roomId: Int): Single<ResponseBean>

    /* 获取直播间人气 */
    @FormUrlEncoded
    @POST("/api/v1/live/room/popularity")
    fun liveRoomPopularity(@Field("room_id") roomId: Int): Single<LiveRoomPopularityBean>

    /**
     * 省市区，快递公司，商品类别等数据接口 **********************************************************
     *0省列表，1快递列表
     * 没有商品类别了
     */
    @FormUrlEncoded
    @POST("/shopapi/address/dictlist")
    fun parentList(@FieldMap  map: Map<String, Any>): Single<CityBean>

    /* 根据父id查找数据列表 */
    @FormUrlEncoded
    @POST("/shopapi/address/sondictlist")
    fun sonList(@FieldMap  map: Map<String, Any>): Single<CityBean>

    /* 推荐分类列表 */
    @POST("/api/v1/dict/recommend/list")
    fun recommendList(): Single<CityBean>


    /**
     * 福利等级接口 **********************************************************************************
     */
    /* 是否能领取新人优惠券 */
    @POST("/api/v1/welfare/couponCanGet")
    fun welfareCouponCanGet(): Single<CouponGetBean>

    /* 邀请人红包信息 */
    @POST("/api/v1/welfare/inRedInfo")
    fun welfareInviteRedInfo(): Single<ResponseBean>

    /* 等级积分列表展示 */
    @POST("/api/v1/welfare/pointRuleList")
    fun pointRuleList(): Single<UserLevelBean>

    /* 新人一键领取优惠券 */
    @POST("/api/v1/welfare/receiveCoupon")
    fun welfareReceiveCoupon(): Single<ResponseBean>

    /* 新人福利券列表 */
    @POST("/api/v1/welfare/registerCouponList")
    fun welfareCouponList(): Single<WelfareCouponBean>

    /* 分享红人榜 */
    @POST("/api/v1/welfare/shareredlist")
    fun welfareSharedList(): Single<ResponseBean>

    /* 用户等级 */
    @POST("/api/v1/welfare/userGrade")
    fun userGrade(): Single<UserGradeBean>

    /**
     * 竞拍接口 *************************************************************************************
     */
    /* 支付保证金 */
    @POST("/api/v1/auction/bind")
    fun auctionBind(@Body requestBody: RequestBody): Single<PayResultBean>

    /* 报价 */
    @POST("/api/v1/auction/bind/offer")
    fun auctionBindOffer(@Body requestBody: RequestBody): Single<BindOfferBean>

    /* 鉴真宝服务的金额配置 */
    @POST("/api/v1/user/amountRule")
    fun goodsAmountRule(): Single<ServiceAmountBean>

    /* 我的参拍记录 */
    @POST("/api/v1/user/auction/bind/list")
    fun auctionBindList(@Body requestBody: RequestBody): Single<AuctionRecoredBean>

    /* 竞拍商品下单 */
    @POST("/api/v1/user/auction/order/insert")
    fun auctionOrderInsert(@Body requestBody: RequestBody): Single<PayResultBean>

    /* 竞拍结束调用处理竞拍 */
    @POST("/api/v1/user/callhandleGoods")
    fun callHandleGoods(): Single<ResponseBean>

    /**
     * 素材接口 *************************************************************************************
     */
    /* 获取商家素材列表 */
    @POST("/api/v1/merchants/material/list")
    fun merchantMaterialList(@Body requestBody: RequestBody): Single<MaterialBean>

    /**
     * 聊天室 ***************************************************************************************
     */
    /* 回调 */
    @POST("/api/v1/chat/callBack")
    fun chatCallback(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 获取客服ID */
    @POST("/api/v1/chat/customer/service")
    fun chatCuscomerService(): Single<ChatCustomerBean>

    /* 校验客户会话是否有效 */
    @POST("/api/v1/chat/customer/service/check")
    fun chatCustomerServiceCheck(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 保存用户聊天信息 */
    @POST("/api/v1/chat/msg/save")
    fun chatMsgSave(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 获取用户在线状态 */
    @POST("/api/v1/chat/onlineStatus")
    fun chatOnlineStatus(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 单个账号导入 */
    @POST("/api/v1/account/import")
    fun chatImport(): Single<ResponseBean>

    /* 获取子账号sig */
    @FormUrlEncoded
    @POST("/api/v1/chat/subUserSig")
    fun chatSubUserSign(@Field("account_name") accountName: String): Single<UserSignBean>

    //TODO
/*    *//* 获取用户sig *//*
    @POST("/api/v1/chat/userSig")
    fun chatUserSign(): Single<UserSignBean>*/

    /**
     * 视频接口 *************************************************************************************
     */
    /* 获取视频列表(主页视频) */
    @POST("/api/v1/home/video/list")
    fun homeVideoList(@Body requestBody: RequestBody): Single<HomeVideoBean>

    /* 删除商家视频 */
    @FormUrlEncoded
    @POST("/api/v1/merchants/video/delete")
    fun merchantVideoDelete(@Field("video_id") videoId: Int): Single<ResponseBean>

    /* 获取商家视频详情 */
    @FormUrlEncoded
    @POST("/api/v1/merchants/video/detail")
    fun merchantVideoDetail(@Field("video_id") videoId: Int): Single<VideoDetailBean>

    /* 编辑商家视频 */
    @Multipart
    @POST("/api/v1/merchants/video/edit")
    fun merchantVideoEdit(@Part list: List<MultipartBody.Part>): Single<ResponseBean>

    /* 获取商家视频列表 */
    @POST("/api/v1/merchants/video/list")
    fun merchantVideoList(@Body requestBody: RequestBody): Single<HomeVideoBean>

    /* 商家上传视频 */
    @Multipart
    @POST("/api/v1/merchants/video/upload")
    fun merchantVideoUpload(@Part list: List<MultipartBody.Part>): Single<ResponseBean>

    /**
     * 订单售后接口 *********************************************************************************
     */
    /* 商户同意仅退款 */
    @FormUrlEncoded
    @POST("/api/v1/merchants/order/after/agreerefund")
    fun merchantAgreeFund(@Field("order_number") orderNumber: String): Single<ResponseBean>

    /* 商户审核售后 */
    @POST("/api/v1/merchants/order/after/audit")
    fun merchantAfterSaleVerify(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 商户签收 */
    @POST("/api/v1/merchants/order/after/receiving")
    fun merchantReceive(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 售后订单物流查看 */
    @POST("/api/v1/user/afterorder/courier")
    fun afterOrderCourier(@Body requestBody: RequestBody): Single<OrderLogisticsBean>

    /* 发起售后 */
    @POST("/api/v1/user/order/after")
    fun afterSaleLaunch(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 售后取消 */
    @POST("/api/v1/user/order/after/cancel")
    fun afterSaleCancel(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 售后发货 */
    @POST("/api/v1/user/order/after/courier")
    fun afterSaleShip(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 售后详情 */
    @POST("/api/v1/user/order/after/detail")
    fun afterSaleDetail(@Body requestBody: RequestBody): Single<AfterSaleDetailBean>

    /* 上传图片(此接口为表单类型) */
    @Multipart
    @POST("/api/v1/user/order/after/photo/insert")
    fun uploadAfterSaleImg(@Part list: List<MultipartBody.Part>): Single<ImageBean>

    /**
     * 订单接口 *************************************************************************************
     */
    /* 用户评价标签列表 */
    @POST("/api/v1/evaluationgradeList")
    fun evaluateGradeList(): Single<ResponseBean>

    /* 用户点评标签列表 */
    @POST("/api/v1/evaluationTagList")
    fun evaluateList(): Single<ResponseBean>

    /* 删除订单 */
    @POST("/api/v1/user/delOrder")
    fun deleteOrder(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 订单取消 */
    @POST("/api/v1/user/order/cancel")
    fun orderCancel(@Body requestBody: RequestBody): Single<OrderCancelBean>

    /* 订单物流查看 */
    @POST("/api/v1/user/order/courier")
    fun orderLogistics(@Body requestBody: RequestBody): Single<OrderLogisticsBean>

    /* 订单详情 */
    @POST("/api/v1/user/order/detail")
    fun orderDetail(@Body requestBody: RequestBody): Single<OrderDetailBean>

    /* 用户评价 */
    @POST("/api/v1/user/order/evaluation")
    fun orderEval(@Body requestBody: RequestBody): Single<OrderEvalBean>

    /* 普通商品下单 */
    @POST("/api/v1/user/order/insert")
    fun orderPay(@Body requestBody: RequestBody): Single<PayResultBean>

    /* 订单列表 */
    @POST("/api/v1/user/order/list")
    fun orderList(@Body requestBody: RequestBody): Single<OrderBean>

    /* 确认收货 */
    @POST("/api/v1/user/order/receiving")
    fun orderConfirmReceive(@Body requestBody: RequestBody): Single<OrderConfirmReceiveBean>

    /* 用户搜索订单列表 */
    @POST("/api/v1/user/order/searchlist")
    fun searchOrderList(@Body requestBody: RequestBody): Single<OrderBean>

    /* 评价页面 */
    @POST("/api/v1/user/order/to/evaluation")
    fun orderEvalList(@Body requestBody: RequestBody): Single<OrderEvalListBean>

    /* 待付款商品下单 */
    @POST("/api/v1/user/order/waitinsert")
    fun waitOrderPay(@Body requestBody: RequestBody): Single<PayResultBean>

    /**
     * 评论接口 *************************************************************************************
     */
    /* 添加子评论 */
    @FormUrlEncoded
    @POST("/api/v1/user/childReply/add")
    fun addVideoChildReply(@Field("reply_id") replyId: Int, @Field("reply_content") replyContent: String): Single<SingleCommentBean>

    /* 获取子评论列表 */
    @FormUrlEncoded
    @POST("/api/v1/user/childReply/list")
    fun videoChildReplyList(@Field("reply_id") replyId: Int, @Field("current_page") currentPage: Int, @Field("page_size") pageSize: Int): Single<VideoReplyBean>

    /* 用户视频评论 */
    @FormUrlEncoded
    @POST("/api/v1/user/videoReply/add")
    fun addVideoReply(@Field("reply_id") replyId: Int, @Field("reply_content") replyContent: String): Single<SingleCommentBean>

    /* 获取视频评论列表 */
    @FormUrlEncoded
    @POST("/api/v1/user/videoReply/list")
    fun videoReplyList(@Field("video_id") videoId: Int, @Field("current_page") currentPage: Int, @Field("page_size") pageSize: Int): Single<VideoReplyBean>

    /**
     * 账单接口 *************************************************************************************
     */
    /* 账单提现详情 */
    @POST("/api/v1/user/change/cash")
    fun billWithdrawDetail(@Body requestBody: RequestBody): Single<BillWithdrawBean>

    /* 账单收入详情 */
    @POST("/api/v1/user/change/divided")
    fun billIncomeDetail(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 账单列表 */
    @POST("/api/v1/user/change/list")
    fun billList(@Body requestBody: RequestBody): Single<BillBean>

    /* 账单支出详情 */
    @POST("/api/v1/user/change/order")
    fun billPayDetail(@Body requestBody: RequestBody): Single<BillPayBean>

    /**
     * 邀请管理接口
     */
    /* 回显商户微信号|得到商户基本信息 */
    @POST("/api/v1/invite/echoMerchantsWechat")
    fun inviteMerchantWeChat(): Single<MerchantWeChatBean>

    /* 专属粉丝列表 */
    @POST("/api/v1/invite/exclusiveFansList")
    fun inviteExclusiveFansList(@Body requestBody: RequestBody): Single<InvitePlayerBean>

    /* 商家设置微信号 */
    @FormUrlEncoded
    @POST("/api/v1/invite/merchantsAddWechat")
    fun inviteMerchantAddWeChat(@Field("wechat_id") weChatId: String, @Field("wechat_image") weChatImage: String): Single<ResponseBean>

    /* 普通粉丝列表 */
    @POST("/api/v1/invite/ordinaryFansList")
    fun inviteOrdinaryFansList(@Body requestBody: RequestBody): Single<InvitePlayerBean>

    /* 玩家列表 */
    @POST("/api/v1/invite/playerList")
    fun invitePlayerList(@Body requestBody: RequestBody): Single<InvitePlayerBean>

    /**
     * 银行卡接口 ***********************************************************************************
     */
    /* 添加银行卡 */
    @POST("/api/v1/user/bank/add")
    fun addBankCard(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 删除银行卡 */
    @POST("/api/v1/user/bank/delete")
    fun deleteBankCard(@Body requestBody: RequestBody): Single<ResponseBean>

    /* 银行卡列表 */
    @POST("/api/v1/user/bank/list")
    fun bankCardList(): Single<BankCardBean>

    /* 编辑银行卡 */
    @POST("/api/v1/user/bank/update")
    fun updateBankCard(@Body requestBody: RequestBody): Single<ResponseBean>

    /**
     * 银行接口 *************************************************************************************
     */
    /* 银行列表 */
    @POST("/api/v1/bank/list")
    fun bankList(): Single<BankBean>

    /* 更新版本接口 */
    @POST("/api/v1/user/updateVersion")
    fun updateApk(): Single<UpdateAppBean>

}
