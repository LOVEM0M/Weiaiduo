package com.miyin.zhenbaoqi.bean

class OrderDetailBean : ResponseBean() {

    var data  : DataBean ? = null
    class DataBean{
        var address: String? = null
        var cancelTime: String? = null
        var cityId = 0
        var consignee: String? = null
        var countyId = 0
        var courierAmount = 0
        var courierCom: String? = null
        var courierName: String? = null
        var courierNo: String? = null
        var deliveryTime: String? = null
        var evaluationContent: String? = null
        var evaluationTime: String? = null
        var logisticsInfo: String? = null
        var orderId = 0
        var orderNumber: String? = null
        var orderTime: String? = null
        var orderTotalAmount = 0
        var payAmount = 0
        var payTime: String? = null
        var payType = 0
        var phoneNo: String? = null
        var provinceId = 0
        var receivingTime: String? = null
        var remark: String? = null
        var state = 0
        var userId = 0
        var list: List<ListBean>? = null
        class ListBean{
            var cancelTime: String? = null
            var courierAmount = 0
            var deliveryTime: String? = null
            var evaluationTime: String? = null
            var goodsId = 0
            var goodsImg: String? = null
            var goodsName: String? = null
            var id = 0
            var image: String? = null
            var isHandle = 0
            var isRestore = 0
            var orderAmount = 0
            var orderId = 0
            var orderNumber: String? = null
            var orderTime: String? = null
            var payAmount = 0
            var payNumber = 0
            var payTime: String? = null
            var payType = 0
            var receivingTime: String? = null
            var refundTime: String? = null
            var state = 0
            var userId = 0
        }
    }

}
