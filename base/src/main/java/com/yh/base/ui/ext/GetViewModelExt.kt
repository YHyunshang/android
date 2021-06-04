package com.yh.base.ui.ext

import com.yh.base.ui.BaseViewModel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @description 获取当前类绑定的泛型ViewModel-clazz
 * @date: 2021/4/6 5:19 PM
 * @author: guowanxin
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    val type: Type? = obj.javaClass.genericSuperclass
    return if (type is ParameterizedType) {
        (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
    } else {
        //如果没有指定泛型参数，则默认使用BaseViewModel
        BaseViewModel::class.java as VM
    }
}

@Suppress("UNCHECKED_CAST")
fun <VB> getVbClazz(obj: Any): VB? {
    val type: Type? = obj.javaClass.genericSuperclass
    return if (type is ParameterizedType) {
        (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VB
    } else null
}

@Suppress("UNCHECKED_CAST")
fun <VB> getDialogVbClazz(obj: Any): VB? {
    val type: Type? = obj.javaClass.genericSuperclass
    return if (type is ParameterizedType) {
        (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VB
    } else null
}