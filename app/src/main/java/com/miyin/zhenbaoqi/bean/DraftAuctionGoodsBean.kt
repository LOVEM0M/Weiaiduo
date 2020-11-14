package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class DraftAuctionGoodsBean : Serializable {

    var goodsName: String? = null
    var goodsDesc: String? = null
    var cate1Id = 0
    var cate2Id = 0
    var cate3Id = 0
    var cate1Name: String? = null
    var cate2Name: String? = null
    var cate3Name: String? = null
    var goodsImg: String? = null
    var goodsVideo: String? = null
    var initPrice = 0L
    var addPrice = 0L
    var referencePrice = 0L
    var goodsFreight = 0
    var isSeven = 0
    var ensurePrice = 0L
    var size: String? = null
    var material: String? = null
    var originPlace: String? = null
    var weight: String? = null

}