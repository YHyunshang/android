package com.yh.baseui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.yh.base.ui.BaseViewModel
import com.yh.baseui.R
import com.yh.base.lib.widget.ActionBarLayout
import com.yh.base.lib.widget.YHSmartRefreshLayout

/**
 * @description  该类有默认的ActionBarLayout、YHSmartRefreshLayout
 *    可以自定义基础布局getBaseRootLayout()
 * @date: 2021/4/12 7:46 PM
 * @author: zengbobo
 */
abstract class YHBaseUIFragment<VM : BaseViewModel, VB : ViewBinding> : BaseUIFragment<VM, VB>() {
    protected lateinit var contentLayout: ViewGroup
    protected lateinit var actionBarLayout: ActionBarLayout
    protected var refreshLayout: YHSmartRefreshLayout? = null


    open fun getBaseRootLayout(): Int = R.layout.base_fragment_linear

    override fun getAppContentView(inflater: LayoutInflater): View {
        val view = inflater.inflate(getBaseRootLayout(), null)
        contentLayout = view.findViewById<ViewGroup>(R.id.content_layout)
                ?: (view as ViewGroup)
        refreshLayout = view as? YHSmartRefreshLayout?
        refreshLayout?.apply {
            setEnableRefresh(false)
            setEnableLoadMore(false)
        }
        actionBarLayout = view.findViewById(R.id.actionbarLayout)
        contentLayout.addView(mViewBinding.root, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        return view
    }

    override fun initView() {
        super.initView()
        initActionBar()
    }

    abstract fun initActionBar()

}