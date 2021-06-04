package com.performance.recyclerviewadapter.adapter

import android.content.Context
import com.performance.recyclerviewadapter.viewHolder.StudentItemViewDelegate
import com.performance.recyclerviewadapter.viewHolder.UserItemViewDelegate
import com.yh.base.lib.adapter.MultiItemTypeAdapter

/**
 * @description $
 * @date: 2021/6/3 5:17 PM
 * @author: zengbobo
 */
class MyMultiItemTypeAdapter(context: Context) : MultiItemTypeAdapter(context) {
    override fun getPositionFlag(position: Int): Boolean? {
        return position % 2 == 0
    }

    init {
        addItemViewDelegate(StudentItemViewDelegate())
        addItemViewDelegate(UserItemViewDelegate())
    }
}