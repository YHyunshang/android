package com.yh.base.lib.widget.refresh

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.yh.base.lib.widget.R

/**
 * @description $
 * @date: 2021/5/19 7:17 PM
 * @author: zengbobo
 */
class CustomClassicsFooter : ClassicsFooter {
    var loadMoreSrlTextPulling: CharSequence? = ""
    var loadMoreSrlTextRelease: CharSequence? = ""
    var isWillNextType: Boolean = false//是否可以下一个type

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.YHSmartRefreshLayout)
            if (ta != null) {
                loadMoreSrlTextPulling = ta.getText(R.styleable.YHSmartRefreshLayout_loadMore_srlTextPulling)
                loadMoreSrlTextRelease = ta.getText(R.styleable.YHSmartRefreshLayout_loadMore_srlTextRelease)
                isWillNextType = ta.getBoolean(R.styleable.YHSmartRefreshLayout_isWillNextType, false)
            }
        }
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        super.onStateChanged(refreshLayout, oldState, newState)
        if (!mNoMoreData) {
            when (newState) {
                RefreshState.PullUpToLoad -> {
                    if (!TextUtils.isEmpty(loadMoreSrlTextPulling) && isWillNextType) {
                        mTitleText.text = loadMoreSrlTextPulling
                    } else {
                        mTitleText.text = mTextPulling
                    }
                }
                RefreshState.ReleaseToLoad -> {
                    if (!TextUtils.isEmpty(loadMoreSrlTextRelease) && isWillNextType) {
                        mTitleText.text = loadMoreSrlTextRelease
                    } else {
                        mTitleText.text = mTextRelease
                    }
                }
            }
        }
    }
}