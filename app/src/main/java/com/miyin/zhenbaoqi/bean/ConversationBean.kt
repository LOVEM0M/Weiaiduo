package com.miyin.zhenbaoqi.bean

class ConversationBean {

    var msgBody: List<MsgBean>? = null
    var msgKey: String? = null
    var msgLifeTime = 0
    var msgRandom = 0L
    var msgTimeStamp = 0L
    var syncOtherMachine = 0
    var toAccount: String? = null

    class MsgBean {
        var msgType: String? = null
        var msgContent: String? = null
    }

}