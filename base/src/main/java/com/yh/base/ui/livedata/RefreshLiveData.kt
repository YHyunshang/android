package com.yh.base.ui.livedata

import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.yh.base.constant.Config
import com.yh.base.utils.DebugUtil
import com.yh.base.utils.GsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @description 过滤刷新型livedata封装（过滤重复数据，防止多余刷新）
 *    同时支持succes refresh 和fail回调
 *    可支持粘性和非粘性 unPeek：true：粘性 false：非粘性
 *
 * @date: 2021/4/6 4:11 PM
 * @author: zhangzhiyuan
 */
open class RefreshLiveData<T>(unPeek: Boolean = true) : MyLiveData<T>(unPeek) {
    private val onRefresh: LiveData<T> = if (unPeek) {
        UnPeekLiveData.Builder<T>().setAllowNullValue(true).create()
    } else {
        MutableLiveData()
    }

    open fun observe(activity: FragmentActivity, success: (T) -> Unit, refresh: ((T) -> Unit)? = null, fail: ((Error) -> Unit)? = null) {
        if (onSuccess is UnPeekLiveData) {
            (onSuccess as UnPeekLiveData<T>).observeInActivity(activity as AppCompatActivity, Observer<T> {
                success(it)
            })
            if (refresh != null) {
                (onRefresh as UnPeekLiveData<T>).observeInActivity(activity, Observer<T> {
                    refresh(it)
                })
            }
            if (fail != null) {
                (onFail as UnPeekLiveData).observeInActivity(activity) {
                    fail(it)
                }
            }
        } else {
            onSuccess.observe(activity, Observer<T> {
                success(it)
            })
            if (refresh != null) {
                onRefresh.observe(activity, Observer<T> {
                    refresh(it)
                })
            }
            if (fail != null) {
                (onFail as MutableLiveData).observe(activity, Observer {
                    fail(it)
                })
            }
        }
        if (Config.isIsDebug())
            DebugUtil.checkInMethod(activity.javaClass, "lifecycleObserver")
    }

    open fun observe(fragment: Fragment, success: (T) -> Unit, refresh: ((T) -> Unit)? = null, fail: ((Error) -> Unit)? = null) {
        if (onSuccess is UnPeekLiveData) {
            (onSuccess as UnPeekLiveData<T>).observeInFragment(fragment) {
                success(it)
            }
            if (refresh != null) {
                (onRefresh as UnPeekLiveData<T>).observeInFragment(fragment, Observer<T> {
                    refresh.invoke(it)
                })
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
            if (refresh != null) {
                onRefresh.observe(fragment, Observer<T> {
                    refresh(it)
                })
            }
            if (fail != null) {
                (onFail as MutableLiveData).observe(fragment, Observer {
                    fail(it)
                })
            }
        }
        if (Config.isIsDebug())
            DebugUtil.checkInMethod(fragment.javaClass, "lifecycleObserver")
    }

    private var compare: ((oldValue: T, newValue: T) -> Boolean)? = null

    open fun setCompare(compare: ((oldValue: T, newValue: T) -> Boolean)) {
        this.compare = compare
    }

    override fun setValue(value: T) {
        val oldValue = onSuccess.value
        if (oldValue == null) {
            super.setValue(value)
            return
        }
        if (compare != null) {
            if (compare!!(oldValue, value)) {
                super.setValue(value)
            }
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            var oldJson = GsonUtil.gson.toJson(oldValue)
            var newJson = GsonUtil.gson.toJson(value)
            var idx = oldJson.indexOf("\"code\":200000")
            if (idx > 0) {
                oldJson = oldJson.substring(idx)
                idx = newJson.indexOf("\"code\":200000")
                if (idx > 0) {
                    newJson = newJson.substring(idx)
                }
            }
            if (!TextUtils.equals(oldJson, newJson)) {
                withContext(Dispatchers.Main) {
                    super.setValue(value)
                }
            } else {
                withContext(Dispatchers.Main) {
                    if (onRefresh is UnPeekLiveData<*>) {
                        (onRefresh as UnPeekLiveData<T>).postValue(value)
                    } else if (onRefresh is MutableLiveData<*>) {
                        (onRefresh as MutableLiveData<T>).postValue(value)
                    }
                }
            }
        }
    }
}