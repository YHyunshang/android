package com.yh.baseui.fragment

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.R
import androidx.viewbinding.ViewBinding
import com.yh.base.databinding.LoadingLayerBinding
import com.yh.base.ui.BaseViewModel
import com.yh.base.ui.ErrorLayer
import com.yh.base.ui.fragment.BaseFragment
import com.yh.base.ui.view.MyBlankRootLayout
import com.yh.base.utils.DisplayUtil
import com.yh.base.utils.UIUtil
import com.yh.base.utils.Util
import com.yh.baseui.view.dialog.EmptyLayer

/**
 * @description  该类有默认的ActionBarLayout、YHSmartRefreshLayout
 *    可以自定义基础布局getBaseRootLayout()
 * @date: 2021/4/12 7:46 PM
 * @author: zengbobo
 */
abstract class BaseUIFragment<VM : BaseViewModel, VB : ViewBinding> : BaseFragment<VM, VB>() {

    private val layerBlankBinding by lazy { LoadingLayerBinding.inflate(layoutInflater) }
    var isBlankShow = false

    val mErrorLayer: EmptyLayer by lazy {
        EmptyLayer(layoutInflater)
                .anchor(getErrorLayerAnchor())
    }

    open fun getErrorLayerAnchor(): View = mViewBinding.root

    override fun getErrorLayer(): ErrorLayer {
        return mErrorLayer
    }

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
                val index = anchorParent.indexOfChild(this)
                anchorParent.removeViewAt(index)
                val newView = MyBlankRootLayout(context)
                val tag = this.getTag(R.id.fragment_container_view_tag)
                if (tag != null)
                    newView.setTag(R.id.fragment_container_view_tag, tag)
                newView.addView(this, UIUtil.layoutParamsMatchParent())
                newView.addView(layerBlankBinding.root, UIUtil.layoutParamsMatchParent())
                anchorParent.addView(newView, index, layoutParams)
            } else {
                val newView = MyBlankRootLayout(context)
                newView.addView(this, UIUtil.layoutParamsMatchParent())
                newView.addView(layerBlankBinding.root, UIUtil.layoutParamsMatchParent())
                mContentView = newView
            }

            (layerBlankBinding.view.layoutParams as RelativeLayout.LayoutParams).let {
                if (marginTop != null) {
                    it.removeRule(RelativeLayout.CENTER_VERTICAL)
                    it.topMargin = DisplayUtil.dp2px(marginTop)
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
                val tag = this.getTag(R.id.fragment_container_view_tag)
                if (tag != null)
                    mAnchorView.setTag(R.id.fragment_container_view_tag, tag)
                anchorParent.addView(mAnchorView, index, layoutParams)
            }
        }
    }
}