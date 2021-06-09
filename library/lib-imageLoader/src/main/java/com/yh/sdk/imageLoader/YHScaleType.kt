package com.yh.sdk.imageLoader

import android.graphics.Matrix
import android.widget.ImageView

/**
 * @description $
 * @date: 2021/4/8 3:47 PM
 * @author: zengbobo
 */
enum class YHScaleType {
    MATRIX,
    FIT_XY,
    FIT_START,
    FIT_CENTER,
    FIT_END,
    CENTER,
    CENTER_CROP,
    CENTER_INSIDE,
    AT_LEAST,
    AT_MOST,
    NONE;
}