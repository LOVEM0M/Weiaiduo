package com.miyin.zhenbaoqi.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddressBean : PagerBean(), Serializable {

    @SerializedName("userAddresss")
    var userAddress: List<UserAddressBean>? = null

    class UserAddressBean : Serializable {
        var address: String? = null
        var ads_id: Int = 0
        var city_id: Int = 0
        var consignee: String? = null
        var county_id: Int = 0
        var is_default: Int = 0
        var pcc_name: String? = null
        var phone_no: String? = null
        var province_id: Int = 0
    }

}
