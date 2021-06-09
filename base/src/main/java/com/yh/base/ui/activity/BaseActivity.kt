package com.yh.base.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.yh.base.R
import com.yh.base.databinding.LoadingLayerBinding
import com.yh.base.ui.BaseViewModel
import com.yh.base.ui.BaseViewModel.Companion.FLAG_DEFAULT
import com.yh.base.ui.BaseViewModel.Companion.FLAG_ERROR_LAYER
import com.yh.base.ui.BaseViewModel.Companion.FLAG_LOADING
import com.yh.base.ui.BaseViewModel.Companion.FLAG_NO_TOAST
import com.yh.base.ui.ErrorLayer
import com.yh.base.ui.IDelegateUI
import com.yh.base.ui.ext.getVbClazz
import com.yh.base.ui.ext.getVmClazz
import com.yh.base.ui.toast.ToastUtil
import com.yh.base.ui.view.MyBlankRootLayout
import com.yh.base.ui.view.PopProgress
import com.yh.base.utils.EventBusUtils
import com.yh.base.utils.LogUtils
import com.yh.base.utils.Reflector
import com.yh.base.utils.UIUtil

/**
 * @description 基础Activity封装类
 * @date: 2021/4/6 4:41 PM
 * @author: guowanxin
 */
abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity(), IDelegateUI {

    protected lateinit var mViewModel: VM
    lateinit var mViewBinding: VB

    private lateinit var mPopProgress: PopProgress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
        ensureInit()
        initView()
        initImmersionBar()
        initListener()
        observer(true)
        lifecycleObserver()
        loadData()
    }

    private var isFirst = true

    override fun onResume() {
        super.onResume()
        if (!isFirst) {
            isFirst = false
            reloadData()
        }
    }

    override fun reloadData() {
    }

    override fun ensureInit() {
        val startTime = System.currentTimeMillis()
        createViewBinding()
        LogUtils.tag("monitor-tob").v("BaseActivity $this createViewBinding=" + (System.currentTimeMillis() - startTime))
        mViewModel = createViewModel()
        mPopProgress = PopProgress(this)
        mViewModel.requestLiveData.observeForever {
            if (it.hasFlag(FLAG_DEFAULT)) {//request
                if (it.hasFlag(FLAG_LOADING)) {
                    showLoadingDialog()
                }
            } else {//response
                if (it.hasFlag(FLAG_LOADING)) {
                    hideLoadingDialog()
                }
                if (it.error != null) {//失败
                    it.error.run {
                        if (!it.hasFlag(FLAG_NO_TOAST)) {
                            ToastUtil.showLongMsg(this.msg)
                        }
                        if (it.hasFlag(FLAG_ERROR_LAYER)) {
                            getErrorLayer().show(this.code, this.msg, it.retry!!)
                        }
                    }
                } else {//成功
                    if (it.hasFlag(FLAG_ERROR_LAYER)) {
                        getErrorLayer().hide()
                    }
                }
            }
        }
    }


    private val layerBlankBinding by lazy { LoadingLayerBinding.inflate(layoutInflater) }

    fun showBlankLayer() {
        if (layerBlankBinding.root.parent != null) return
        val mAnchorView = mViewBinding.root
        mAnchorView.run {
            val layoutParams = layoutParams
            parent?.let {
                val anchorParent = it as ViewGroup
                val index = anchorParent.indexOfChild(this)
                anchorParent.removeViewAt(index)
                val newView = MyBlankRootLayout(context)
                val tag = this.getTag(androidx.fragment.R.id.fragment_container_view_tag)
                if (tag != null)
                    newView.setTag(androidx.fragment.R.id.fragment_container_view_tag, tag)
                newView.addView(this, UIUtil.layoutParamsMatchParent())
                newView.addView(layerBlankBinding.root, UIUtil.layoutParamsMatchParent())
                anchorParent.addView(newView, index, layoutParams)
            }
        }
    }

    fun hideBlankLayer() {
        layerBlankBinding.root.parent?.run {
            val mAnchorView = (this as ViewGroup).getChildAt(0) as View
            this.removeView(layerBlankBinding.root)
            this.removeView(mAnchorView)
            val layoutParams = this.layoutParams
            val anchorParent = this.parent as ViewGroup
            val index = anchorParent.indexOfChild(this)
            anchorParent.removeViewAt(index)
            anchorParent.addView(mAnchorView, index, layoutParams)
        }
    }

    abstract fun getErrorLayer(): ErrorLayer

    fun post(runnable: Runnable) {
        window.decorView.post(runnable)
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    fun createViewBinding() {
        mViewBinding = Reflector.invoke(getVbClazz(this), "inflate", arrayOf<Class<*>>(LayoutInflater::class.java), LayoutInflater.from(this))
    }

    override fun initView() {
        setContentView(mViewBinding.root)
    }

    open fun isNeedInitImmersionBar(): Boolean = true

    override fun initImmersionBar() {
        if (isNeedInitImmersionBar()) {
            ImmersionBar.with(this)
                    .statusBarColor(R.color.color_status_action_bar)
                    .fitsSystemWindows(true)
                    .autoDarkModeEnable(false)
                    .statusBarDarkFont(true)
                    .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                            or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
                    .init()
        }
    }

    fun showLoadingDialog() {
        mPopProgress.show()
    }

    fun hideLoadingDialog() {
        mPopProgress.hide()
    }

    override fun observer(isRegister: Boolean) {
        if (isNeedRegister()) {
            if (isRegister) {
                EventBusUtils.register(this)
            } else {
                EventBusUtils.unregister(this)
            }
        }
    }

    override fun onDestroy() {
        mPopProgress.close()
        super.onDestroy()
        observer(false)
    }

    /**
     * 是否需要注册EventBus  默认不需要  true：需要   false：不需要
     */
    open fun isNeedRegister(): Boolean {
        return false
    }

    /**
     * 初始化类相关的工作，如注入等
     */
    override fun initParams() {
        ARouter.getInstance().inject(this)
    }

}