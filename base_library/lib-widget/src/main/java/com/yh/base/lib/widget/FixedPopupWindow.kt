package com.yh.base.lib.widget

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.PopupWindow

/**
 * @description 修复Build.VERSION.SDK_INT >= 24 当高度设置MATCH_PARENT的时候偏移量参数设置不起作用；
 * @date: 2021/4/25 3:55 PM
 * @author: zengbobo
 */
class FixedPopupWindow : PopupWindow {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}
    constructor() {}
    constructor(contentView: View?) : super(contentView) {}
    constructor(width: Int, height: Int) : super(width, height) {}
    constructor(contentView: View?, width: Int, height: Int) : super(contentView, width, height) {}
    constructor(contentView: View?, width: Int, height: Int, focusable: Boolean) : super(contentView, width, height, focusable) {}

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int, gravity: Int) {
        if (Build.VERSION.SDK_INT >= 24) {
            val rect = Rect()
            anchor.getGlobalVisibleRect(rect)
            val contentHeight = (contentView.context as Activity).findViewById<View>(R.id.content).height
            val h = contentHeight - rect.bottom - yoff
            height = h
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity)
    }
}