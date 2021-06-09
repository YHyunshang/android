package com.yh.base.lib.adapter

import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yh.base.lib.adapter.ViewHolder.Companion.createViewHolder

/**
 * 可以添加head和foot的adapter
 *
 * @author guowanxin
 */
class HeaderAndFooterWrapper(val innerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mHeaderViews = SparseArrayCompat<View?>()
    private val mFootViews = SparseArrayCompat<View?>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = mHeaderViews[viewType]
        if (view != null) {
            if (view.parent != null) {
                (view.parent as ViewGroup).removeView(view)
            }
            return createViewHolder<ViewBinding>(parent.context, view)
        }
        view = mFootViews[viewType]
        if (view != null) {
            if (view.parent != null) {
                (view.parent as ViewGroup).removeView(view)
            }
            return createViewHolder<ViewBinding>(parent.context, view)
        }
        return innerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - headersCount - realItemCount)
        }
        return innerAdapter.getItemViewType(position - headersCount)
    }

    private val realItemCount: Int
        private get() = innerAdapter.itemCount

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeaderViewPos(position)) {
            return
        }
        if (isFooterViewPos(position)) {
            return
        }
        innerAdapter.onBindViewHolder(holder, position - headersCount)
    }

    override fun getItemCount(): Int {
        return headersCount + footersCount + realItemCount
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        WrapperUtils.onAttachedToRecyclerView(innerAdapter, recyclerView, object : WrapperUtils.SpanSizeCallback {
            override fun getSpanSize(layoutManager: GridLayoutManager?, oldLookup: GridLayoutManager.SpanSizeLookup?, position: Int): Int {
                val viewType = getItemViewType(position)
                if (mHeaderViews[viewType] != null) {
                    return layoutManager?.spanCount ?: 1
                } else if (mFootViews[viewType] != null) {
                    return layoutManager?.spanCount ?: 1
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position)
                }
                return 1
            }
        })
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        innerAdapter.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder)
        }
    }

    private fun isHeaderViewPos(position: Int): Boolean {
        return position < headersCount
    }

    private fun isFooterViewPos(position: Int): Boolean {
        return position >= headersCount + realItemCount
    }

    fun addHeaderView(view: View?) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view)
    }

    fun addFootView(view: View?) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view)
    }

    val headersCount: Int
        get() = mHeaderViews.size()
    val footersCount: Int
        get() = mFootViews.size()

    fun removeHeaderView() {
        if (mHeaderViews.size() > 0) {
            mHeaderViews.clear()
        }
    }

    fun removeFootView() {
        if (mFootViews.size() > 0) {
            mFootViews.clear()
        }
    }

    companion object {
        private const val BASE_ITEM_TYPE_HEADER = 100000
        private const val BASE_ITEM_TYPE_FOOTER = 200000
    }
}