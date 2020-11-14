package com.miyin.zhenbaoqi.base.mvp

import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.ref.WeakReference
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V : IBaseView> : IBasePresenter<V> {

    private var mWeakReferenceView: WeakReference<V>? = null
    private var mProxyView: V? = null
    protected var mDisposable: CompositeDisposable? = null

    init {
        mDisposable = CompositeDisposable()
    }

    override fun attachView(view: V) {
        mWeakReferenceView = WeakReference(view)
        val viewHandler = MvpViewHandler(mWeakReferenceView!!.get()!!)
        mProxyView = Proxy.newProxyInstance(view.javaClass.classLoader, view.javaClass.interfaces, viewHandler) as V
    }

    override fun detachView() {
        if (null != mDisposable) {
            mDisposable!!.clear()
            mDisposable = null
        }
        if (isViewAttached()) {
            mWeakReferenceView!!.clear()
            mWeakReferenceView = null
        }
    }

    private fun isViewAttached(): Boolean {
        return mWeakReferenceView != null && mWeakReferenceView!!.get() != null
    }

    protected fun getView(): V? {
        return mProxyView
    }

    protected fun <T : ResponseBean> request(observable: Single<T>, observer: BaseSingleObserver<T>) {
        val disposable = observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
        mDisposable?.add(disposable)
    }

    protected fun uploadImg(path: String, key: String = "photo_file", type: String = "image/*"): List<MultipartBody.Part> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        val file = File(path)
        val requestBody = RequestBody.create(MediaType.parse(type), file)
        builder.addFormDataPart(key, file.name, requestBody)

        return builder.build().parts()
    }

    protected fun getDisposable(): CompositeDisposable? = mDisposable

    private inner class MvpViewHandler internal constructor(private val mvpView: IBaseView) : InvocationHandler {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any? {
            return if (isViewAttached()) { // 如果 V 层没被销毁, 执行 V 层的方法.
                method.invoke(mvpView, *args.orEmpty())
            } else { // P 层不需要关注 V 层的返回值
                null
            }
        }
    }
}
