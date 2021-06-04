package com.yh.base.lib.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @param <VB>
 * @author zengbobo
</VB> */
open class ViewHolder<VB : ViewBinding> : RecyclerView.ViewHolder {
    protected var mContext: Context

    @JvmField
    var mViewBinding: VB? = null

    constructor(context: Context, mViewBinding: VB) : super(mViewBinding.root) {
        mContext = context
        this.mViewBinding = mViewBinding
    }

    constructor(context: Context, view: View) : super(view) {
        mContext = context
    }

    companion object {
        fun <VB : ViewBinding> createViewHolder(context: Context, mViewBinding: VB): ViewHolder<VB> {
            return ViewHolder<VB>(context, mViewBinding)
        }

        @JvmStatic
        fun <VB : ViewBinding> createViewHolder(context: Context, view: View): ViewHolder<VB> {
            return ViewHolder<VB>(context, view)
        }
    }
}