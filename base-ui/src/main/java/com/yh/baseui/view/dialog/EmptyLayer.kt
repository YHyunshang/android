package com.yh.baseui.view.dialog

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yh.base.net.http.Code
import com.yh.base.ui.ErrorLayer
import com.yh.base.ui.livedata.Error
import com.yh.base.ui.view.MyErrorRootLayout
import com.yh.base.utils.UIUtil
import com.yh.baseui.R
import com.yh.baseui.databinding.LayerNoDataBinding

class EmptyLayer(layoutInflater: LayoutInflater) : ErrorLayer() {
    var mAnchorView: View? = null
    var mContent: CharSequence? = null
    var mButton: CharSequence? = null
    var mIcon: Int? = null
    var bg: Int? = null
    var topMargin: Int = 0
    fun content(text: CharSequence): EmptyLayer {
        this.mContent = text
        return this
    }

    fun touchable(touchable: Boolean): EmptyLayer {
        layerNoDataBinding.root.isClickable = touchable
        layerNoDataBinding.root.isFocusable = touchable
        layerNoDataBinding.root.isFocusableInTouchMode = touchable
        return this
    }

    fun bg(bg: Int): EmptyLayer {
        this.bg = bg
        layerNoDataBinding.root.setBackgroundColor(bg)
        return this
    }

    fun topMargin(topMargin: Int): EmptyLayer {
        this.topMargin = topMargin
        (layerNoDataBinding.root.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin
        return this
    }

    fun button(text: CharSequence?): EmptyLayer {
        this.mButton = text
        return this
    }

    fun icon(icon: Int): EmptyLayer {
        this.mIcon = icon
        return this
    }

    fun onClick(onClickListener: (mEmptyLayer: EmptyLayer) -> Unit): EmptyLayer {
        layerNoDataBinding.button.setOnClickListener {
            onClickListener(this)
        }
        return this
    }

    fun anchor(anchor: View): EmptyLayer {
        this.mAnchorView = anchor
        return this
    }

    val layerNoDataBinding = LayerNoDataBinding.inflate(layoutInflater)// = Reflector.invoke(getVmClazz(this), "inflate", arrayOf<Class<*>>(LayoutInflater::class.java), layoutInflater)

//    var mIndex = 0

    fun show(): EmptyLayer {
        return showInfo(true)
    }

    fun showInfo(refreshInfo: Boolean): EmptyLayer {
        if (layerNoDataBinding.root.parent == null) {
            mAnchorView?.run {
                val layoutParams = layoutParams
                parent?.let {
                    val anchorParent = it as ViewGroup
                    val index = anchorParent.indexOfChild(this)
                    anchorParent.removeViewAt(index)
                    val newView = MyErrorRootLayout(context)
                    val tag = this.getTag(R.id.fragment_container_view_tag)
                    if (tag != null)
                        newView.setTag(R.id.fragment_container_view_tag, tag)
                    newView.addView(this, UIUtil.layoutParamsMatchParent())
                    newView.addView(layerNoDataBinding.root, UIUtil.layoutParamsMatchParent(topMargin))
                    if (anchorParent is SmartRefreshLayout) {
                        anchorParent.setRefreshContent(newView, MATCH_PARENT, MATCH_PARENT)
                    } else {
                        anchorParent.addView(newView, index, layoutParams)
                    }
                }
            }
        }
        if (refreshInfo) {
            mContent?.let { layerNoDataBinding.content.text = it }
            setButton(mButton)
            mIcon?.let {
                layerNoDataBinding.icon.setImageResource(it)
            }
        }
        return this
    }

    override fun show(code: Int, msg: String, retry: () -> Unit) {
        if (code < 0) {
            when (code) {
                Error.INTERNET, Error.UNKNOWN -> {
                    layerNoDataBinding.content.text = "网络开小差了"
                    layerNoDataBinding.icon.setImageResource(R.drawable.ic_nodata_internet)
                }
                Error.SERVER -> {
                    layerNoDataBinding.content.text = "服务器错误"
                    layerNoDataBinding.icon.setImageResource(R.drawable.ic_nodata_server)
                }
            }
            setButton("重试")
            onClick {
                retry()
            }
            showInfo(false)
        } else if (code != Code.SUCCESS) {
            layerNoDataBinding.content.text = msg
            if (mIcon != null) {
                layerNoDataBinding.icon.setImageResource(mIcon!!)
            } else {
                layerNoDataBinding.icon.setImageResource(R.drawable.ic_nodata_server)
            }
            setButton("重试")
            onClick {
                retry()
            }
            showInfo(false)
        } else {
            mContent?.let { layerNoDataBinding.content.text = it }
            setButton(mButton)
            mIcon?.let {
                layerNoDataBinding.icon.setImageResource(it)
            }
        }
    }

    override fun hide() {
        layerNoDataBinding.root.parent?.run {
            val mAnchorView = (this as ViewGroup).getChildAt(0) as View
            this.removeView(layerNoDataBinding.root)
            this.removeView(mAnchorView)
            val layoutParams = this.layoutParams
            val anchorParent = this.parent as ViewGroup?
            if (anchorParent != null) {
                val index = anchorParent.indexOfChild(this)
                anchorParent.removeViewAt(index)
                if (anchorParent is SmartRefreshLayout) {
                    anchorParent.setRefreshContent(mAnchorView, MATCH_PARENT, MATCH_PARENT)
                } else {
                    anchorParent.addView(mAnchorView, index, layoutParams)
                }
            }
        }
    }

    private fun setButton(text: CharSequence?): EmptyLayer {
        if (TextUtils.isEmpty(text)) {
            layerNoDataBinding.button.visibility = View.GONE
        } else {
            layerNoDataBinding.button.text = text
            layerNoDataBinding.button.visibility = View.VISIBLE
        }
        return this
    }

}