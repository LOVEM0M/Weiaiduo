package com.miyin.zhenbaoqi.bean
import java.io.Serializable
class HomeGoodsHotBean : PagerBean() {

    var data: List<DataBean>? = null

    class DataBean  : Serializable {
        var goodsId = 0
        var goodsName: String? = null
        var goodsAmount = 0
        var goodsImg: String? = null
        var state = 0
        var inventory = 0
        var goodsSales = 0
        var cateId1 = 0
        var cateId2 = 0
        var goodsDescribe: String? = null
        var goodsOriginalAmount = 0
        var goodsFreight = 0
        var goodsType = 0
        var isRestriction = 0
        var isSeven = 0
        var addTime: String? = null
        var publishTime: String? = null
        var tags: Any? = null
        var image: Any? = null
        var goodsVipAmount = 0
        var isRestore = 0
        var isHot = 0
        var content: Any? = null
        var cartNumber: Any? = null
    }

}
