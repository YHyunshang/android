package com.yh.baseui.view.flowLayout

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @link https://github.com/xiangcman/LayoutManager-FlowLayout
 * Created by xiangcheng on 17/8/22.
 */
class SpaceItemDecoration(private val left: Int, private val top: Int, private val right: Int, private val bottom: Int) : RecyclerView.ItemDecoration() {

    constructor(space: Int) : this(space, space, space, space)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = top
        outRect.left = left
        outRect.right = right
        outRect.bottom = bottom
    }
}