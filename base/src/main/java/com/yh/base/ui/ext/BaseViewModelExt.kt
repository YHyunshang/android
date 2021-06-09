package com.yh.base.ui.ext

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.yh.base.net.bean.Rsp
import com.yh.base.net.http.Code
import com.yh.base.ui.BaseViewModel
import com.yh.base.ui.annotation.REQUEST_LOADING_CODE
import com.yh.base.ui.livedata.Error
import com.yh.base.ui.livedata.RequestData
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException


/**
 * 过滤服务器结果，失败抛异常
 * @param block 请求体方法，必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不传
 * @param isShowDialog 是否显示加载框 可不传
 */
fun <T> BaseViewModel.request(
        @REQUEST_LOADING_CODE flag: Int = 0,
        block: suspend () -> Rsp<T>,
        success: ((Rsp<T>) -> Unit)? = null,
        error: ((Error) -> Unit)? = null
): Job {
    return viewModelScope.launch {
        runCatching {
            requestLiveData.value = RequestData(flag or BaseViewModel.FLAG_DEFAULT)
            block()
        }.onSuccess {
            if (it.code == Code.SUCCESS) {
                requestLiveData.value = RequestData(flag)
                success?.invoke(it)
            } else {
                var er = Error(it.code, it.message
                        ?: "错误：" + it.code)
                requestLiveData.value = RequestData(flag, er) {
                    request(flag, block, success, error)
                }
                error?.invoke(er)
            }

        }.onFailure {
            if (this.isActive) {
                parseError(it).run {
                    requestLiveData.value = RequestData(flag, this) {
                        request(flag, block, success, error)
                    }
                    error?.invoke(this)
                }
            }
        }
    }
}

/**
 * 过滤服务器结果，失败抛异常
 * @param block 请求体方法，必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不传
 * @param isShowDialog 是否显示加载框 可不传
 */
fun <T> BaseViewModel.requestForRefresh(
        @REQUEST_LOADING_CODE flag: Int = 0,
        blockRequest: suspend () -> Rsp<T>,
        blockRefresh: suspend () -> Rsp<T>,
        success: (rsp: Rsp<T>, isRefresh: Boolean) -> Unit,
        error: ((code: Int, msg: String, isRefresh: Boolean) -> Unit)? = null
) {
    viewModelScope.launch {
        runCatching {
            requestLiveData.value = RequestData(flag or BaseViewModel.FLAG_DEFAULT)
            blockRequest()
        }.onSuccess { it1 ->
            if (it1.code == Code.SUCCESS) {
                requestLiveData.value = RequestData(flag)
                success.invoke(it1, true)
                if (it1.cache == 2) {
                    refreshRequest(flag, blockRefresh, success, error)
                }
            } else {
                val er = Error(it1.code, it1.message
                        ?: "错误：" + it1.code)
                requestLiveData.value = RequestData(flag, er) {
                    requestForRefresh(flag, blockRequest, blockRefresh, success, error)
                }
                error?.invoke(er.code, er.msg, false)
            }
        }.onFailure {
            if (this.isActive) {
                parseError(it).run {
                    requestLiveData.value = RequestData(flag, this) {
                        requestForRefresh(flag, blockRequest, blockRefresh, success, error)
                    }
                    error?.invoke(this.code, this.msg, false)
                }
            }
        }
    }
}

private fun parseError(it: Throwable): Error {
    var code = if ((it is UnknownHostException)
            or (it is SocketTimeoutException)
            or (it is ConnectException)
            or (it is IOException)
            or (it is SSLHandshakeException)
    ) {
        Error.INTERNET
    } else if ((it is SocketException)
            or (it is HttpException)
            or (it is JsonSyntaxException)
            or (it is NullPointerException)
    ) {
        Error.SERVER
    } else {
        Error.UNKNOWN
    }
    return Error(code, it.message ?: it.toString())
}

fun <T> BaseViewModel.refreshRequest(
        @REQUEST_LOADING_CODE flag: Int = 0,
        blockRefresh: suspend () -> Rsp<T>,
        success: (rsp: Rsp<T>, isRefresh: Boolean) -> Unit,
        error: ((code: Int, msg: String, isRefresh: Boolean) -> Unit)? = null
) {
    viewModelScope.launch {
        runCatching {
            withContext(Dispatchers.IO) {
                blockRefresh()
            }
        }.onSuccess {
            if (it.code == Code.SUCCESS) {
                success(it, true)
            } else {
                error?.invoke(it.code, it.message ?: "错误：" + it.code, true)
            }
        }.onFailure {
            if (this.isActive) {
                parseError(it).run {
                    error?.invoke(this.code, this.msg, true)
                }
            }
        }
    }
}

