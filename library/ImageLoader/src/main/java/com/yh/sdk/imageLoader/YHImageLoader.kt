package com.yh.sdk.imageLoader

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide

/**
 * @description 应用层通过该类来使用图片加载
 *
 * @date: 2021/4/8 2:31 PM
 *
 * @author: zengbobo
 */
object YHImageLoader {
    fun with(context: Context): YHImageRequestManager {
        return YHImageRequestManager(Glide.with(context))
    }

    fun with(activity: Activity): YHImageRequestManager {
        return YHImageRequestManager(Glide.with(activity))
    }

    fun with(fragmentActivity: FragmentActivity): YHImageRequestManager {
        return YHImageRequestManager(Glide.with(fragmentActivity))
    }

    fun with(fragment: Fragment): YHImageRequestManager {
        return YHImageRequestManager(Glide.with(fragment))
    }

    fun with(view: View): YHImageRequestManager {
        return YHImageRequestManager(Glide.with(view))
    }
}