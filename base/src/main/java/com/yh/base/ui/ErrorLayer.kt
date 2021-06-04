package com.yh.base.ui

abstract class ErrorLayer {
    abstract fun show(code: Int, msg: String, retry: () -> Unit)
    abstract fun hide()
}