package com.yh.base.ui.ext

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yh.base.utils.LogUtils
import com.yh.baseui.R
import com.yh.baseui.recyclerView.RecyclerViewDivider
import com.yh.baseui.recyclerView.VerticalItemDecoration

/**
 * 设置RecyclerView间距
 * @param topSpace：第一条数据top值  如果top和bottom相同可以不传 默认为space  可以传0  代表不设置
 */
fun RecyclerView.addItemDecorationExt(context: Context, space: Int, topSpace: Int = space, headSize: Int = 0) {
    val spacingInPixels = context.resources.getDimensionPixelSize(space)
    val topSpaceInPixels = if (topSpace == space) spacingInPixels else if (topSpace == 0) 0 else context.resources.getDimensionPixelSize(topSpace)
    addItemDecoration(VerticalItemDecoration(spacingInPixels, headSize, topSpaceInPixels))
}

/**
 * 添加RecyclerView分割线
 */
fun RecyclerView.addItemDecorationV(context: Context, colorId: Int, lastLineHide: Boolean = true, headSize: Int = 0, footSize: Int = 0, listSize: Int = 0) {
    addItemDecoration(RecyclerViewDivider(context, LinearLayoutManager.HORIZONTAL, context.resources.getDimensionPixelSize(R.dimen.dp_line),
            ContextCompat.getColor(context, colorId), lastLineHide).setHeadSize(headSize).setFootSize(footSize).setListSize(listSize))
}

