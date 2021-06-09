package com.yh.base.lib.recyclerview.itemdecoration

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @description  悬浮置顶
 * @date: 2021/4/21 10:52 AM
 * @author: zengbobo
 */
abstract class TopTitleItemDecoration<VB : ViewBinding>(parent: RecyclerView) : RecyclerView.ItemDecoration() {
    lateinit var titleBinding: VB

    init {
        titleBinding = createViewBinding(parent)
        onMeasure()
        parent.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val height =titleBinding.root.measuredHeight?:-1
                val y:Int = (event?.y?:0).toInt()
                if (y < height) {
                    return true
                }
                return false
            }

        })
    }

    abstract fun createViewBinding(parent: RecyclerView): VB

    abstract fun bind(titleBinding: VB, parent: RecyclerView, position: Int): Boolean

    abstract fun isTop(view: View, measuredHeight: Int): Boolean

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (parent.childCount <= 0) {
            return
        }
        initTitle(parent)
        if (titleBinding.root.measuredHeight <= 0) {
            onMeasure()
        }
        onLayout(parent)
        parent.drawChild(c, titleBinding.root, 0)
    }

    private fun onMeasure() {
        val root = titleBinding.root
        root.measure(View.MeasureSpec.makeMeasureSpec(root.layoutParams.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(root.layoutParams.height, View.MeasureSpec.EXACTLY))
    }

    private fun onLayout(parent: RecyclerView) {
        val childAt = parent.getChildAt(1)
        if (childAt == null) {
            return
        }
        val viewHolder = parent.findContainingViewHolder(childAt) as RecyclerView.ViewHolder
        val measuredHeight = titleBinding.root.measuredHeight
        var top = 0
        if (isTop(viewHolder.itemView, measuredHeight)) {
            top = Math.min(measuredHeight,
                    measuredHeight - viewHolder.itemView.top)
        }
        titleBinding.root.layout(0, -top, titleBinding.root.measuredWidth, measuredHeight - top)
    }

    fun initTitle(parent: RecyclerView) {
        val childAt = parent.getChildAt(0)
        if (childAt == null) {
            return
        }
        val viewHolder = parent.findContainingViewHolder(childAt)
        val position = viewHolder?.adapterPosition ?: -1
        if (bind(titleBinding, parent, position)) {
            onMeasure()
        }
    }

}