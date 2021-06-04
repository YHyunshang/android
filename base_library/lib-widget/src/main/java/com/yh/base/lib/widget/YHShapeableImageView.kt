package com.yh.base.lib.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView

/**
 * @description 封装ImageView
 * @date: 5/28/21 1:55 PM
 * @author: 张致远
 */
open class YHShapeableImageView : ShapeableImageView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    var path: String? = null
    fun load(path: String, loadBlock: () -> Unit) {
        if (TextUtils.isEmpty(path) || path != this.path) {
            this.path = path
            loadBlock()
        }
    }
}