package com.yh.base.lib.widget

import android.content.Context
import android.util.AttributeSet

/**
 * An image view which always remains square with respect to its height.
 * 适用于横向RecycleView里面的图片
 * 圆角图片
 * @author guowanxin
 */
class SquaredShapeImageView : YHShapeableImageView {
    var isSameHeight: Boolean = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs == null) {
            return
        }
        val a = context.obtainStyledAttributes(attrs, R.styleable.SquaredImageView, 0, 0)
        isSameHeight = a.getBoolean(R.styleable.SquaredImageView_isSameHeight, true)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (isSameHeight) {
            setMeasuredDimension(measuredHeight, measuredHeight)
        } else {
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
        }
    }
}