package com.performance.recyclerviewadapter.adapter

import android.content.Context
import com.performance.recyclerviewadapter.bean.Student
import com.performance.recyclerviewadapter.databinding.Item2Binding
import com.performance.recyclerviewadapter.viewHolder.StudentItemViewDelegate
import com.yh.base.lib.adapter.SingleTypeAdapter

/**
 * @description $
 * @date: 2021/6/3 5:17 PM
 * @author: zengbobo
 */
class MySingleTypeAdapter(context: Context) : SingleTypeAdapter<Student, Item2Binding, Boolean>(context, StudentItemViewDelegate()) {
    override fun getPositionFlag(position: Int): Boolean? {
        return position % 2 == 0
    }
}