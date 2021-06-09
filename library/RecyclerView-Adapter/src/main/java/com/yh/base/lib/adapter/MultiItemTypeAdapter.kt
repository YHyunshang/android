package com.yh.base.lib.adapter

import android.content.Context
import androidx.viewbinding.ViewBinding

/**
 * 可以添加多type类型的adapter
 *
 * @author guowanxin
 */
open class MultiItemTypeAdapter(context: Context) : BaseAdapter<Any, ViewBinding, Any?>(context) {
    override fun getItemBean(position: Int): Any? {
        return super.getItemBean(position)
    }

    override fun getPositionFlag(position: Int): Any? {
        return super.getPositionFlag(position)
    }

     fun <Delegate> addItemViewDelegate(itemViewDelegate: Delegate?): ItemViewDelegateManager<Any, ViewBinding, Any?> {
        return super.addItemViewDelegate(itemViewDelegate as? ItemViewDelegate<Any, ViewBinding, Any?>?)
    }

     fun <Delegate> addItemViewDelegate(viewType: Int, itemViewDelegate: Delegate?): ItemViewDelegateManager<Any, ViewBinding, Any?> {
        return super.addItemViewDelegate(viewType, itemViewDelegate as? ItemViewDelegate<Any, ViewBinding, Any?>?)
    }
}