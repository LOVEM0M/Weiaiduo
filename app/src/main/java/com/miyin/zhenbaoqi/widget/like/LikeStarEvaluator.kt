package com.miyin.zhenbaoqi.widget.like

import android.animation.TypeEvaluator
import android.graphics.Point

class LikeStarEvaluator(private val mControllerPoint: Point) : TypeEvaluator<Point> {

    override fun evaluate(t: Float, startValue: Point, endValue: Point): Point {
        val x = ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * mControllerPoint.x + t * t * endValue.x).toInt()
        val y = ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * mControllerPoint.y + t * t * endValue.y).toInt()
        return Point(x, y)
    }

}
