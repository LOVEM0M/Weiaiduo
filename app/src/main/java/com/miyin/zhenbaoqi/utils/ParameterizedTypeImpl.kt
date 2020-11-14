package com.miyin.zhenbaoqi.utils

import java.lang.reflect.ParameterizedType

class ParameterizedTypeImpl(private val clazz: Class<*>) : ParameterizedType {

    override fun getRawType() = List::class.java

    override fun getOwnerType() = null

    override fun getActualTypeArguments() = arrayOf(clazz)

}
