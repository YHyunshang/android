package com.yh.base.lib.widget.refresh

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.yh.base.lib.widget.R

/**
 * @description $
 * @date: 2021/5/19 7:17 PM
 * @author: zengbobo
 */
class CustomClassicsHeader : ClassicsHeader {
    var refreshSrlTextPulling: CharSequence? = ""
    var refreshSrlTextRelease: CharSequence? = ""
    var isWillPreType: Boolean = false //是否可以下一个type
        set(value) {
            field = value
            mEnableLastTime = !value
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.YHSmartRefreshLayout)
            if (ta != null) {
                refreshSrlTextPulling = ta.getText(R.styleable.YHSmartRefreshLayout_refresh_srlTextPulling)
                refreshSrlTextRelease = ta.getText(R.styleable.YHSmartRefreshLayout_refresh_srlTextRelease)
                isWillPreType = ta.getBoolean(R.styleable.YHSmartRefreshLayout_isWillNextType, false)
            }
        }
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        super.onStateChanged(refreshLayout, oldState, newState)
        when (newState) {
            RefreshState.PullDownToRefresh -> {
                mLastUpdateText.visibility = if (mEnableLastTime) VISIBLE else GONE
                if (!TextUtils.isEmpty(refreshSrlTextPulling) && isWillPreType) {
                    mTitleText.text = refreshSrlTextPulling
                } else {
                    mTitleText.text = mTextPulling
                }
            }
            RefreshState.ReleaseToRefresh -> {
                mLastUpdateText.visibility = if (mEnableLastTime) VISIBLE else GONE
                if (!TextUtils.isEmpty(refreshSrlTextRelease) && isWillPreType) {
                    mTitleText.text = refreshSrlTextRelease
                } else {
                    mTitleText.text = mTextRelease
                }
            }
        }
    }
}