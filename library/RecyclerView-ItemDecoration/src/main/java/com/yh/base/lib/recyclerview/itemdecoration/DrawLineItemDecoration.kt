package com.yh.base.lib.recyclerview.itemdecoration

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * @param margin        item之间距离
 * @param mColor        画笔颜色
 * @param mLineHeight   left、top、right、bottom画线高度
 * @param start         isLeftLine、isRightLine 画线代表顶部距离；
 *                      isTopLine、isBottomLine 画线代表左边距离;
 *                      RecyclerView.HORIZONTAL 代表顶部距离；
 *                      RecyclerView.VERTICAL 代表左边距离;
 * @param end           isLeftLine、isRightLine 画线代表底部距离；
 *                      isTopLine、isBottomLine 画线代表右边距离;
 *                      RecyclerView.HORIZONTAL 代表底部距离；
 *                      RecyclerView.VERTICAL 代表右边距离;
 * @param isLeftLine    RecyclerView左边是否画线
 * @param isTopLine     RecyclerView顶部是否画线
 * @param isRightLine   RecyclerView右边边是否画线
 * @param isBottomLine  RecyclerView低边是否画线
 * @description $
 * @date: 2021/4/17 1:49 PM
 * @author: zengbobo
 */
class DrawLineItemDecoration(val margin: Float, @ColorInt val mColor: Int,
                             val mLineHeight: Float = 0f,
                             val start: Float = 0f,
                             val end: Float = 0f,
                             val isLeftLine: Boolean = false,
                             val isTopLine: Boolean = false,
                             val isRightLine: Boolean = false,
                             val isBottomLine: Boolean = false) : RecyclerView.ItemDecoration() {
    var mPaint: Paint = Paint()

    init {
        mPaint.apply {
            color = mColor
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (isLeftLine) {
            c.drawRect(parent.left.toFloat(), start, parent.left.toFloat() + mLineHeight, parent.height.toFloat() - end, mPaint)
        }

        if (isTopLine) {
            c.drawRect(parent.left.toFloat() + start, 0f, parent.right.toFloat() - end, mLineHeight, mPaint)
        }

        if (isRightLine) {
            c.drawRect(parent.right.toFloat() - mLineHeight, start, parent.right.toFloat(), parent.height.toFloat() - end, mPaint)
        }

        if (isBottomLine) {
            c.drawRect(parent.left.toFloat() + start, parent.height.toFloat() - mLineHeight, parent.right.toFloat() - end, parent.height.toFloat(), mPaint)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent.layoutManager is GridLayoutManager) {
            onDrawGridLayoutManager(c, parent, state)
        } else if (parent.layoutManager is LinearLayoutManager) {
            onDrawLinearLayoutManager(c, parent, state)
        }

    }

    private fun onDrawLinearLayoutManager(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as LinearLayoutManager
        val count: Int = parent.childCount

        if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
            for (i in 0 until count) {
                val view: View = parent.getChildAt(i)
                val position = parent.getChildAdapterPosition(view)
                val left = view.left.toFloat()
                val top = view.top.toFloat() + start
                val right = view.right.toFloat()
                val bottom = view.bottom.toFloat() - end

                if (!isLeftLine && position == 0) {
                    c.drawRect(left - mLineHeight, top, left, bottom, mPaint)
                }
                if (!isRightLine || position + 1 != parent.adapter?.itemCount ?: -1) {
                    c.drawRect(right, top, right + margin, bottom, mPaint)
                } else {
                    c.drawRect(right, top, right + mLineHeight, bottom, mPaint)
                }
            }
        } else {
            for (i in 0 until count) {
                val view: View = parent.getChildAt(i)
                val position = parent.getChildAdapterPosition(view)
                val left = view.left.toFloat() + start
                val top = view.top.toFloat()
                val right = view.right.toFloat() - end
                val bottom = view.bottom.toFloat()

                if (!isTopLine && position == 0) {
                    c.drawRect(left, top - mLineHeight, right, top, mPaint)
                }
                if (!isBottomLine || position + 1 != parent.adapter?.itemCount ?: -1) {
                    c.drawRect(left, bottom, right, bottom + margin, mPaint)
                } else {
                    c.drawRect(left, bottom, right, bottom + mLineHeight, mPaint)
                }
            }
        }
    }

    private fun onDrawGridLayoutManager(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as? GridLayoutManager ?: return
        val count: Int = parent.childCount
        layoutManager.apply {
            if (orientation == RecyclerView.HORIZONTAL) {
                for (i in 0 until count) {
                    val view: View = parent.getChildAt(i)
                    val position = parent.getChildAdapterPosition(view)
                    val left = view.left.toFloat()
                    val top = view.top.toFloat() + start
                    val right = view.right.toFloat()
                    val bottom = view.bottom.toFloat() - end

                    when (position % layoutManager.spanCount) {
                        0 -> {
                            if (!isLeftLine) {
                                c.drawRect(left - mLineHeight, top, left, bottom, mPaint)
                            }
                            c.drawRect(right, top, right + margin, bottom, mPaint)
                        }
                        layoutManager.spanCount - 1 -> {
                            if (!isRightLine) {
                                c.drawRect(right, top, right + mLineHeight, bottom, mPaint)
                            }
                        }
                        else -> {
                            c.drawRect(right, top, right + margin, bottom, mPaint)
                        }
                    }
                }
            } else {
                for (i in 0 until count) {
                    val view: View = parent.getChildAt(i)
                    val position = parent.getChildAdapterPosition(view)
                    val left = view.left.toFloat() + start
                    val top = view.top.toFloat()
                    val right = view.right.toFloat() - end
                    val bottom = view.bottom.toFloat()

                    if (position % layoutManager.spanCount != layoutManager.spanCount - 1) {
                        c.drawRect(right + end, top, right + end + margin, bottom, mPaint)
                    }
                    if (!isTopLine && position % layoutManager.spanCount == 0) {
                        c.drawRect(start, top - mLineHeight, parent.width.toFloat() - end, top, mPaint)
                    }

                    val itemCount = parent.adapter?.itemCount ?: 0
                    val totalPage = itemCount / layoutManager.spanCount + if (itemCount % layoutManager.spanCount > 0) 1 else 0
                    if (!isBottomLine || position / layoutManager.spanCount != totalPage - 1) {
                        c.drawRect(start, bottom, parent.width.toFloat() - end, bottom + mLineHeight, mPaint)
                    }
                }
            }
        }
    }

}