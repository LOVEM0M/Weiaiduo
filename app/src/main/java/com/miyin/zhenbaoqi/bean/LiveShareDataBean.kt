package com.miyin.zhenbaoqi.bean

import java.io.Serializable

data class LiveShareDataBean(var name: String? = null,
                             var icon: String? = null,
                             var cover: String? = null,
                             var describe: String? = null,
                             var fans_count: Int = 0,
                             var people_count: Int = 0) : Serializable {




}