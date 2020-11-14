package com.miyin.zhenbaoqi.utils

import java.text.DecimalFormat

object FormatUtils {

    private val mDecimalFormat = DecimalFormat("0.00")

    fun formatNumber(obj: Number) = mDecimalFormat.format(obj)

}