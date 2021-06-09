package com.yh.base.lib.adapter

import android.annotation.SuppressLint
import androidx.collection.SparseArrayCompat
import androidx.viewbinding.ViewBinding

/**
 * @author guowanxin
 */
class ItemViewDelegateManager<T, VB : ViewBinding, FLAG> {
    var delegates: SparseArrayCompat<ItemViewDelegate<T, VB, FLAG>> = SparseArrayCompat()
    val itemViewDelegateCount: Int
        get() = delegates.size()

    fun addDelegate(delegate: ItemViewDelegate<T, VB, FLAG>?): ItemViewDelegateManager<T, VB, FLAG> {
        var viewType = delegates.size()
        if (delegate != null) {
            delegates.put(viewType, delegate)
            viewType++
        }
        return this
    }

    fun addDelegate(viewType: Int, delegate: ItemViewDelegate<T, VB, FLAG>?): ItemViewDelegateManager<T, VB, FLAG> {
        require(delegates[viewType] == null) {
            ("An ItemViewDelegate is already registered for the viewType = "
                    + viewType
                    + ". Already registered ItemViewDelegate is "
                    + delegates[viewType])
        }
        delegates.put(viewType, delegate)
        return this
    }

    fun removeDelegate(delegate: ItemViewDelegate<T, VB, FLAG>?): ItemViewDelegateManager<T, VB, FLAG> {
        if (delegate == null) {
            throw NullPointerException("ItemViewDelegate is null")
        }
        val indexToRemove = delegates.indexOfValue(delegate)
        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove)
        }
        return this
    }

    fun removeDelegate(itemType: Int): ItemViewDelegateManager<T, VB, FLAG> {
        val indexToRemove = delegates.indexOfKey(itemType)
        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove)
        }
        return this
    }

    fun getItemViewType(item: T?, position: Int): Int {
        return try {
            val delegatesCount = delegates.size()
            for (i in delegatesCount - 1 downTo 0) {
                val delegate = delegates.valueAt(i)
                if (delegate!!.isForViewType(item, position)) {
                    return delegates.keyAt(i)
                }
            }
            showErrorInfo(item, position, "=== >>> No ItemViewDelegate added that matches position=[%d] in data source,%s !!!")
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            0
        }
    }

    fun convert(holder: ViewHolder<VB>, item: T?, position: Int, flag: FLAG?) {
        try {
            val delegatesCount = delegates.size()
            for (i in 0 until delegatesCount) {
                val delegate = delegates.valueAt(i)
                if (delegate.isForViewType(item, position)) {
                    delegate.convert(holder.mViewBinding, item, position, flag)
                    return
                }
            }
            showErrorInfo(item, position, "=== >>> No ItemViewDelegateManager added that matches position=[%d] in data source,%s !!!")
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    /**
     * 显示异常信息
     *
     * @param item
     * @param position
     * @param tipInfo
     * @return
     */
    private fun showErrorInfo(item: T?, position: Int, tipInfo: String): Int {
        @SuppressLint("DefaultLocale") val errorTip = String.format(tipInfo, position)
        throw IllegalArgumentException(errorTip)
    }

    fun getItemViewDelegate(viewType: Int): ItemViewDelegate<T, VB, FLAG>? {
        return delegates[viewType]
    }

    fun getItemViewType(itemViewDelegate: ItemViewDelegate<T, VB, FLAG>?): Int {
        if (itemViewDelegate == null) {
            return -1
        }
        return delegates.indexOfValue(itemViewDelegate!!)
    }
}