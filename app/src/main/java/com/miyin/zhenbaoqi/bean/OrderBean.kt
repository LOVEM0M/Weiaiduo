package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class OrderBean : PagerBean() {
   var data : DataBeanX?= null
    class DataBeanX{
        var total = 0
        var data : List<DataBean>? = null
        class DataBean : Serializable {
            var orderId = 0
            var orderNumber: String? = null
            var userId = 0
            var orderTime: String? = null
            var payTime: String? = null
            var deliveryTime: Any? = null
            var receivingTime: Any? = null
            var payType = 0
            var address: String? = null
            var consignee: String? = null
            var phoneNo: String? = null
            var provinceId = 0
            var cityId = 0
            var countyId = 0
            var state = 0
            var payAmount = 0
            var orderTotalAmount = 0
            var evaluationTime: Any? = null
            var cancelTime: Any? = null
            var courierAmount = 0
            var evaluationContent: String? = null
            var courierNo: String? = null
            var courierName: String? = null
            var courierCom: String? = null
            var remark: Any? = null
            var logisticsInfo: Any? = null
            var list: List<ListBean>? = null
            class ListBean:Serializable{
                var id = 0
                var orderId = 0
                var orderNumber: String? = null
                var userId = 0
                var goodsId = 0
                var goodsName: String? = null
                var orderTime: String? = null
                var payTime: String? = null
                var deliveryTime: Any? = null
                var receivingTime: Any? = null
                var payType = 0
                var state = 0
                var orderAmount = 0
                var payNumber = 0
                var payAmount = 0
                var evaluationTime: Any? = null
                var refundTime: Any? = null
                var cancelTime: Any? = null
                var goodsImg: String? = null
                var image: Any? = null
                var courierAmount = 0
                var isHandle = 0
                var isRestore = 0
            }
        }
    }
}
