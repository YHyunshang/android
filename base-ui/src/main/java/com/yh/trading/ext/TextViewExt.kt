package com.yh.trading.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * @description TextView扩展函数 设置textview图标
 * @date: 2021/4/13 2:34 PM
 * @author: guowanxin
 * @param drawableId drawableId=0时 图片设为空
 * @param position   0 左 1 上 2右 3下
 */
fun TextView.setDrawableImage(context: Context, drawableId: Int, position: Int) {
    var drawable: Drawable? = null
    if (0 != drawableId) {
        drawable = ContextCompat.getDrawable(context, drawableId)
        drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    }
    when (position) {
        0 -> setCompoundDrawables(drawable, null, null, null)
        1 -> setCompoundDrawables(null, drawable, null, null)
        2 -> setCompoundDrawables(null, null, drawable, null)
        3 -> setCompoundDrawables(null, null, null, drawable)
        else -> throw IllegalStateException("Unexpected value: $position")
    }
}

