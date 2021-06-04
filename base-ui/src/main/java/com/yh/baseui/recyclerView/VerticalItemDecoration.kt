package com.yh.baseui.recyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 竖向 第一条多个top  其他都是加bottom  支持传入head
 * @author gavin
 * @date 2019-08-07 14:11
 * @function
 * @param
 */
class VerticalItemDecoration(space: Int, headSize: Int = 0, topSpace: Int) : RecyclerView.ItemDecoration() {

    private var space: Int = space
    private var headViewSize = headSize
    private var topSpace = topSpace

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        when {
            headViewSize - position > 0 -> {
                outRect[0, 0, 0] = 0
            }
            headViewSize - position == 0 -> {
                outRect.set(0, topSpace, 0, space)
            }
            else -> {
                outRect.set(0, 0, 0, space)
            }
        }
    }
}