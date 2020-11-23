package com.miyin.zhenbaoqi.bean;

import java.util.List;

public class aaa {


    /**
     * code : 0
     * data : {"address":"string","cancelTime":"string","cityId":0,"consignee":"string","countyId":0,"courierAmount":0,"courierCom":"string","courierName":"string","courierNo":"string","deliveryTime":"string","evaluationContent":"string","evaluationTime":"string","list":[{"cancelTime":"string","courierAmount":0,"deliveryTime":"string","evaluationTime":"string","goodsId":0,"goodsImg":"string","goodsName":"string","id":0,"image":"string","isHandle":0,"isRestore":0,"orderAmount":0,"orderId":0,"orderNumber":"string","orderTime":"string","payAmount":0,"payNumber":0,"payTime":"string","payType":0,"receivingTime":"string","refundTime":"string","state":0,"userId":0}],"logisticsInfo":"string","orderId":0,"orderNumber":"string","orderTime":"string","orderTotalAmount":0,"payAmount":0,"payTime":"string","payType":0,"phoneNo":"string","provinceId":0,"receivingTime":"string","remark":"string","state":0,"userId":0}
     * msg : string
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * address : string
         * cancelTime : string
         * cityId : 0
         * consignee : string
         * countyId : 0
         * courierAmount : 0
         * courierCom : string
         * courierName : string
         * courierNo : string
         * deliveryTime : string
         * evaluationContent : string
         * evaluationTime : string
         * list : [{"cancelTime":"string","courierAmount":0,"deliveryTime":"string","evaluationTime":"string","goodsId":0,"goodsImg":"string","goodsName":"string","id":0,"image":"string","isHandle":0,"isRestore":0,"orderAmount":0,"orderId":0,"orderNumber":"string","orderTime":"string","payAmount":0,"payNumber":0,"payTime":"string","payType":0,"receivingTime":"string","refundTime":"string","state":0,"userId":0}]
         * logisticsInfo : string
         * orderId : 0
         * orderNumber : string
         * orderTime : string
         * orderTotalAmount : 0
         * payAmount : 0
         * payTime : string
         * payType : 0
         * phoneNo : string
         * provinceId : 0
         * receivingTime : string
         * remark : string
         * state : 0
         * userId : 0
         */

        private String address;
        private String cancelTime;
        private int cityId;
        private String consignee;
        private int countyId;
        private int courierAmount;
        private String courierCom;
        private String courierName;
        private String courierNo;
        private String deliveryTime;
        private String evaluationContent;
        private String evaluationTime;
        private String logisticsInfo;
        private int orderId;
        private String orderNumber;
        private String orderTime;
        private int orderTotalAmount;
        private int payAmount;
        private String payTime;
        private int payType;
        private String phoneNo;
        private int provinceId;
        private String receivingTime;
        private String remark;
        private int state;
        private int userId;
        private List<ListBean> list;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCancelTime() {
            return cancelTime;
        }

        public void setCancelTime(String cancelTime) {
            this.cancelTime = cancelTime;
        }

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public int getCountyId() {
            return countyId;
        }

        public void setCountyId(int countyId) {
            this.countyId = countyId;
        }

        public int getCourierAmount() {
            return courierAmount;
        }

        public void setCourierAmount(int courierAmount) {
            this.courierAmount = courierAmount;
        }

        public String getCourierCom() {
            return courierCom;
        }

        public void setCourierCom(String courierCom) {
            this.courierCom = courierCom;
        }

        public String getCourierName() {
            return courierName;
        }

        public void setCourierName(String courierName) {
            this.courierName = courierName;
        }

        public String getCourierNo() {
            return courierNo;
        }

