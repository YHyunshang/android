package com.yh.sdk.imageLoader

import com.bumptech.glide.request.target.Target

/**
 * @description 监听类
 * @date: 2021/4/8 8:07 PM
 * @author: zengbobo
 */
interface YHImageLoaderListener<R> {

    fun onLoadStarted()

    fun onResourceReady(resource: R?, isFirstResource: Boolean): Boolean

    fun onLoadFailed(e: Exception?, isFirstResource: Boolean): Boolean
}