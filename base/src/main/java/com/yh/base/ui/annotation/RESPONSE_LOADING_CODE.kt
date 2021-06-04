package com.yh.base.ui.annotation

import androidx.annotation.IntDef
import com.yh.base.ui.BaseViewModel.Companion.FLAG_ERROR_LAYER
import com.yh.base.ui.BaseViewModel.Companion.FLAG_LOADING
import com.yh.base.ui.BaseViewModel.Companion.FLAG_NO_TOAST
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @description $
 * @date: 2021/5/13 1:33 PM
 * @author: zengbobo
 */
@IntDef(value = [FLAG_LOADING,
    FLAG_ERROR_LAYER,
    FLAG_NO_TOAST])
@Retention(RetentionPolicy.SOURCE)
annotation class RESPONSE_LOADING_CODE {}
