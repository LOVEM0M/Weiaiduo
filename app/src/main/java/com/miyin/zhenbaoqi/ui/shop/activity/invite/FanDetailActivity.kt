package com.miyin.zhenbaoqi.ui.shop.activity.invite

import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.bean.InvitePlayerBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_fan_detail.*

class FanDetailActivity : BaseActivity() {

    private var mBean: InvitePlayerBean.DataBean? = null

    override fun getContentView(): Int {
        mBean = intent.getSerializableExtra("bean") as InvitePlayerBean.DataBean
        return R.layout.activity_fan_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("粉丝详情")
        immersionBar { statusBarDarkFont(true) }

        mBean?.run {
            iv_avatar.loadImg(headImg)
            tv_name.text = SpanUtils()
                    .appendLine(nickName ?: "").setBold()
                    .append("唯爱新秀\u3000已购买${count}单").setFontSize(12, true)
                    .create()
            tv_date_time.text = registerTime

            tv_count.text = SpanUtils()
                    .appendLine("购买单数")
                    .appendLine("$count").setBold().setFontSize(18, true)
                    .create()
            tv_profit.text = SpanUtils()
                    .appendLine("贡献收益")
                    .appendLine("¥${FormatUtils.formatNumber(payAmount / 100f)}").setBold().setFontSize(18, true)
                    .create()
        }
    }

    override fun initData() {

    }

}
