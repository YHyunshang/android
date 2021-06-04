package com.yh.baseui.utils

import android.content.Context
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.yh.base.YHFactory
import com.yh.baseui.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * @description $
 * @date: 2021/4/8 4:19 PM
 * @author: zengbobo
 */
object ImageLoaderUtils {
    fun loadBlur(context: Context, url: String?, target: ImageView) {
        YHFactory.getImageLoader().with(context)
                .load(url ?: "")
                .error(R.drawable.ic_empty)
                .placeholder(R.drawable.ic_empty)
                .isBlur(true)
                .into(target)
    }

    fun loadRoundedCorners(context: Context, url: String?, target: ImageView, radius: Int, type: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL) {
        YHFactory.getImageLoader().with(context)
                .load(url ?: "")
                .error(R.drawable.ic_empty)
                .placeholder(R.drawable.ic_empty)
                .roundRadius(radius)
                .roundType(type)
                .into(target)
    }


    fun load(context: Context, url: String?, target: ImageView) {
        YHFactory.getImageLoader().with(context)
                .load(url ?: "")
                .error(R.drawable.ic_empty)
                .into(target)
    }

    fun load(context: Fragment, url: String?, target: ImageView) {
        YHFactory.getImageLoader().with(context)
                .load(url ?: "")
                .error(R.drawable.ic_empty)
                .into(target)
    }

    fun loadCircle(context: Context, url: String?, target: ImageView, borderOutSize: Int = 0, @ColorInt borderOutColor: Int = 0,
                   borderInSize: Int = 0, @ColorInt borderInColor: Int = 0) {
        YHFactory.getImageLoader().with(context)
                .load(url ?: "")
                .error(R.drawable.ic_empty)
                .placeholder(R.drawable.ic_empty)
                .circleBorderOut(borderOutSize, borderOutColor)
                .circleBorderIn(borderInSize, borderInColor)
                .into(target)

    }

    fun load(context: Context, url: String?, target: ImageView, isCacheable: Boolean) {
        YHFactory.getImageLoader().with(context)
                .load(url ?: "")
                .error(R.drawable.ic_empty)
                .placeholder(R.drawable.ic_empty)
                .skipMemoryCache(isCacheable)
                .into(target)
    }


    fun load(context: Context, url: String?, target: ImageView, @DrawableRes placeholderId: Int, @DrawableRes errorId: Int) {
        YHFactory.getImageLoader().with(context)
                .load(url ?: "")
                .placeholder(placeholderId)
                .error(errorId)
                .into(target)
    }


}