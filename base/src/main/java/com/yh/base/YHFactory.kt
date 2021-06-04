package com.yh.base

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.yh.base.ui.BaseViewModel
import com.yh.sdk.imageLoader.YHImageLoader

/**
 * @description 项目中单例对象通过这个来获取，方便了解单例使用情况$
 * @date: 2021/4/6 3:28 PM
 * @author: zengbobo
 */
object YHFactory {
    fun <T : BaseViewModel> getViewModel(owner: ViewModelStoreOwner, modelClass: Class<T>): T {
        return ViewModelProvider(owner).get(modelClass)
    }

    /**
     * @return  图片加载类
     *
     * @see com.yh.sdk.imageLoader.YHImageLoader  第一种方式
     * @see com.yh.base.utils.ImageLoaderUtils  第二种方式
     * @description
     * @time 2021/4/9 11:25 AM
     * @author zengbobo
     */
    fun getImageLoader(): YHImageLoader {
        return YHImageLoader
    }

}