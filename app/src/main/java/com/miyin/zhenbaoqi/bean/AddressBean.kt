package com.miyin.zhenbaoqi.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddressBean : PagerBean(), Serializable {
     var data : DataBeanX?=null
    class DataBeanX{
        var total = 0
        @SerializedName("data")
        var userAddress: List<UserAddressBean>? = null

        class UserAddressBean : Serializable {
            var addTime: String? = null
            var address: String? = null
            var adsId = 0
            var cityId = 0
            var consignee: String? = null
            var countyId = 0
            var isDefault = 0
            var isEnalbe = 0
            var pccName: String? = null
            var phoneNo: String? = null
            var provinceId = 0
            var updateTime: String? = null
            var userId = 0
        }
    }


}
