package com.yh.base.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.yh.base.constant.LoadingCode
import com.yh.base.ui.BaseViewModel
import com.yh.base.ui.BaseViewModel.Companion.FLAG_ERROR_LAYER
import com.yh.base.ui.BaseViewModel.Companion.FLAG_LOADING
import com.yh.base.ui.BaseViewModel.Companion.FLAG_NO_TOAST
import com.yh.base.ui.ErrorLayer
import com.yh.base.ui.IDelegateUI
import com.yh.base.ui.ext.getVbClazz
import com.yh.base.ui.ext.getVmClazz
import com.yh.base.ui.toast.ToastUtil
import com.yh.base.ui.view.MyErrorRootLayout
import com.yh.base.ui.view.MyFragmentRootLayout
import com.yh.base.ui.view.PopProgress
import com.yh.base.utils.EventBusUtils
import com.yh.base.utils.LogUtils
import com.yh.base.utils.Reflector
import com.yh.base.utils.UIUtil

/**
 * @description 基础Fragment封装类
 * @date: 2021/4/6 6:23 PM
 * @author: guowanxin
 */
abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment(), IDelegateUI {
    lateinit var mViewModel: VM
    lateinit var mViewBinding: VB

    //是否第一次加载
//    private var isFirst: Boolean = true

    protected var mContentView: View? = null
    lateinit var mActivity: AppCompatActivity
    lateinit var mPopProgress: PopProgress

    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("Lifecycle BaseFragment onAttach $isFirst $this $mContentView")
        mActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Lifecycle BaseFragment onCreate $isFirst $this $mContentView")
    }

    override fun ensureInit() {
        val startTime = System.currentTimeMillis()
        createViewBinding(layoutInflater)
        LogUtils.tag("monitor-tob").v("BaseFragment $isFirst $this createViewBinding=" + (System.currentTimeMillis() - startTime))
        mViewModel = createViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        println("Lifecycle BaseFragment onCreateView $isFirst $this $mContentView")
        if (mContentView == null) {
            ensureInit()
            var layout = MyFragmentRootLayout(requireActivity())
            layout.addView(getAppContentView(inflater), UIUtil.layoutParamsMatchParent())
            mContentView = layout
            mPopProgress = PopProgress(activity, layout)
            mViewModel.requestLiveData.observe(viewLifecycleOwner, {
                if (it.hasFlag(LoadingCode.DEFAULT_FLAG)) {//request
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
            })
            initParams()
            if (!isLazyLoad()) {
                initView()
                initImmersionBar()
                initListener()
                observer(true)
                lifecycleObserver()
                loadData()
            }
            return mContentView!!
        } else {
            var contentView = mContentView
            var parent = contentView?.parent as ViewGroup?
//            if (parent != null && parent is FrameLayout) {
//                val tag = parent.getTag(R.id.fragment_container_view_tag)
//                if (tag != null) {
//                    contentView = parent
//                    parent = parent.parent as ViewGroup?
//                }
//            }
            while (parent != null && (parent is MyErrorRootLayout)) {
                contentView = parent
                parent = parent.parent as ViewGroup?
            }
            parent?.removeView(contentView)
            if (!isLazyLoad()) {
                initImmersionBar()
                lifecycleObserver()
                reloadData()
            }
            return contentView!!
        }
    }

    abstract fun getErrorLayer(): ErrorLayer

    open fun getAppContentView(inflater: LayoutInflater): View = mViewBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("Lifecycle BaseFragment onViewCreated $isFirst $this")
//        isViewCreated = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("Lifecycle BaseFragment onActivityCreated $isFirst $this")
//        initImmersionBar()
//        onExecPendingActions()
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
        mViewBinding = Reflector.invoke(
                getVbClazz(this),
                "inflate",
                arrayOf<Class<*>>(LayoutInflater::class.java),
                inflater)
    }

    open fun isLazyLoad(): Boolean {
        return false
    }

    private var isFirst = true

    override fun onHiddenChanged(hidden: Boolean) {
        println("Lifecycle BaseFragment onHiddenChanged ${hidden} $this")
        if (hidden) {
            pauseData()
        } else {
            if (isLazyLoad()) {
                if (isFirst) {
                    isFirst = false
                    initView()
                    initImmersionBar()
                    initListener()
                    observer(true)
                    lifecycleObserver()
                    loadData()
                } else {
                    initImmersionBar()
//                    lifecycleObserver()
                    reloadData()
                }
            } else {
                initImmersionBar()
                reloadData()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        println("Lifecycle BaseFragment onPause $this")
        pauseData()
    }

    override fun onResume() {
        super.onResume()
        println("Lifecycle BaseFragment onResume $this")
        if (!isVisible) return
//        onVisible()
        if (isLazyLoad()) {
            if (isFirst) {
                isFirst = false
                initView()
                initListener()
                observer(true)
                lifecycleObserver()
                loadData()
            } else {
//                    lifecycleObserver()
                reloadData()
            }
        } else {
            if (isFirst) {
                isFirst = false
            } else {
                reloadData()
            }
        }
    }

//    private val mPendingAction: MutableList<() -> Unit> by lazy { mutableListOf<() -> Unit>() }
//
//    var isViewCreated = false
//    fun execPendingAction(action: () -> Unit) {
//        if (this.isViewCreated) {
//            action()
//        } else {
//            mPendingAction.add(action)
//        }
//    }

//    private fun onExecPendingActions() {
//        for (action in mPendingAction) {
//            action()
//        }
//        mPendingAction.clear()
//    }

    override fun reloadData() {
    }

    open fun pauseData() {
    }


    fun showLoadingDialog() {
//        var contentView: View = mViewBinding.root
//        var parent: ViewGroup? = contentView.parent as ViewGroup?
//        while (parent != null) {
//            if (parent is FragmentContainerView) {
//                val tag = parent.getTag(R.id.fragment_container_view_tag)
//                if (tag != null) {
////                    newView.setTag(R.id.fragment_container_view_tag, tag)
//                    mPopProgress.setParentView(parent, tag)
//                    break
//                }
//            }
//            contentView = parent
//            parent = parent.parent as ViewGroup?
//        }
        mPopProgress.show()
    }

    fun hideLoadingDialog() {
        mPopProgress.hide()
    }

    //    /**
//     * 是否需要懒加载
//     */
//    private fun onVisible() {
//        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
//            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
//            mContentView?.postDelayed({
//                loadData(isFirst)
//                isFirst = false
//            }, lazyLoadTime())
//        }
//    }
//
//    /**
//     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
//     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
//     * 不传默认 300毫秒
//     * @return Long
//     */
//    open fun lazyLoadTime(): Long {
//        return 300
//    }


    override fun onStop() {
        super.onStop()
        println("Lifecycle BaseFragment onStop $this")
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        isViewCreated = false
        println("Lifecycle BaseFragment onDestroyView $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        mPopProgress.close()
        println("Lifecycle BaseFragment onDestroy $this")
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

    private fun println(string: String) {
        LogUtils.d(string)
    }
}