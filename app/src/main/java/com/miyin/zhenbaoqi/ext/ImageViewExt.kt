package com.miyin.zhenbaoqi.ext

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.load.Transformation
import com.miyin.zhenbaoqi.utils.GlideApp

fun ImageView.loadImg(path: Any?) {
    GlideApp.with(context).load(path).into(this)
}

fun ImageView.placeholder(path: Any?, @DrawableRes drawableRes: Int) {
    GlideApp.with(context).load(path)
            .placeholder(drawableRes)
            .error(drawableRes)
            .into(this)
}

fun ImageView.transform(path: Any?, transformation: Transformation<Bitmap>) {
    GlideApp.with(context).load(path)
            .transform(transformation)
            .into(this)
}

fun ImageView.loadImgAll(path: Any?, @DrawableRes drawableRes: Int, transformation: Transformation<Bitmap>) {
    GlideApp.with(context).load(path)
            .placeholder(drawableRes)
            .error(drawableRes)
            .transform(transformation)
            .into(this)
}
