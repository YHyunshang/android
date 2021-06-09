package com.yh.base.ui.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.viewbinding.ViewBinding
import com.yh.base.ui.ext.getVbClazz
import com.yh.base.utils.Reflector

/**
 * @description listView的Adapter封装
 *
 * @date: 2021/4/6 4:11 PM
 * @author: zhangzhiyuan
 */
abstract class ListViewAdapter<T, VB : ViewBinding>(mList: List<T>) : BaseAdapter() {
    var mList: List<T> = mList
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mView: View
        var holder: ViewHolder<VB>
        if (convertView == null) {
            val mViewBinding: VB = Reflector.invoke(getVbClazz(this), "inflate", arrayOf(LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType), LayoutInflater.from(parent.context), parent, false);
            holder = ViewHolder(mViewBinding)
            mView = mViewBinding.root
            mView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder<VB>
            mView = convertView;
        }
        convert(holder, mList[position], position)
        return mView
    }

    override fun getItem(position: Int): T {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mList.size
    }

    abstract fun convert(holder: ViewHolder<VB>, data: T, position: Int)

    class ViewHolder<VB : ViewBinding>(viewBinding: VB) {
        val viewBinding: VB = viewBinding
    }


}