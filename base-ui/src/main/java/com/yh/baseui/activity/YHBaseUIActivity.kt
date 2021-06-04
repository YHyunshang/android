package com.yh.baseui.activity

import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
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
abstract class YHBaseUIActivity<VM : BaseViewModel, VB : ViewBinding> : BaseUIActivity<VM, VB>(), View.OnClickListener {
    protected lateinit var rootLayout: ViewGroup
    protected lateinit var contentLayout: ViewGroup
    protected lateinit var actionBarLayout: ActionBarLayout
    protected var refreshLayout: YHSmartRefreshLayout? = null

    open fun getBaseRootLayout(): Int = R.layout.base_activity_linear

    override fun initView() {
        setContentView(getBaseRootLayout())
        rootLayout = findViewById(R.id.root_layout) ?: findViewById(R.id.content_layout);
        contentLayout = findViewById(R.id.content_layout) ?: rootLayout
        refreshLayout = contentLayout as? YHSmartRefreshLayout?
        refreshLayout?.apply {
            setEnableRefresh(false)
            setEnableLoadMore(false)
        }
        actionBarLayout = findViewById(R.id.actionbarLayout)
        contentLayout.addView(mViewBinding.root, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        initActionBar()
    }

    abstract fun initActionBar()

    override fun initImmersionBar() {
        if (isNeedInitImmersionBar()) {
            ImmersionBar.with(this)
                    .titleBar(actionBarLayout)
                    .statusBarColor(R.color.transparent)
                    .autoDarkModeEnable(false)
                    .statusBarDarkFont(true)
                    .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                            or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
                    .init()
        }
    }

    override fun onClick(v: View?) {
    }
}