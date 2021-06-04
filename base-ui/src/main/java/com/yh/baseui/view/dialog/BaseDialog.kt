package com.yh.baseui.view.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.*
import androidx.viewbinding.ViewBinding
import com.yh.base.ui.view.MyFragmentRootLayout
import com.yh.base.ui.view.PopProgress
import com.yh.base.utils.UIUtil
import com.yh.baseui.R

/**
 * @description 基类dialog
 * @date: 2021/4/17 10:33 AM
 * @author: guowanxin
 */
abstract class BaseDialog<VB : ViewBinding>(context: Context) : Dialog(context,  R.style.dialogFullScreen) {
    protected var mContext: Context = context
    protected var mInflater: LayoutInflater = LayoutInflater.from(context)

    var mViewBinding: VB
    protected var confirmInter: ConfirmInter<Any?>? = null

    var mPopProgress: PopProgress

    init {
        mViewBinding = getViewBinding()

        val layout = MyFragmentRootLayout(mContext)
        layout.addView(getAppContentView(), UIUtil.layoutParamsMatchParent())
        mPopProgress = PopProgress(mContext as Activity, layout)

//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window!!.setBackgroundDrawableResource(android.R.color.transparent)
//        window!!.setWindowAnimations(R.style.Theme_Dialog)
//        setContentView(mViewBinding.root)
        setContentView(layout)

//        setCenter()
        setCancelable(true)
        setCanceledOnTouchOutside(true)
//        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        initListener()
    }

    abstract fun initListener()

    abstract fun getViewBinding(): VB

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    fun setConfirmInter(confirmInter: ConfirmInter<Any?>): BaseDialog<VB> {
        this.confirmInter = confirmInter
        return this
    }

    open fun setCenter() {
        val window = window
        window!!.setGravity(Gravity.CENTER)
//        window.setWindowAnimations(R.style.Theme_Dialog)
        val lp = window.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
    }

    open fun setBottom() {
        val window = window
        window!!.setGravity(Gravity.BOTTOM)
//        window.setWindowAnimations(R.style.Theme_Dialog)
        val lp = window.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
    }

    interface ConfirmInter<T> {
        fun confirm(t: T?)
    }

    open fun getAppContentView(): View = mViewBinding.root
}