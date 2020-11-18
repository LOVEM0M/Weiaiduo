package  com.miyin.zhenbaoqi.bean

import java.io.Serializable

class SeedingBean : PagerBean() {

    var data : DataBeanX? = null
    class DataBeanX :  Serializable{
        var total = 0
        var data : List<DataBean>? = null
        class DataBean{
            var  id = 0
            var  images: String? = null
            var  title: String? = null
            var  createTime: String? = null
            var  goodsId = 0
            var  goodsName: String? = null
            var  state = 0
            var  content: String? = null

        }
    }
}