package com.yh.base.lib.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @author guowanxin
 */
object WrapperUtils {
    fun onAttachedToRecyclerView(innerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, recyclerView: RecyclerView, callback: SpanSizeCallback) {
        innerAdapter.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val gridLayoutManager = layoutManager
            val spanSizeLookup = gridLayoutManager.spanSizeLookup
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position)
                }
            }
            gridLayoutManager.spanCount = gridLayoutManager.spanCount
        }
    }

    fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
            lp.isFullSpan = true
        }
    }

    fun setNotFullSpan(holder: RecyclerView.ViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
            lp.isFullSpan = false
        }
    }

    interface SpanSizeCallback {
        /**
         * 获取span大小
         * @param layoutManager
         * @param oldLookup
         * @param position
         * @return
         */
        fun getSpanSize(layoutManager: GridLayoutManager?, oldLookup: GridLayoutManager.SpanSizeLookup?, position: Int): Int
    }
}