package com.miyin.zhenbaoqi.ui.live.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.bean.MerchantInfoBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.dialog_info_live.*

class LiveInfoDialog : BaseDialogFragment() {

    private var mBean: MerchantInfoBean? = null

    companion object {
        fun newInstance(bean: MerchantInfoBean) = LiveInfoDialog().apply {
            arguments = Bundle().apply {
                putSerializable("bean", bean)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mBean = getSerializable("bean") as MerchantInfoBean?
        }
        return R.layout.dialog_info_live
    }

    override fun initView(view: View) {
        mBean?.data?.run {
            iv_avatar.loadImg(head_img)
            tv_title.text = merchants_name
            tv_desc.text = merchants_subtitle

            tv_info_money.text = SpanUtils()
                    .append(FormatUtils.formatNumber(quality_retention_money / 100f))
                    .setFontSize(20, true).setForegroundColor(Color.BLACK)
                    .append("元").setFontSize(10, true).setForegroundColor(Color.BLACK)
                    .appendLine()
                    .append("店铺保证金")
                    .create()
            tv_info_fans.text = SpanUtils()
                    .append("$focus_number")
                    .setFontSize(20, true).setForegroundColor(Color.BLACK)
                    .append("个").setFontSize(10, true).setForegroundColor(Color.BLACK)
                    .appendLine()
                    .append("粉丝")
                    .create()
            tv_info_score.text = SpanUtils()
                    .append("$merchants_grade")
                    .setFontSize(20, true).setForegroundColor(Color.BLACK)
                    .append("分").setFontSize(10, true).setForegroundColor(Color.BLACK)
                    .appendLine()
                    .append("评分")
                    .create()
        }
        info_close.setOnClickListener { dismiss() }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
