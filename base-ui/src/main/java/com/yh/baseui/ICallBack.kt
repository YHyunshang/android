package com.yh.baseui

/**
 * @description $
 * @date: 2021/4/21 5:09 PM
 * @author: zengbobo
 */
interface ICallBack<T> {
    fun onCall(data: T): Boolean
}