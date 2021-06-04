package com.yh.base.lib.recyclerview.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager

/**
 * @param  margin item之间距离的一半
 * @param  topParent  RecyclerView top (注意： RecyclerView.HORIZONTAL spanCount>3的时候GridLayoutManager的时候保持margin一致)
 * @param  bottomParent  RecyclerView bottom (注意： RecyclerView.HORIZONTAL  spanCount>3的时候GridLayoutManager的时候保持margin一致)
 * @param  leftParent  RecyclerView left (注意： RecyclerView.VERTICAL spanCount>3的时候GridLayoutManager的时候保持margin一致)
 * @param  rightParent  RecyclerView right(注意： RecyclerView.VERTICAL spanCount>3的时候GridLayoutManager的时候保持margin一致)
 * @description
 * @time 2021/4/14 4:27 PM
 * @author zengbobo
 */
class MarginLinearItemDecoration(private val margin: Int = 0, private val topParent: Int = 0, private val bottomParent: Int = 0,
                                 private val leftParent: Int = 0, private val rightParent: Int = 0) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.layoutManager is FlexboxLayoutManager) {
            getItemOffsetsFlexBoxLayoutManager(outRect, view, parent, state)
        } else if (parent.layoutManager is GridLayoutManager) {
            getItemOffsetsGridLayoutManager(outRect, view, parent, state)
        } else if (parent.layoutManager is LinearLayoutManager) {
            getItemOffsetsLinearLayoutManager(outRect, view, parent, state)
        }
    }

    private fun getItemOffsetsFlexBoxLayoutManager(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(margin, margin, margin, margin)
    }

    private fun getItemOffsetsGridLayoutManager(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as? GridLayoutManager ?: return
        layoutManager.apply {
            if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
                val position = parent.getChildAdapterPosition(view)
                val itemCount = parent.adapter?.itemCount ?: 0
                var l = 0
                var t = topParent
                var r = 0
                var b = bottomParent

                when (position % layoutManager.spanCount) {
                    0 -> {
                        l = leftParent
                        r = margin
                    }
                    layoutManager.spanCount - 1 -> {
                        l = margin
                        r = rightParent
                    }
                    else -> {
                        l = margin
                        r = margin
                    }
                }
                outRect.set(l, t, r, b)
            } else {
                val position = parent.getChildAdapterPosition(view)
                val itemCount = parent.adapter?.itemCount ?: 0
                var l = leftParent
                var t = margin
                var r = rightParent
                var b = margin
                if (position < layoutManager.spanCount) {
                    t = topParent
                }

                val totalPage = itemCount / layoutManager.spanCount + if (itemCount % layoutManager.spanCount > 0) 1 else 0
                if (position / layoutManager.spanCount == totalPage - 1) {
                    b = bottomParent
                }

                when (position % layoutManager.spanCount) {
                    0 -> {
                        l = leftParent
                        r = margin
                    }
                    layoutManager.spanCount - 1 -> {
                        l = margin
                        r = rightParent
                    }
                    else -> {
                        l = margin
                        r = margin
                    }
                }
                outRect.set(l, t, r, b)
            }
        }
    }

    private fun getItemOffsetsLinearLayoutManager(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as? LinearLayoutManager ?: return
        layoutManager.apply {
            if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
                when (parent.getChildAdapterPosition(view)) {
                    0 -> {
                        outRect.set(leftParent, topParent, margin, bottomParent)
                    }
                    ((parent.adapter?.itemCount ?: 0) - 1) -> {
                        outRect.set(margin, topParent, rightParent, bottomParent)
                    }
                    else -> {
                        outRect.set(margin, topParent, margin, bottomParent)
                    }
                }
            } else {
                when (parent.getChildAdapterPosition(view)) {
                    0 -> {
                        outRect.set(leftParent, topParent, rightParent, margin)
                    }
                    ((parent.adapter?.itemCount ?: 0) - 1) -> {
                        outRect.set(leftParent, margin, rightParent, bottomParent)
                    }
                    else -> {
                        outRect.set(leftParent, margin, rightParent, margin)
                    }
                }
            }
        }
    }
}