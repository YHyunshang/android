package com.yh.baseui.view.flowLayout

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * @link https://github.com/xiangcman/LayoutManager-FlowLayout
 * Created by xiangcheng on 18/1/17.
 * 邮箱:a1002326270@163.com
 * qq:1002326270
 * 主要是为了适配rv嵌套rv的案例，如果大家有什么好的方案请提出来，谢谢了!!!
 */
class NestedRecyclerView : RecyclerView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {}

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        val layoutManager = layoutManager as FlowLayoutManager?
        val widthMode = MeasureSpec.getMode(widthSpec)
        val measureWidth = MeasureSpec.getSize(widthSpec)
        val heightMode = MeasureSpec.getMode(heightSpec)
        val measureHeight = MeasureSpec.getSize(heightSpec)
        val width: Int
        val height: Int
        width = if (widthMode == MeasureSpec.EXACTLY) {
            measureWidth
        } else {
            //以实际屏宽为标准
            context.resources.displayMetrics.widthPixels
        }
        height = if (heightMode == MeasureSpec.EXACTLY) {
            measureHeight
        } else {
            layoutManager!!.totalHeight + paddingTop + paddingBottom
        }
        setMeasuredDimension(width, height)
    }
}