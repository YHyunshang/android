package com.yh.base.lib.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yh.base.lib.adapter.ViewHolder.Companion.createViewHolder

/**
 * @author guowanxin
 */
class EmptyWrapper(val innerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var emptyView: View? = null
    private var mEmptyLayoutId = 0
    private var emptyOnClick: EmptyOnClick? = null
    private var emptyStr: String? = null
    private var emptyBgResId = 0
    private var errorBgResId = 0
    private var networkRetryStr: String? = null
    private var isShowAdd = true
    private val isEmpty: Boolean
        private get() = (emptyView != null || mEmptyLayoutId != 0) && innerAdapter.itemCount == 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isEmpty) {
            val holder: ViewHolder<*>
            holder = if (emptyView != null) {
                createViewHolder<ViewBinding>(parent.context, emptyView!!)
            } else {
                createViewHolder<ViewBinding>(parent.context,
                        LayoutInflater.from(parent.context).inflate(mEmptyLayoutId, parent, false))
            }
            holder
        } else {
            innerAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        WrapperUtils.onAttachedToRecyclerView(innerAdapter, recyclerView, object : WrapperUtils.SpanSizeCallback {
            override fun getSpanSize(layoutManager: GridLayoutManager?, oldLookup: GridLayoutManager.SpanSizeLookup?, position: Int): Int {
                if (isEmpty) {
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
        if (isEmpty) {
            WrapperUtils.setFullSpan(holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isEmpty) {
            ITEM_TYPE_EMPTY
        } else innerAdapter.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isEmpty) {
            return
        }
        innerAdapter.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return if (isEmpty) 1 else innerAdapter.itemCount
    }

    fun setEmptyView(layoutId: Int) {
        mEmptyLayoutId = layoutId
    }

    interface EmptyOnClick {
        fun onClick()
    }

    fun setEmptyStr(emptyStr: String?) {
        this.emptyStr = emptyStr
    }

    fun setEmptyBgResId(emptyBgResId: Int) {
        this.emptyBgResId = emptyBgResId
    }

    fun setErrorBgResId(errorBgResId: Int) {
        this.errorBgResId = errorBgResId
    }

    fun setNetworkRetryStr(networkRetryStr: String?) {
        this.networkRetryStr = networkRetryStr
    }

    fun setShowAdd(showAdd: Boolean) {
        isShowAdd = showAdd
    }

    fun setEmptyOnClick(emptyOnClick: EmptyOnClick?) {
        this.emptyOnClick = emptyOnClick
    }

    companion object {
        const val ITEM_TYPE_EMPTY = Int.MAX_VALUE - 1
    }
}