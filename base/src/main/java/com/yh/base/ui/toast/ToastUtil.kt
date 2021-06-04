package com.yh.base.ui.toast

import android.widget.Toast
import com.yh.base.BaseApplication
import com.yh.base.utils.StringUtils

/**
 * @description Toast工具类
 * @time 2021/4/7 10:36 AM
 * @author zengbobo
 */
object ToastUtil {
    private var mToast: Toast? = null

    @JvmStatic
    fun showLongMsg(str: CharSequence?) {
        if (mToast != null) {
            mToast!!.cancel()
        }
        if(StringUtils.isNullOrEmpty(str)){
            return
        }
        mToast = CustomToast.makeText(BaseApplication.getInstance(), str, Toast.LENGTH_LONG)
        mToast?.show()
    }

    @JvmStatic
    fun showShortMsg(str: CharSequence?) {
        if (mToast != null) {
            mToast!!.cancel()
        }
        if(StringUtils.isNullOrEmpty(str)){
            return
        }
        mToast = CustomToast.makeText(BaseApplication.getInstance(), str, Toast.LENGTH_SHORT)
        mToast?.show()
    }

    @JvmStatic
    fun showLongMsg(resId: Int) {
        showLongMsg(BaseApplication.getInstance().resources.getString(resId))
    }

    @JvmStatic
    fun showShortMsg(resId: Int) {
        showShortMsg(BaseApplication.getInstance().resources.getString(resId))
    }

    @JvmStatic
    fun cancelToast() {
        if (mToast != null) {
            mToast!!.cancel()
        }
    }
}