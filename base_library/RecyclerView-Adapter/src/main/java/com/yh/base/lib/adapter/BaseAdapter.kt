package com.yh.base.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.util.*

/**
 * @param <T>    数据实体类
 * @param <VB>   xml对应生成的ViewBinding（再也不用findViewById了）
 * @param <FLAG> 任何类型，数据渲染的时候一些特殊处理预留字段（如：item是否选中）
 *
 * @description 对RecyclerView.Adapter统一封装，结合ViewBinding使用
 * @time 2021/6/3 4:39 PM
 * @author zengbobo
 */
open class BaseAdapter<T, VB : ViewBinding, FLAG>(var mContext: Context) : RecyclerView.Adapter<ViewHolder<VB>>() {
    private val inflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }
    private val mItemViewDelegateManager: ItemViewDelegateManager<T, VB, FLAG> by lazy {
        ItemViewDelegateManager<T, VB, FLAG>()
    }
    val data: MutableList<T> by lazy { mutableListOf() }

    private var mOnItemClickListener: OnItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (!useItemViewDelegateManager()) {
            super.getItemViewType(position)
        } else mItemViewDelegateManager.getItemViewType(getItemBean(position), position)
    }

    open fun getItemBean(position: Int): T? {
        return if (position < 0 || position > data.size - 1) {
            null
        } else data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //<editor-fold desc="onCreateViewHolder扩展">
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        val itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType)
        val holder: ViewHolder<VB> = ViewHolder.createViewHolder<VB>(mContext, itemViewDelegate!!.getViewBinding(inflater, parent))
        onViewHolderCreated(holder)
        if (isDefaultListener(viewType)) {
            setListener(parent, holder, viewType)
        }
        return holder
    }

    open fun onViewHolderCreated(holder: ViewHolder<VB>?) {}

    open fun setListener(parent: ViewGroup?, viewHolder: ViewHolder<VB>, viewType: Int) {
        viewHolder.itemView.setOnClickListener { v: View? ->
            if (mOnItemClickListener != null) {
                val position = viewHolder.adapterPosition
                if (position >= 0) {
                    mOnItemClickListener!!.onItemClick(v, viewHolder, position)
                }
            }
        }
        viewHolder.itemView.setOnLongClickListener { v: View? ->
            if (mOnItemClickListener != null) {
                val position = viewHolder.adapterPosition
                return@setOnLongClickListener mOnItemClickListener!!.onItemLongClick(v, viewHolder, position)
            }
            false
        }
    }

    /**
     * @param viewType
     * @description 是否使用该框架默认点击事件
     * @time 2021/6/3 3:24 PM
     * @author zengbobo
     */
    fun isDefaultListener(viewType: Int): Boolean {
        return false
    }

    //</editor-fold>
    //<editor-fold desc="onBindViewHolder 扩展">
    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        mItemViewDelegateManager.convert(holder, getItemBean(position), position, getPositionFlag(position))
    }

    /**
     * 做一些特定判断（如：选中）
     *
     * @param position
     * @return
     */
    open fun getPositionFlag(position: Int): FLAG? {
        return null
    }

    open fun <Delegate:ItemViewDelegate<T, VB, FLAG>> addItemViewDelegate(itemViewDelegate: Delegate?): ItemViewDelegateManager<T, VB, FLAG> {
        if (itemViewDelegate != null) {
            mItemViewDelegateManager.addDelegate(itemViewDelegate)
        }
        return mItemViewDelegateManager
    }

    open fun  <Delegate:ItemViewDelegate<T, VB, FLAG>>  addItemViewDelegate(viewType: Int, itemViewDelegate: Delegate?): ItemViewDelegateManager<T, VB, FLAG> {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate)
        return mItemViewDelegateManager
    }

    protected fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.itemViewDelegateCount > 0
    }

    //</editor-fold>
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        /**
         * item点击事件
         *
         * @param view
         * @param holder
         * @param position
         */
        fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int)

        /**
         * item长按事件
         *
         * @param view
         * @param holder
         * @param position
         * @return
         */
        fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean
    }

}