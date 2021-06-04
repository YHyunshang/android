package com.yh.baseui.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorInt

/**
 * @description $
 * @date: 2021/4/27 1:52 PM
 * @author: zengbobo
 */
object SpannableStringUtils {

    fun foregroundColorSpan(text: String?, @ColorInt color: Int, start: Int, end: Int): SpannableString {
        var spannableString = SpannableString(text)
        return foregroundColorSpan(spannableString, color, start, end)
    }
    
    fun foregroundColorSpan(spannableString: SpannableString, @ColorInt color: Int, start: Int, end: Int): SpannableString {
        spannableString.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

}