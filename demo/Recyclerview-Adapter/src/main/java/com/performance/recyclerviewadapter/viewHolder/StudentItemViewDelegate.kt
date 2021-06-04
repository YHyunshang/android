package com.performance.recyclerviewadapter.viewHolder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.performance.recyclerviewadapter.bean.Student
import com.performance.recyclerviewadapter.databinding.Item2Binding
import com.yh.base.lib.adapter.ItemViewDelegate

/**
 * @description $
 * @date: 2021/6/3 5:13 PM
 * @author: zengbobo
 */
class StudentItemViewDelegate : ItemViewDelegate<Student, Item2Binding, Boolean> {
    override fun isForViewType(bean: Any?, position: Int): Boolean = bean is Student

    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup?): Item2Binding = Item2Binding.inflate(inflater, parent, false)

    override fun convert(mViewBinding: Item2Binding?, bean: Student?, position: Int, flag: Boolean?) {
        mViewBinding?.apply {
            root.setBackgroundColor(if (flag == true) Color.parseColor("#f6f6f6") else Color.parseColor("#ffffff"))
            tv1.text = "Student $position"
        }
    }
}