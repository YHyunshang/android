package com.yh.baseui.recyclerView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yh.base.utils.DisplayUtil
import com.yh.base.utils.Util

class RecyclerViewDivider(context: Context, orientation: Int) : RecyclerView.ItemDecoration() {

    private var mPaint: Paint? = null
    private var mDivider: Drawable?

    /**
     * 是否隐藏 最后一条线
     * true 隐藏 最后一条线
     */
    private var lastLineHide = false
    private var mDividerHeight = DisplayUtil.dp2px(0.5f) //分割线高度，默认为1px
    private val mOrientation //列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
            : Int

    /**
     * 仅对竖项列表
     *
     *
     * true 隐藏第一行的间隔
     * false 默认 显示第一行的间隔
     */
    private var firstLineHide = false

    private var headSize: Int = 0
    private var footSize: Int = 0

    private var listSize: Int = 0

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    constructor(context: Context, orientation: Int, drawableId: Int) : this(context, orientation) {
        mDivider = ContextCompat.getDrawable(context, drawableId)
        mDividerHeight = mDivider!!.intrinsicHeight
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    constructor(context: Context, orientation: Int, dividerHeight: Int, dividerColor: Int) : this(context, orientation) {
        mDividerHeight = dividerHeight
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = dividerColor
        mPaint!!.style = Paint.Style.FILL
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    constructor(context: Context, orientation: Int, dividerHeight: Int, dividerColor: Int, lastLineHide: Boolean) : this(context, orientation) {
        mDividerHeight = dividerHeight
        this.lastLineHide = lastLineHide
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = dividerColor
        mPaint!!.style = Paint.Style.FILL
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    constructor(context: Context, orientation: Int, dividerHeight: Int, dividerColor: Int, firstLineHide: Boolean, lastLineHide: Boolean) : this(context, orientation) {
        mDividerHeight = dividerHeight
        this.firstLineHide = firstLineHide
        this.lastLineHide = lastLineHide
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = dividerColor
        mPaint!!.style = Paint.Style.FILL
    }

    fun setHeadSize(headSize: Int): RecyclerViewDivider {
        this.headSize = headSize
        return this
    }

    fun setFootSize(footSize: Int): RecyclerViewDivider {
        this.footSize = footSize
        return this
    }

    fun setListSize(listSize: Int): RecyclerViewDivider {
        this.listSize = listSize
        return this
    }

    //获取分割线尺寸
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        when {
            (headSize - position > 0) -> {
                outRect[0, 0, 0] = 0
            }
            (listSize > 0 && footSize > 0 && position >= listSize + headSize + footSize - 2) -> {
                outRect[0, 0, 0] = 0
            }
            else -> {
                outRect.set(0, 0, 0, mDividerHeight)
            }
        }
    }

    //绘制分割线
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    //绘制横向 item 分割线
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        for (i in 0 until if (lastLineHide) childSize - 1 else childSize) {
            if (headSize > i) continue
            val child = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(child)
            if (listSize > 0 && footSize > 0 && pos >= listSize + headSize + footSize - 2) continue
//            if (firstLineHide && i == 0 && pos == 0) {
//                continue
//            }

            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + mDividerHeight
            if (mDivider != null) {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint!!)
            }
        }
    }

    //绘制纵向 item 分割线
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
//        val listSize = if (lastLineHide) childSize - footSize - 1 else childSize - footSize
//        val listSize = if (lastLineHide) childSize - 1 else childSize
        for (i in 0 until if (lastLineHide) childSize - 1 else childSize) {
            if (headSize > i) continue
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + mDividerHeight
            if (mDivider != null) {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint!!)
            }
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     * @param orientation 列表方向
     */
    init {
        require(!(orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL)) { "请输入正确的参数！" }
        mOrientation = orientation
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }
}