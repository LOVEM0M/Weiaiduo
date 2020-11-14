package com.miyin.zhenbaoqi.bean

import android.text.TextUtils

class AliPayResultBean(rawResult: Map<String, String>?) {

    var resultStatus: String? = null
        private set
    var result: String? = null
        private set
    var memo: String? = null
        private set

    init {
        rawResult?.let {
            for (key in it.keys) {
                when {
                    TextUtils.equals(key, "resultStatus") -> resultStatus = rawResult[key]
                    TextUtils.equals(key, "result") -> result = rawResult[key]
                    TextUtils.equals(key, "memo") -> memo = rawResult[key]
                }
            }
        }
    }

    override fun toString(): String {
        return ("resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}")
    }

}
