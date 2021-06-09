package com.yh.base.utils

import android.text.TextUtils


/**
 * @description $
 * @date: 2021/4/7 10:59 AM
 * @author: zengbobo
 */
object StringUtils {
    @JvmStatic
    fun isNullOrEmpty(text: CharSequence?): Boolean {
        return TextUtils.isEmpty(text)
    }

    /**
     *
     *
     * @description  隐藏部分字符串
     * @time 2021/4/23 1:59 PM
     * @author zengbobo
     */
    @JvmStatic
    fun hideSection(phone: String, start: Int = 3, end: Int = phone.length - 5): String {
        if (StringUtils.isNullOrEmpty(phone)) {
            return ""
        }
        val buf = StringBuffer()
        for (i in phone.indices) {
            if (i in start..end) {
                buf.append("*")
            } else {
                buf.append(phone[i])
            }
        }
        return buf.toString()
    }
}