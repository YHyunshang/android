package com.yh.base.ui.toast

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.yh.base.R
import com.yh.base.utils.DisplayUtil.dp2px
import com.yh.base.utils.Util

/**
 * @description 自定义Toast样式
 * @time 2021/4/7 10:35 AM
 * @author zengbobo
 */
class CustomToast @SuppressLint("InflateParams") constructor(context: Context, text: CharSequence?, duration: Int) : Toast(context) {
    private val tvMessage: TextView
    override fun setText(resId: Int) {
        tvMessage.setText(resId)
    }

    override fun setText(s: CharSequence) {
        tvMessage.text = s
    }

    companion object {
        fun makeText(context: Context, text: CharSequence?, duration: Int): Toast {
            return CustomToast(context, text, duration)
        }

        fun makeText(context: Context, resId: Int, duration: Int): Toast {
            return makeText(context, context.resources.getText(resId), duration)
        }
    }

    init {
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflate.inflate(R.layout.lib_custom_toast, null)
        tvMessage = view.findViewById(R.id.custom_toast_text)
        tvMessage.text = text
        setView(view)
        setDuration(duration)
        setGravity(Gravity.BOTTOM, 0, dp2px(100.0f))
    }
}