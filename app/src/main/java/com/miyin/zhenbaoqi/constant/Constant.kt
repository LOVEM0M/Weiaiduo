package com.miyin.zhenbaoqi.constant

import android.os.Environment
import com.miyin.zhenbaoqi.BuildConfig
import com.tencent.rtmp.ui.TXCloudVideoView

object Constant {

    const val DELAY_TIME = 1500
    const val PERMISSION_REQUEST_CODE = 200
    const val INTENT_REQUEST_CODE = 500
    const val BASE_URL = BuildConfig.APP_BASE_URL
    const val WX_TOKEN = "wx_token"
    const val WX_USER_INFO = "wx_user_info"
    const val DOMAIN = "domain"
    const val SECRET = "bbd3cc8ad264e6f290d77a7c2d84589a"
    const val TENCENT_APPID = "1400324831"

    //    const val TENCENT_APPID = "1400324080"
    val CACHE_PATH = Environment.getExternalStorageDirectory().absolutePath + "/ZhenBaoQi"
    var VIDEO_VIEW: TXCloudVideoView? = null

    /* H5 链接 */
    const val USER_AGREEMENT = "${BASE_URL}zhen_url/user_agreement.html"
    const val PRIVACY_PROTOCOL = "${BASE_URL}zhen_url/privacy_agreement.html"
    const val SHARE_INVITE = "${BASE_URL}zhen_url/invitee.html"
    const val INVITE = "${BASE_URL}zhen_url/yaoqing.html"
    const val RECHARGE = "${BASE_URL}zhen_url/chongzhixieyi.html"
    const val AUTH_STORE = "${BASE_URL}zhen_url/renzheng.html"
    const val WARRANTY_DESC = "${BASE_URL}zhen_url/storeAuthentication.html"
    const val GOODS_CATEGORY = "${BASE_URL}zhen_url/goodList/list1.html"
    const val ZHEN_PIN_PRODUCT = "${BASE_URL}zhen_url/Genuine_product.html"
    const val LEAK_COLLECT = "${BASE_URL}zhen_url/Leak_collection.html"
    const val START_SHOOT = "${BASE_URL}zhen_url/Start_shooting.html"
    const val ZHEN_PIN = "${BASE_URL}zhen_url/zhenp.html"
    const val ZHEN_BAO_FU_WU = "${BASE_URL}zhen_url/fuwu.html"
    const val AUCTION_EXPLAIN = "${BASE_URL}zhen_url/auctionExplain.html"
    const val ZHUAN_PAN = "${BASE_URL}zhen_url/zhuanPan/zhuanPan.html"
    const val INTEGRAL = "${BASE_URL}zhen_url/integral.html"
    const val SYSTEM_RULE = "${BASE_URL}zhen_url/platform-rules/rules.html"
    const val SYSTEM_CUSTOMER = "https://cschat-ccs.aliyun.com/index.htm?tntInstId=_1nv1XrU&scene=SCE00006559"

    /*
    https://www.zbq888.cn/zhen_url/renzheng.html
https://www.zbq888.cn/zhen_url/fuwu.html
https://www.zbq888.cn/zhen_url/store.html
https://www.zbq888.cn/zhen_url/renzheng.html
https://www.zbq888.cn/zhen_url/fuwu.html
https://www.zbq888.cn/zhen_url/store.html
https://www.zbq888.cn/zhen_url/yaoqing.html
31_NlQ9gqXUJcuO2mumMZ9aif3CWHhv6vCY229KLUlnIMR2B9xwWWFO6hrdiNzbob3cc1lCWeDqO0D_WfBkLGJzmYFPTzjPDo-ysgiKHyeZpZ9y9yudUOkSBDLuiq9gg7LU-TsSydszIcUVl4iWEAJfAEAIEN
     */
}
