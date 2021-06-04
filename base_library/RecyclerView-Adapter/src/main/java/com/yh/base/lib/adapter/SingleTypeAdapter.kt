package com.yh.base.lib.adapter

import android.content.Context
import androidx.viewbinding.ViewBinding

/**
 * 单type普通adapter
 *
 * @author guowanxin
 */
open class SingleTypeAdapter<T, VB : ViewBinding, FLAG>(context: Context, delegate: ItemViewDelegate<T, VB, FLAG>?) : BaseAdapter<T, VB, FLAG>(context) {

    init {
        requireNotNull(delegate) { "SingleTypeAdapter constructor param delegate is no-null" }
        addItemViewDelegate(delegate)
    }

    override fun getPositionFlag(position: Int): FLAG? {
        return super.getPositionFlag(position)
    }
}