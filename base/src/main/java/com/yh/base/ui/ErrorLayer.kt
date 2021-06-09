package com.yh.base.ui

/**
 * @description 网络覆盖层基类定义
 *
 * @date: 2021/4/6 4:11 PM
 * @author: zhangzhiyuan
 */
abstract class ErrorLayer {
    abstract fun show(code: Int, msg: String, retry: () -> Unit)
    abstract fun hide()
}