package com.miyin.zhenbaoqi.utils

import android.app.Activity
import android.os.Process

import java.util.Stack

object ActivityManager {

    private val mStack = Stack<Activity>()

    fun addActivity(activity: Activity) {
        mStack.add(activity)
    }

    fun finishActivity() {
        val activity = mStack.lastElement()
        finishActivity(activity)
    }

    fun finishActivity(cls: Class<*>) {
        mStack.reversed()
                .filter { it.javaClass == cls }
                .forEach { finishActivity(it) }
    }

    fun finishActivity(activity: Activity) {
        if (!activity.isFinishing) {
            mStack.remove(activity)
            activity.finish()
        }
    }

    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            mStack.remove(activity)
        }
    }

    fun finishAllActivity() {
        val stackSize = getStackSize()
        for (i in stackSize - 1 downTo 0) {
            mStack[i].finish()
        }
        mStack.clear()
    }

    private fun getStackSize() = mStack.size

    fun exit() {
        try {
            finishAllActivity()
            System.exit(0)
            Process.killProcess(Process.myPid())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
