package com.miyin.zhenbaoqi.bean;

import java.util.List;

public class aaa {


    /**
     * data : [{"dictId":200,"codeType":null,"parentId":null,"codeName":"北京市","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":202,"codeType":null,"parentId":null,"codeName":"重庆市","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":203,"codeType":null,"parentId":null,"codeName":"天津市","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":204,"codeType":null,"parentId":null,"codeName":"上海市","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":205,"codeType":null,"parentId":null,"codeName":"河北省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":206,"codeType":null,"parentId":null,"codeName":"山西省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":208,"codeType":null,"parentId":null,"codeName":"辽宁省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":209,"codeType":null,"parentId":null,"codeName":"吉林省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":210,"codeType":null,"parentId":null,"codeName":"黑龙江省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":211,"codeType":null,"parentId":null,"codeName":"江苏省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":212,"codeType":null,"parentId":null,"codeName":"浙江省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":213,"codeType":null,"parentId":null,"codeName":"安徽省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":214,"codeType":null,"parentId":null,"codeName":"福建省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":215,"codeType":null,"parentId":null,"codeName":"江西省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":216,"codeType":null,"parentId":null,"codeName":"山东省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":217,"codeType":null,"parentId":null,"codeName":"河南省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":218,"codeType":null,"parentId":null,"codeName":"湖北省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":219,"codeType":null,"parentId":null,"codeName":"湖南省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":220,"codeType":null,"parentId":null,"codeName":"广东省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":221,"codeType":null,"parentId":null,"codeName":"甘肃省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":222,"codeType":null,"parentId":null,"codeName":"四川省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":223,"codeType":null,"parentId":null,"codeName":"贵州省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":224,"codeType":null,"parentId":null,"codeName":"海南省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":225,"codeType":null,"parentId":null,"codeName":"云南省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":226,"codeType":null,"parentId":null,"codeName":"青海省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":227,"codeType":null,"parentId":null,"codeName":"陕西省","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":228,"codeType":null,"parentId":null,"codeName":"广西壮族自治区","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":229,"codeType":null,"parentId":null,"codeName":"西藏自治区","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":230,"codeType":null,"parentId":null,"codeName":"宁夏回族自治区","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":231,"codeType":null,"parentId":null,"codeName":"新疆维吾尔自治区","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null},{"dictId":232,"codeType":null,"parentId":null,"codeName":"内蒙古自治区","dictSort":null,"status":null,"codeValue":"","remark":null,"extend":null}]
     * msg : 成功
     * code : 200
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * dictId : 200
         * codeType : null
         * parentId : null
         * codeName : 北京市
         * dictSort : null
         * status : null
         * codeValue :
         * remark : null
         * extend : null
         */

        private int dictId;
        private Object codeType;
        private Object parentId;
        private String codeName;
        private Object dictSort;
        private Object status;
        private String codeValue;
        private Object remark;
        private Object extend;

        public int getDictId() {
            return dictId;
        }

        public void setDictId(int dictId) {
            this.dictId = dictId;
        }

        public Object getCodeType() {
            return codeType;
        }

        public void setCodeType(Object codeType) {
            this.codeType = codeType;
        }

        public Object getParentId() {
            return parentId;
        }

        public void setParentId(Object parentId) {
            this.parentId = parentId;
        }

        public String getCodeName() {
            return codeName;
        }

        public void setCodeName(String codeName) {
            this.codeName = codeName;
        }

        public Object getDictSort() {
            return dictSort;
        }

        public void setDictSort(Object dictSort) {
            this.dictSort = dictSort;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public Object getExtend() {
            return extend;
        }

        public void setExtend(Object extend) {
            this.extend = extend;
        }
    }
}
