package com.yh.base.ui.ext

/**
 * 过滤服务器结果，失败抛异常
 * @param block 请求体方法，必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不传
 * @param isShowDialog 是否显示加载框 可不传
 */
//private fun <T> BaseViewModel.requestForRefresh(
//        @REQUEST_LOADING_CODE flag: Int = 0,
//        blockRequest: suspend () -> Rsp<T>,
//        blockRefresh: suspend () -> Rsp<T>,
//        success: (rsp: Rsp<T>, isRefresh: Boolean) -> Unit,
//        error: ((code: Int, msg: String, isRefresh: Boolean) -> Unit)? = null
//) {
//    viewModelScope.launch {
//        runCatching {
//            requestLiveData.value = RequestData(flag or LoadingCode.DEFAULT_FLAG)
//            blockRequest()
//        }.onSuccess { it1 ->
//            if (it1.code == Code.SUCCESS) {
//                requestLiveData.value = RequestData(flag)
//                if (it1.cache == 2) {
//                    success?.invoke(it1, true)
//                    it1.cache = 0
//                    refreshRequest(flag, blockRefresh, success, error, it1)
//                }
//            } else {
//                requestLiveData.value = RequestData(flag, Error(it1.code, it1.message
//                        ?: "错误：" + it1.code)) {
//                    requestForRefresh(flag, blockRequest, blockRefresh, success, error)
//                }
//                refreshRequest(flag, blockRefresh, success, error, null)
//            }
//        }.onFailure {
//            parseError(it).run {
//                requestLiveData.value = RequestData(flag, this) {
//                    requestForRefresh(flag, blockRequest, blockRefresh, success, error)
//                }
//                error?.invoke(this.code, this.msg, false)
//            }
//        }
//    }
//}
//
//private fun parseError(it: Throwable): Error {
//    var code = if ((it is UnknownHostException)
//            or (it is SocketTimeoutException)
//            or (it is ConnectException)
//            or (it is IOException)
//            or (it is SSLHandshakeException)
//    ) {
//        Error.INTERNET
//    } else if ((it is SocketException)
//            or (it is HttpException)
//            or (it is JsonSyntaxException)
//            or (it is NullPointerException)
//    ) {
//        Error.SERVER
//    } else {
//        Error.UNKNOWN
//    }
//    return Error(code, it.message ?: it.toString())
//}
//
//private fun <T> BaseViewModel.refreshRequest(
//        @REQUEST_LOADING_CODE flag: Int = 0,
//        blockRefresh: suspend () -> Rsp<T>,
//        success: (rsp: Rsp<T>, isRefresh: Boolean) -> Unit,
//        error: ((code: Int, msg: String, isRefresh: Boolean) -> Unit)? = null,
//        cacheEntry: Rsp<T>?
//) {
//    viewModelScope.launch {
//        runCatching {
//            blockRefresh()
//        }.onSuccess {
//            if (it.code == Code.SUCCESS) {
//                it.cache = 0
//                val result1 = GsonUtil.Gson().toJson(cacheEntry)
//                val result2 = GsonUtil.Gson().toJson(it)
//                if (!TextUtils.equals(result1, result2)) {
//                    success(it, true)
//                }
//            } else {
//                error?.invoke(it.code, it.message ?: "错误：" + it.code, false)
//            }
//        }.onFailure {
//            parseError(it).run {
//                error?.invoke(this.code, this.msg, false)
//            }
//        }
//    }
//}


//fun parseError(it: Throwable): Error {
//    var code = if ((it is UnknownHostException)
//            or (it is SocketTimeoutException)
//            or (it is ConnectException)
//            or (it is IOException)
//            or (it is SSLHandshakeException)
//    ) {
//        Error.INTERNET
//    } else if ((it is SocketException)
//            or (it is HttpException)
//            or (it is JsonSyntaxException)
//            or (it is NullPointerException)
//    ) {
//        Error.SERVER
//    } else {
//        Error.UNKNOWN
//    }
//    return Error(code, it.message ?: it.toString())
//}

