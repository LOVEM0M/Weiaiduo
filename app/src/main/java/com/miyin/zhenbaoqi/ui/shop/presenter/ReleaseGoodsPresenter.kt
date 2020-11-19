package com.miyin.zhenbaoqi.ui.shop.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.ImageBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.ReleaseGoodsContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ProtocolException

class ReleaseGoodsPresenter : BasePresenter<ReleaseGoodsContract.IView>(), ReleaseGoodsContract.IPresenter {

    override fun getCategoryName(cateId1: Int, cateId2: Int, cateId3: Int) {
        var cateName1 = ""
        var cateName2 = ""
        val map = ArrayMap<String, Any>().apply {
            put("type", 0)
        }
        val disposable = RetrofitUtils.mApiService.parentList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap { it ->
                    if (it.code == 0) {
                        if (null == it.data) {
                            throw Exception(it.msg)
                        } else {
                            val bean = it.data!!.first { it.dictId == cateId1 }
                            cateName1 = bean.codeName ?: ""
                            val map = ArrayMap<String, Any>().apply {
                                put("parentId", cateId1)
                            }
                            return@flatMap RetrofitUtils.mApiService.sonList(map)
                        }
                    } else {
                        throw Exception(it.msg)
                    }
                }
                .observeOn(Schedulers.io())
                .flatMap { it ->
                    if (it.code == 0) {
                        if (null == it.data) {
                            throw Exception(it.msg)
                        } else {
                            val bean = it.data!!.first { it.dictId == cateId2 }
                            cateName2 = bean.codeName ?: ""
                            val map = ArrayMap<String, Any>().apply {
                                put("parentId", cateId2)
                            }
                            return@flatMap RetrofitUtils.mApiService.sonList(map)
                        }
                    } else {
                        throw Exception(it.msg)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<CityBean>() {
                    override fun doOnSuccess(data: CityBean) {
                        val bean = data.data?.first { it.dictId == cateId3 }
                        val cateName3 = bean?.codeName ?: ""
                        Logger.d("name1 == $cateName1, name2 == $cateName2, name3 == $cateName3")
                        getView()?.getCategoryNameSuccess(cateName1, cateName2, cateName3)
                    }
                })
        getDisposable()?.add(disposable)
    }

    override fun getMerchantMainCateState() {
        request(RetrofitUtils.mApiService.merchantCate(), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {

            }

            override fun doOnFailure(data: ResponseBean) {
                if (data.code == 1 && data.msg == "未填写") {
                    getView()?.onFailure(data.msg ?: "", 1)
                } else {
                    super.doOnFailure(data)
                }
            }
        })
    }

    override fun getParentList(type: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("type", type)
        }
        request(RetrofitUtils.mApiService.parentList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getParentListSuccess(data.data!!)
            }
        })
    }

    override fun getSonList(parentId: Int, type: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("parentId", parentId)
        }
        request(RetrofitUtils.mApiService.sonList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getSonListSuccess(data.data!!, type)
            }
        })
    }

    override fun uploadGoodsImg(path: String) {
        val list = uploadImg(path)
        request(RetrofitUtils.mApiService.uploadMerchantGoodsImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.uploadGoodsImgSuccess(data.photo_url!!)
            }

            override fun doOnFailure(data: ImageBean) {
                getView()?.onFailure(data.msg!!, 0)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.hideDialog()
            }
        })
    }

    override fun uploadGoodsVideo(path: String) {
        getView()?.showDialog("正在上传视频...", false)
        val list = uploadImg(path, type = "video/*")
        request(RetrofitUtils.mApiService.uploadVideo(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.hideDialog()
                getView()?.uploadGoodsVideoSuccess(data.photo_url!!)
            }

            override fun doOnFailure(data: ImageBean) {
                super.doOnFailure(data)
                getView()?.hideDialog()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.hideDialog()
                if (e is ProtocolException) {
                    Logger.d("onError == ${e.message}")
                }
            }
        })
    }

    override fun updateMerchantMainCateState(mainCate: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("main_cate"), arrayOf(mainCate))
        request(RetrofitUtils.mApiService.updateMerchantCate(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateMerchantMainCateStateSuccess()
            }
        })
    }

}
