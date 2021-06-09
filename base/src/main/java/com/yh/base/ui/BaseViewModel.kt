package com.yh.base.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.yh.base.net.bean.Rsp
import com.yh.base.net.http.Code
import com.yh.base.ui.annotation.REQUEST_LOADING_CODE
import com.yh.base.ui.ext.request
import com.yh.base.ui.ext.requestForRefresh
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

        /**
         * 成功回调，参数为Rsp类型
         */
        fun successRsp(success: ((Rsp<T>) -> Unit)?): request<T> {
            this.success1 = success
            return this
        }

        /**
         * 重复数据回调，参数为T类型（已自动判断code）
         */
        fun refresh(refresh: (suspend () -> Rsp<T>)?): request<T> {
            this.refresh = refresh
            return this
        }

        /**
         * 成功回调，参数为T类型（已自动判断code）
         */
        fun success(success: ((T?) -> Unit)?): request<T> {
            this.success = success
            return this
        }

        /**
         * 失败回调
         */
        fun error(error: ((Error) -> Unit)?): request<T> {
            this.error = error
            return this
        }

        /**
         * flag：标志控制
         * FLAG_NO_TOAST: 不展示toast（默认展示）
         * FLAG_LOADING: 展示loading（默认不展示）
         * FLAG_ERROR_LAYER: 展示toast（默认不展示）
         */
        fun flag(flag: Int): request<T> {
            this.flag = flag
            return this
        }

        /**
         * 发起网络请求（传入需要接受数据的liveData）
         */
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

        /**
         * 发起网络请求（传入需要接受数据的liveData）
         */
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
    }
}
