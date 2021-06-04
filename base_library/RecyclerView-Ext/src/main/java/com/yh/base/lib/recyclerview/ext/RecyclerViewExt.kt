package com.yh.base.ui.ext

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @description item置顶
 * @date: 2021/4/14 8:03 PM
 * @author: zengbobo
 */
fun RecyclerView.scrollToPositionTop(position: Int) {
    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
}

/**
 * @description item局中
 * @time 2021/6/3 7:20 PM
 * @author zengbobo
 */
fun RecyclerView.scrollToPositionCenter(position: Int) {
    if (adapter == null || childCount <= 0 || adapter?.itemCount ?: 0 < position + 1) {
        return
    }
    var offsetPosition = 0
    var firstPosition = 0
    var lastPosition = 0
    var spanCount = 1
    if (layoutManager is GridLayoutManager) {
        val manager = layoutManager as GridLayoutManager
        firstPosition = manager.findFirstVisibleItemPosition()
        lastPosition = manager.findLastVisibleItemPosition()
        spanCount = manager.spanCount
    } else if (layoutManager is LinearLayoutManager) {
        val manager = layoutManager as LinearLayoutManager
        firstPosition = manager.findFirstVisibleItemPosition()
        lastPosition = manager.findLastVisibleItemPosition()
        spanCount = 1
    }

    //最大的position
    val maxPosition: Int = (adapter?.itemCount ?: 0) - 1
    //position的偏移量
    val offset = (lastPosition - firstPosition) / (2 * spanCount)

    offsetPosition = if (firstPosition > position) { //第一种情况
        position - offset
    } else if (lastPosition < position) { //第二种情况
        position + offset
    } else { //第三种情况
        if (lastPosition - position > position - firstPosition) { //第三种情况中position在中心点偏上
            position - offset
        } else { //第三种情况中position在中心点偏下
            position + offset
        }
    }
    //偏移过的offsetPosition越界
    if (offsetPosition < 0) {
        offsetPosition = 0
    } else if (offsetPosition > maxPosition) {
        offsetPosition = maxPosition
    }
    scrollToPosition(offsetPosition)
}



