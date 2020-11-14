package com.miyin.zhenbaoqi.ext

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.Transformation
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.App

fun BaseViewHolder.setSelect(@IdRes viewId: Int, selected: Boolean): BaseViewHolder {
    getView<View>(viewId).isSelected = selected
    return this
}

fun BaseViewHolder.loadImg(@IdRes viewId: Int, path: Any?): BaseViewHolder {
    getView<ImageView>(viewId).loadImg(path)
    return this
}

fun BaseViewHolder.placeholder(@IdRes viewId: Int, path: Any?, @DrawableRes drawableRes: Int): BaseViewHolder {
    getView<ImageView>(viewId).placeholder(path, drawableRes)
    return this
}


fun BaseViewHolder.transform(@IdRes viewId: Int, path: Any?, transformation: Transformation<Bitmap>): BaseViewHolder {
    getView<ImageView>(viewId).transform(path, transformation)
    return this
}

fun BaseViewHolder.setBackground(@IdRes viewId: Int, drawable: Drawable): BaseViewHolder {
    getView<View>(viewId).background = drawable
    return this
}

fun BaseViewHolder.setRightDrawable(@IdRes viewId: Int, @DrawableRes drawableRes: Int): BaseViewHolder {
    val rightDrawable = ContextCompat.getDrawable(App.context, drawableRes)
    getView<TextView>(viewId).setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null)
    return this
}
