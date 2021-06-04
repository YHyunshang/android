package com.performance.recyclerviewadapter.viewHolder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.performance.recyclerviewadapter.bean.UserInfo
import com.performance.recyclerviewadapter.databinding.Item1Binding
import com.yh.base.lib.adapter.ItemViewDelegate

/**
 * @description $
 * @date: 2021/6/3 5:13 PM
 * @author: zengbobo
 */
class UserItemViewDelegate : ItemViewDelegate<UserInfo, Item1Binding, Boolean> {
    override fun isForViewType(bean: Any?, position: Int): Boolean = bean is UserInfo

    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup?): Item1Binding = Item1Binding.inflate(inflater, parent, false)

    override fun convert(mViewBinding: Item1Binding?, bean: UserInfo?, position: Int, flag: Boolean?) {
        mViewBinding?.apply {
            root.setBackgroundColor(if (flag == true) Color.parseColor("#f6f6f6") else Color.parseColor("#ffffff"))
            tv1.text = "UserInfo $position"
        }
    }
}