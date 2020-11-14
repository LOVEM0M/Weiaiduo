package com.miyin.zhenbaoqi.ui.live.callback

interface StandardCallback {
    /**
     * @param errCode 错误码
     * @param errInfo 错误信息
     */
    fun onError(errCode: Int, errInfo: String?)

    fun onSuccess()

}