//fun <T> BaseViewModel.requestForRefresh(
//        flag: Int = 0,
//        blockRequest: suspend () -> Rsp<T>,
//        blockRefresh: suspend () -> Rsp<T>,
//        result: EventLiveData<Rsp<T>>
//) {
//    requestForRefresh(flag, {
//        blockRequest()
//    }, {
//        blockRefresh()
//    }, { rsp, _ ->
//        result.value = rsp
//    }, { code, msg, _ ->
//        result.postValue(Error(code, msg, ResetRequest()))
//    })
//}

/**
 * 过滤服务器结果，失败抛异常
 * @param block 请求体方法，必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不传
 * @param isShowDialog 是否显示加载框 可不传
 */
//fun <T> BaseViewModel.request(
//        @REQUEST_LOADING_CODE flag: Int = 0,
//        block: suspend () -> Rsp<T>,
//        success: ((Rsp<T>) -> Unit)? = null,
//        error: ((Error) -> Unit)? = null
//): Job {
//    return viewModelScope.launch {
//        runCatching {
//            requestLiveData.value = RequestData(flag or LoadingCode.DEFAULT_FLAG)
//            block()
//        }.onSuccess {
//            if (it.code == Code.SUCCESS) {
//                requestLiveData.value = RequestData(flag)
//                success?.invoke(it)
//            } else {
//                requestLiveData.value = RequestData(flag, Error(it.code, it.message
//                        ?: "错误：" + it.code)) {
//                    request(flag, block, success, error)
//                }
//            }
//
//        }.onFailure {
//            parseError(it).run {
//                requestLiveData.value = RequestData(flag, this) {
//                    request(flag, block, success, error)
//                }
//                error?.invoke(this)
//            }
//        }
//    }
//}


/**
 * 过滤服务器结果，失败抛异常
 * @param block 请求体方法，必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不传
 * @param isShowDialog 是否显示加载框 可不传
 */
//fun <T> BaseViewModel.request(
//        block: suspend () -> Rsp<T>,
//        success: ((Rsp<T>) -> Unit)? = null,
//        error: ((Error) -> Unit)? = null,
//        @REQUEST_LOADING_CODE flag: Int = 0
//): Job {
//    return request(block, success, error, null, flag)
//}

//fun <T> BaseViewModel.request(
//        block: suspend () -> Rsp<T>,
//        @REQUEST_LOADING_CODE flag: Int = 0
//): Job {
//    return request(block, { }, null, null, flag)
//}

//fun <T> BaseViewModel.request(
//        block: suspend () -> Rsp<T>,
//        errorLayer: ErrorLayer? = null,
//        @REQUEST_LOADING_CODE flag: Int = 0
//): Job {
//    return request(block, { }, null, errorLayer, flag)
//}

//fun <T> BaseViewModel.request(
//        block: suspend () -> Rsp<T>,
//        success: ((Rsp<T>) -> Unit)? = null,
//        @REQUEST_LOADING_CODE flag: Int = 0
//): Job {
//    return request(block, success, null, null, flag)
//}

//fun <T> BaseViewModel.request(
//        block: suspend () -> Rsp<T>,
//        result: EventLiveData<Rsp<T>>,
//        @REQUEST_LOADING_CODE flag: Int = 0
//): Job {
//    return request(block, result, null, flag)
//}
//
//fun <T> BaseViewModel.request(
//        @REQUEST_LOADING_CODE flag: Int = 0,
//        block: suspend () -> Rsp<T>,
//        result: EventLiveData<Rsp<T>>,
//        errorLayer: ErrorLayer? = null
//): Job {
//    return request(flag,
//            { block() },
//            { result.value = it },
//            { result.postValue(it) },
//            errorLayer)
//}

//fun <T> BaseViewModel.request(
//        block: suspend () -> Rsp<T>,
//        success: ((Rsp<T>) -> Unit)? = null,
//        errorLayer: ErrorLayer? = null,
//        @REQUEST_LOADING_CODE flag: Int = 0
//): Job {
//    return request(block, success, null, errorLayer, flag)
//}

