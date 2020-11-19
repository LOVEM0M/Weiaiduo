package com.miyin.zhenbaoqi.bean

import java.io.Serializable
class WeekNewGoodsBean : PagerBean() {
   var data :  DataBeanX ? = null
    class DataBeanX : Serializable{
       var total = 0
        var data : List<DataBean>?= null
        class DataBean {
            var addTime: String? = null
            var cartNumber = 0
            var cateId1 = 0
            var cateId2 = 0
            var content: String? = null
            var goodsAmount = 0
            var goodsDescribe: String? = null
            var goodsFreight = 0
            var goodsId = 0
            var goodsImg: String? = null
            var goodsName: String? = null
            var goodsOriginalAmount = 0
            var goodsSales = 0
            var goodsType = 0
            var goodsVipAmount = 0
            var image: String? = null
            var inventory = 0
            var isHot = 0
            var isRestore = 0
            var isRestriction = 0
            var isSeven = 0
            var publishTime: String? = null
            var state = 0
            var tags: String? = null
        }
    }

}
