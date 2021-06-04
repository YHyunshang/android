package com.yh

import com.alibaba.android.arouter.facade.annotation.Route
import com.yh.base.ui.toast.ToastUtil
import com.yh.base.utils.GsonUtil
import com.yh.baseui.activity.BaseUIActivity
import com.yh.tobtrading.databinding.ActivityMainBinding
import com.yh.trading.constant.ARouterPathManager
import com.yh.viewmodel.MainViewModel


@Route(path = ARouterPathManager.ActivityMain)
class MainActivity : BaseUIActivity<MainViewModel, ActivityMainBinding>() {

    override fun initListener() {
        mViewBinding.testNet.setOnClickListener {
            mViewModel.getTels()
        }
    }

    override fun lifecycleObserver() {
        mViewModel.telsLiveData1.observe(this, {
            ToastUtil.showShortMsg("成功:" + GsonUtil.toJson(it))
        })
    }

    override fun loadData() {
    }
}