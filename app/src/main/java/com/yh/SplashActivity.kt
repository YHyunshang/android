package com.yh

import com.yh.base.ui.BaseViewModel
import com.yh.baseui.activity.BaseUIActivity
import com.yh.tobtrading.databinding.ActivitySplashBinding
import com.yh.trading.constant.ARouterIntentManager
import com.yh.trading.constant.ARouterPathManager

/**
 * @description 闪屏页面
 * @date: 2021/5/22 3:20 PM
 * @author: zhangzhiyuan
 */
class SplashActivity : BaseUIActivity<BaseViewModel, ActivitySplashBinding>() {
    override fun initListener() {
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(0, 0)
    }

    override fun lifecycleObserver() {
    }

    override fun loadData() {
        ARouterIntentManager.navigation(ARouterPathManager.ActivityMain)
    }
}