package com.yh.base.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.yh.base.net.bean.Rsp
import com.yh.base.net.http.Code
import com.yh.base.ui.annotation.REQUEST_LOADING_CODE
import com.yh.base.ui.livedata.Error
import com.yh.base.ui.livedata.MyLiveData
import com.yh.base.ui.livedata.RequestData
import com.yh.base.utils.Util
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * @description 基础ViewModel类
 * @date: 2021/4/6 4:37 PM
 * @author: guowanxin
 */
open class BaseViewModel : ViewModel() {

    companion object {
        const val FLAG_DEFAULT: Int = 0x1 shl 31
        const val FLAG_NO_TOAST: Int = 0x1
        const val FLAG_LOADING: Int = 0x1 shl 1
        const val FLAG_ERROR_LAYER: Int = 0x1 shl 2
    }

    private val mApplication: Application by lazy {
        Util.getApplication()
    }

    /**
     * Return the application.
     */
    open fun getApplication(): Application {
        return mApplication
    }

    /**
     * 内置封装好的可通知Activity/fragment 显示隐藏加载框
     */

    val requestLiveData: MutableLiveData<RequestData> by lazy { MutableLiveData() }

    inner class request<T>(private val block: (suspend () -> Rsp<T>), private var refresh: (suspend () -> Rsp<T>)?) {
        constructor(block: (suspend () -> Rsp<T>)) : this(block, null)

        private var success: ((T?) -> Unit)? = null
        private var success1: ((Rsp<T>) -> Unit)? = null
        private var error: ((Error) -> Unit)? = null
        private var flag: Int = 0

        fun successRsp(success: ((Rsp<T>) -> Unit)?): request<T> {
            this.success1 = success
            return this
        }

        fun refresh(refresh: (suspend () -> Rsp<T>)?): request<T> {
            this.refresh = refresh
            return this
        }

        fun success(success: ((T?) -> Unit)?): request<T> {
            this.success = success
            return this
        }

        fun error(error: ((Error) -> Unit)?): request<T> {
            this.error = error
            return this
        }

        fun flag(flag: Int): request<T> {
            this.flag = flag
            return this
        }

        fun run(liveData: MyLiveData<T?>? = null) {
            if (refresh != null)
                requestForRefresh(flag, block, refresh!!, { rsp: Rsp<T>, b: Boolean ->
                    liveData?.value = rsp.result
                    success?.invoke(rsp.result)
                    success1?.invoke(rsp)
                }, { i: Int, s: String, b: Boolean ->
                    val err = Error(i, s)
                    liveData?.postValue(err)
                    error?.invoke(err)
                })
            else
                request(flag, block, {
                    liveData?.value = it.result
                    success?.invoke(it.result)
                    success1?.invoke(it)
                }, {
                    liveData?.postValue(it)
                    error?.invoke(it)
                })
        }

        @JvmName("rsp")
        fun run(liveData: MyLiveData<Rsp<T>>?) {
            if (refresh != null)
                requestForRefresh(flag, block, refresh!!, { rsp: Rsp<T>, b: Boolean ->
                    liveData?.value = rsp
                    success?.invoke(rsp.result)
                    success1?.invoke(rsp)
                }, { i: Int, s: String, b: Boolean ->
                    if (!b) {
                        val err = Error(i, s)
                        liveData?.postValue(err)
                        error?.invoke(err)
                    }
                })
            else
                request(flag, block, {
                    liveData?.value = it
                    success?.invoke(it.result)
                    success1?.invoke(it)
                }, {
                    liveData?.postValue(it)
                    error?.invoke(it)
                })
        }

        /**
         * 过滤服务器结果，失败抛异常
         * @param block 请求体方法，必须要用suspend关键字修饰
         * @param success 成功回调
         * @param error 失败回调 可不传
         * @param isShowDialog 是否显示加载框 可不传
         */
        private fun <T> request(
                @REQUEST_LOADING_CODE flag: Int = 0,
                block: suspend () -> Rsp<T>,
                success: ((Rsp<T>) -> Unit)? = null,
                error: ((Error) -> Unit)? = null
        ): Job {
            return viewModelScope.launch {
                runCatching {
                    requestLiveData.value = RequestData(flag or FLAG_DEFAULT)
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
        private fun <T> requestForRefresh(
                @REQUEST_LOADING_CODE flag: Int = 0,
                blockRequest: suspend () -> Rsp<T>,
                blockRefresh: suspend () -> Rsp<T>,
                success: (rsp: Rsp<T>, isRefresh: Boolean) -> Unit,
                error: ((code: Int, msg: String, isRefresh: Boolean) -> Unit)? = null
        ) {
            viewModelScope.launch {
                runCatching {
                    requestLiveData.value = RequestData(flag or FLAG_DEFAULT)
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

        private fun <T> refreshRequest(
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
    }
}
