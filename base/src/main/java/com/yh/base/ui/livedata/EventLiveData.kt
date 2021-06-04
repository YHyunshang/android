package com.yh.base.ui.livedata

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.yh.base.constant.Config
import com.yh.base.utils.DebugUtil

open class EventLiveData<T>(unPeek: Boolean = true) : MyLiveData<T>(unPeek) {
    open fun observe(activity: FragmentActivity, success: (T) -> Unit, fail: ((Error) -> Unit)? = null) {
        if (onSuccess is UnPeekLiveData) {
            (onSuccess as UnPeekLiveData<T>).observeInActivity(activity as AppCompatActivity, Observer<T> {
                success(it)
            })
            if (fail != null) {
                (onFail as UnPeekLiveData).observeInActivity(activity) {
                    fail(it)
                }
            }
        } else {
            onSuccess.observe(activity, Observer<T> {
                success(it)
            })
            if (fail != null) {
                onFail.observe(activity, Observer {
                    fail(it)
                })
            }
        }
        if (Config.isIsDebug())
            DebugUtil.checkInMethod(activity.javaClass, "lifecycleObserver")
    }

    open fun observe(fragment: Fragment, success: (T) -> Unit, fail: ((Error) -> Unit)? = null) {
        if (onSuccess is UnPeekLiveData) {
            (onSuccess as UnPeekLiveData<T>).observeInFragment(fragment) {
                success(it)
            }
            if (fail != null) {
                (onFail as UnPeekLiveData).observeInFragment(fragment) {
                    fail(it)
                }
            }
        } else {
            onSuccess.observe(fragment, Observer<T> {
                success(it)
            })
            if (fail != null) {
                onFail.observe(fragment, Observer {
                    fail(it)
                })
            }
        }
        if (Config.isIsDebug())
            DebugUtil.checkInMethod(fragment.javaClass, "lifecycleObserver")
    }
}

data class Error(var code: Int, var msg: String) {
    companion object {
        const val INTERNET: Int = -1
        const val SERVER: Int = -2

        //        const val SERVER_DATA: Int = -3
        const val UNKNOWN: Int = -10001
    }
}

data class RequestData(val flag: Int, val error: Error? = null, val retry: (() -> Unit)? = null) {
    fun hasFlag(flag: Int): Boolean {
        return this.flag and flag != 0
    }
}