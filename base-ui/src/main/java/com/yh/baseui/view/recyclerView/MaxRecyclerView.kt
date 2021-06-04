package com.yh.baseui.view.recyclerView

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.yh.baseui.R

/**
 * @description 设置最大高度
 * @date: 2021/4/15 8:24 PM
 * @author: zengbobo
 */
class MaxRecyclerView : RecyclerView {
    private var mMaxHeight = 0

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.MaxRecyclerView)
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxRecyclerView_maxHeight, mMaxHeight)
        arr.recycle()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        val height = measuredHeight
        if (height > mMaxHeight) {
            setMeasuredDimension(widthSpec, mMaxHeight)
        }
    }
}