package com.miyin.zhenbaoqi.ui.shop.presenter

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
        val requestBody = JSONUtils.createJSON(arrayOf("code_type"), arrayOf("goods_category"))
        val disposable = RetrofitUtils.mApiService.parentList(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap { it ->
                    if (it.code == 0) {
                        if (null == it.dicts) {
                            throw Exception(it.msg)
                        } else {
                            val bean = it.dicts!!.first { it.dict_id == cateId1 }
                            cateName1 = bean.code_name ?: ""
                            val secondRequestBody = JSONUtils.createJSON(arrayOf("parent_id"), arrayOf(cateId1))
                            return@flatMap RetrofitUtils.mApiService.sonList(secondRequestBody)
                        }
                    } else {
                        throw Exception(it.msg)
                    }
                }
                .observeOn(Schedulers.io())
                .flatMap { it ->
                    if (it.code == 0) {
                        if (null == it.dicts) {
                            throw Exception(it.msg)
                        } else {
                            val bean = it.dicts!!.first { it.dict_id == cateId2 }
                            cateName2 = bean.code_name ?: ""
                            val thirdRequestBody = JSONUtils.createJSON(arrayOf("parent_id"), arrayOf(cateId2))
                            return@flatMap RetrofitUtils.mApiService.sonList(thirdRequestBody)
                        }
                    } else {
                        throw Exception(it.msg)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<CityBean>() {
                    override fun doOnSuccess(data: CityBean) {
                        val bean = data.dicts?.first { it.dict_id == cateId3 }
                        val cateName3 = bean?.code_name ?: ""
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

    override fun getParentList(codeType: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("code_type"), arrayOf(codeType))
        request(RetrofitUtils.mApiService.parentList(requestBody), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getParentListSuccess(data.dicts!!)
            }
        })
    }

    override fun getSonList(parentId: Int, type: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("parent_id"), arrayOf(parentId))
        request(RetrofitUtils.mApiService.sonList(requestBody), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getSonListSuccess(data.dicts!!, type)
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
