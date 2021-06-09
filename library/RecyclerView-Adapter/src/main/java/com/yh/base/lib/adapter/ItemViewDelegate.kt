package com.yh.base.lib.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * @param <T>
 * @param <VB>
 * @author zengbobo
</VB></T> */
interface ItemViewDelegate<T, VB : ViewBinding, FLAG> {
    fun isForViewType(bean: Any?, position: Int): Boolean
    fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup?): VB
    fun convert(mViewBinding: VB?, bean: T?, position: Int, flag: FLAG?)
}