        public void setCourierNo(String courierNo) {
            this.courierNo = courierNo;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public String getEvaluationContent() {
            return evaluationContent;
        }

        public void setEvaluationContent(String evaluationContent) {
            this.evaluationContent = evaluationContent;
        }

        public String getEvaluationTime() {
            return evaluationTime;
        }

        public void setEvaluationTime(String evaluationTime) {
            this.evaluationTime = evaluationTime;
        }

        public String getLogisticsInfo() {
            return logisticsInfo;
        }

        public void setLogisticsInfo(String logisticsInfo) {
            this.logisticsInfo = logisticsInfo;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public int getOrderTotalAmount() {
            return orderTotalAmount;
        }

        public void setOrderTotalAmount(int orderTotalAmount) {
            this.orderTotalAmount = orderTotalAmount;
        }

        public int getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(int payAmount) {
            this.payAmount = payAmount;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public int getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }

        public String getReceivingTime() {
            return receivingTime;
        }

        public void setReceivingTime(String receivingTime) {
            this.receivingTime = receivingTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * cancelTime : string
             * courierAmount : 0
             * deliveryTime : string
             * evaluationTime : string
             * goodsId : 0
             * goodsImg : string
             * goodsName : string
             * id : 0
             * image : string
             * isHandle : 0
             * isRestore : 0
             * orderAmount : 0
             * orderId : 0
             * orderNumber : string
             * orderTime : string
             * payAmount : 0
             * payNumber : 0
             * payTime : string
             * payType : 0
             * receivingTime : string
             * refundTime : string
             * state : 0
             * userId : 0
             */

            private String cancelTime;
            private int courierAmount;
            private String deliveryTime;
            private String evaluationTime;
            private int goodsId;
            private String goodsImg;
            private String goodsName;
            private int id;
            private String image;
            private int isHandle;
            private int isRestore;
            private int orderAmount;
            private int orderId;
            private String orderNumber;
            private String orderTime;
            private int payAmount;
            private int payNumber;
            private String payTime;
            private int payType;
            private String receivingTime;
            private String refundTime;
            private int state;
            private int userId;

            public String getCancelTime() {
                return cancelTime;
            }

            public void setCancelTime(String cancelTime) {
                this.cancelTime = cancelTime;
            }

            public int getCourierAmount() {
                return courierAmount;
            }

            public void setCourierAmount(int courierAmount) {
                this.courierAmount = courierAmount;
            }

            public String getDeliveryTime() {
                return deliveryTime;
            }

            public void setDeliveryTime(String deliveryTime) {
                this.deliveryTime = deliveryTime;
            }

            public String getEvaluationTime() {
                return evaluationTime;
            }

            public void setEvaluationTime(String evaluationTime) {
                this.evaluationTime = evaluationTime;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsImg() {
                return goodsImg;
            }

            public void setGoodsImg(String goodsImg) {
                this.goodsImg = goodsImg;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getIsHandle() {
                return isHandle;
            }

            public void setIsHandle(int isHandle) {
                this.isHandle = isHandle;
            }

            public int getIsRestore() {
                return isRestore;
            }

            public void setIsRestore(int isRestore) {
                this.isRestore = isRestore;
            }

            public int getOrderAmount() {
                return orderAmount;
            }

            public void setOrderAmount(int orderAmount) {
                this.orderAmount = orderAmount;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getOrderNumber() {
                return orderNumber;
            }

            public void setOrderNumber(String orderNumber) {
                this.orderNumber = orderNumber;
            }

            public String getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(String orderTime) {
                this.orderTime = orderTime;
            }

            public int getPayAmount() {
                return payAmount;
            }

            public void setPayAmount(int payAmount) {
                this.payAmount = payAmount;
            }

            public int getPayNumber() {
                return payNumber;
            }

            public void setPayNumber(int payNumber) {
                this.payNumber = payNumber;
            }

            public String getPayTime() {
                return payTime;
            }

            public void setPayTime(String payTime) {
                this.payTime = payTime;
            }

            public int getPayType() {
                return payType;
            }

            public void setPayType(int payType) {
                this.payType = payType;
            }

            public String getReceivingTime() {
                return receivingTime;
            }

            public void setReceivingTime(String receivingTime) {
                this.receivingTime = receivingTime;
            }

            public String getRefundTime() {
                return refundTime;
            }

            public void setRefundTime(String refundTime) {
                this.refundTime = refundTime;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
