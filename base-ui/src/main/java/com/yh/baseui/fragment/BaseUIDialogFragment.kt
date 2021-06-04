package com.yh.baseui.fragment

import androidx.viewbinding.ViewBinding
import com.yh.base.ui.BaseViewModel
import com.yh.base.ui.ErrorLayer
import com.yh.base.ui.fragment.BaseDialogFragment
import com.yh.baseui.view.dialog.EmptyLayer

/**
 * @description  该类有默认的ActionBarLayout、YHSmartRefreshLayout
 *    可以自定义基础布局getBaseRootLayout()
 * @date: 2021/4/12 7:46 PM
 * @author: zengbobo
 */
abstract class BaseUIDialogFragment<VM : BaseViewModel, VB : ViewBinding> : BaseDialogFragment<VM, VB>() {

    val mErrorLayer: EmptyLayer by lazy {
        EmptyLayer(layoutInflater)
                .anchor(mViewBinding.root)
    }

    override fun getErrorLayer(): ErrorLayer {
        return mErrorLayer
    }
}