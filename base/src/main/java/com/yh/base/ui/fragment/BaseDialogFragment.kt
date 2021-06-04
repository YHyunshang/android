package com.yh.base.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.yh.base.R
import com.yh.base.constant.LoadingCode
import com.yh.base.databinding.LoadingLayerBinding
import com.yh.base.ui.BaseViewModel
import com.yh.base.ui.ErrorLayer
import com.yh.base.ui.IDelegateUI
import com.yh.base.ui.ext.getVbClazz
import com.yh.base.ui.ext.getVmClazz
import com.yh.base.ui.toast.ToastUtil
import com.yh.base.ui.view.MyBlankRootLayout
import com.yh.base.ui.view.MyFragmentRootLayout
import com.yh.base.ui.view.PopProgress
import com.yh.base.utils.DisplayUtil.dp2px
import com.yh.base.utils.EventBusUtils
import com.yh.base.utils.Reflector
import com.yh.base.utils.UIUtil
import com.yh.base.utils.Util

/**
 * @description 基础Fragment封装类
 * @date: 2021/4/6 6:23 PM
 * @author: guowanxin
 */
abstract class BaseDialogFragment<VM : BaseViewModel, VB : ViewBinding> : DialogFragment(), IDelegateUI {
    lateinit var mViewModel: VM
    lateinit var mViewBinding: VB

    //是否第一次加载
//    private var isFirst: Boolean = true

    protected var mContentView: View? = null
    lateinit var mActivity: AppCompatActivity
    lateinit var mPopProgress: PopProgress

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, getCustomTheme())
        initParams()
    }

    @StyleRes
   open fun getCustomTheme(): Int =  R.style.dialogFullScreen

    override fun ensureInit() {
        createViewBinding(layoutInflater)
        mViewModel = createViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (mContentView == null) {
            ensureInit()
            var layout = MyFragmentRootLayout(requireActivity())
            layout.addView(getAppContentView(inflater), UIUtil.layoutParamsMatchParent())
            mContentView = layout
            mPopProgress = PopProgress(activity, layout)
            mViewModel.requestLiveData.observe(requireActivity(), {
                if (it.hasFlag(LoadingCode.DEFAULT_FLAG)) {//request
                    if (it.hasFlag(BaseViewModel.FLAG_LOADING)) {
                        execPendingAction { showLoadingDialog() }
                    }
                } else {//response
                    if (it.hasFlag(BaseViewModel.FLAG_LOADING)) {
                        execPendingAction { hideLoadingDialog() }
                    }
                    if (it.error != null) {//失败
                        it.error.run {
                            if (!it.hasFlag(BaseViewModel.FLAG_NO_TOAST)) {
                                execPendingAction { ToastUtil.showLongMsg(this.msg) }
                            }
                            if (it.hasFlag(BaseViewModel.FLAG_ERROR_LAYER)) {
                                execPendingAction { getErrorLayer().show(this.code, this.msg, it.retry!!) }
                            }
                        }
                    } else {//成功
                        if (it.hasFlag(BaseViewModel.FLAG_ERROR_LAYER)) {
                            execPendingAction { getErrorLayer().hide() }
                        }
                    }
                }
            })
            initParams()
            initView()
            initImmersionBar()
            initListener()
            observer(true)
            lifecycleObserver()
            loadData()
        } else {
            val parent = mContentView?.parent as ViewGroup?
            parent?.removeView(mContentView)
            loadData()
        }
        return mContentView!!
    }
    private val layerBlankBinding by lazy { LoadingLayerBinding.inflate(layoutInflater) }

    var isBlankShow = false

    fun showBlankLayer(anchor: View? = null, marginTop: Float? = null) {
        if (isBlankShow) return
        isBlankShow = true
        if (layerBlankBinding.root.parent != null) return
        var mAnchorView = anchor
        if (mAnchorView == null) {
            mAnchorView = mViewBinding.root
        }
        mAnchorView.run {
            val layoutParams = layoutParams
            if (parent != null) {
                val anchorParent = parent as ViewGroup
                anchorParent.removeView(this)
                val newView = MyBlankRootLayout(context)
                val tag = this.getTag(androidx.fragment.R.id.fragment_container_view_tag)
                if (tag != null)
                    newView.setTag(androidx.fragment.R.id.fragment_container_view_tag, tag)
                newView.addView(this, UIUtil.layoutParamsMatchParent())
                newView.addView(layerBlankBinding.root, UIUtil.layoutParamsMatchParent())
                anchorParent.addView(newView, layoutParams)
            } else {
                val newView = MyBlankRootLayout(context)
                newView.addView(this, UIUtil.layoutParamsMatchParent())
                newView.addView(layerBlankBinding.root, UIUtil.layoutParamsMatchParent())
                mContentView = newView
            }

            (layerBlankBinding.view.layoutParams as RelativeLayout.LayoutParams).let {
                if (marginTop != null) {
                    it.removeRule(RelativeLayout.CENTER_VERTICAL)
                    it.topMargin = dp2px(marginTop)
                } else {
                    it.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
                }
                layerBlankBinding.view.layoutParams = it
            }
        }
    }

    open fun hideBlankLayer() {
        layerBlankBinding.root.parent?.run {
            val mAnchorView = (this as ViewGroup).getChildAt(0) as View
            this.removeView(layerBlankBinding.root)
            this.removeView(mAnchorView)
            if (mContentView == this) {
                mContentView = mAnchorView
            }
            val layoutParams = this.layoutParams
            val anchorParent = this.parent as ViewGroup?
            if (anchorParent != null) {
                val index = anchorParent.indexOfChild(this)
                anchorParent.removeViewAt(index)
                val tag = this.getTag(androidx.fragment.R.id.fragment_container_view_tag)
                if (tag != null)
                    mAnchorView.setTag(androidx.fragment.R.id.fragment_container_view_tag, tag)
                anchorParent.addView(mAnchorView, index, layoutParams)
            }
        }
    }

    abstract fun getErrorLayer(): ErrorLayer

    open fun getAppContentView(inflater: LayoutInflater): View = mViewBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onExecPendingActions()
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        isFirst = true
//    }


    override fun initView() {
    }

    override fun initImmersionBar() {
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    private fun createViewBinding(inflater: LayoutInflater) {
        mViewBinding = Reflector.invoke(getVbClazz(this), "inflate", arrayOf<Class<*>>(LayoutInflater::class.java), inflater)
    }

    private val mPendingAction: MutableList<() -> Unit> by lazy { mutableListOf<() -> Unit>() }

    var isViewCreated = false
    fun execPendingAction(action: () -> Unit) {
        if (this.isViewCreated) {
            action()
        } else {
            mPendingAction.add(action)
        }
    }

    private fun onExecPendingActions() {
        for (action in mPendingAction) {
            action()
        }
        mPendingAction.clear()
    }

    fun showLoadingDialog() {
        mPopProgress.show()
    }

    fun hideLoadingDialog() {
        mPopProgress.hide()
    }

    override fun onDestroyView() {
        isViewCreated = false
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPopProgress.close()
        observer(false)
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

    /**
     * 是否需要注册EventBus  默认不需要  true：需要   false：不需要
     */
    open fun isNeedRegister(): Boolean {
        return false
    }

    override fun initParams() {
        ARouter.getInstance().inject(this)
    }

}