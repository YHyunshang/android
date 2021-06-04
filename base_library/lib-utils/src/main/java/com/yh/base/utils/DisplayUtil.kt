package com.yh.base.utils

import android.content.Context
import android.os.Build
import android.view.WindowManager

/**
 * @author zengbobo
 * @description 设备一些基本信息
 * @time 2021/4/7 11:39 AM
 */
object DisplayUtil {
    /**
     * @param context
     * @return 屏幕宽
     */
    @JvmStatic
    fun getScreenWidth(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val wm = Util.getApplication().getSystemService(
                    Context.WINDOW_SERVICE) as WindowManager
            return wm.currentWindowMetrics.bounds.width()
        } else {
            return Util.getApplication().resources.displayMetrics.widthPixels
        }
    }

    /**
     * @param context
     * @return 屏幕高
     */
    @JvmStatic
    fun getScreenHeight(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val wm = Util.getApplication().getSystemService(
                    Context.WINDOW_SERVICE) as WindowManager
            return wm.currentWindowMetrics.bounds.height()
        } else {
            return Util.getApplication().resources.displayMetrics.heightPixels
        }
    }

    /**
     * @param context
     * @return
     */
    @JvmStatic
    fun getStatusBarHeight(): Int {
        var statusbarheight = 0
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val o = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = field[o] as Int
            statusbarheight = Util.getApplication().resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusbarheight
    }

    @JvmStatic
    fun dp2pxf(dipValue: Float): Float {
        val scale = Util.getApplication().resources.displayMetrics.density
        return dipValue * scale
    }

    @JvmStatic
    fun px2dpf(pxValue: Float): Float {
        val scale = Util.getApplication().resources.displayMetrics.density
        return pxValue / scale
    }

    @JvmStatic
    fun dp2px(dipValue: Float): Int {
        val scale = Util.getApplication().resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2dp(pxValue: Float): Int {
        val scale = Util.getApplication().resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

}