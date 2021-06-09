package com.yh.base.lib.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ViewGroup
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnMultiListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @description
 * @time 2021/4/9 4:54 PM
 * @author zengbobo
 */
class YHSmartRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SmartRefreshLayout(context, attrs) {
    companion object {
        init {
            //ClassicsHeader
            ClassicsHeader.REFRESH_HEADER_PULLING = "下拉可以刷新";
            ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在刷新...";
            ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载...";
            ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新";
            ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成";
            ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";
            ClassicsHeader.REFRESH_HEADER_SECONDARY = "释放进入二楼";
            ClassicsHeader.REFRESH_HEADER_UPDATE = "上次更新 M-d HH:mm";
            //ClassicsFooter
            ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉加载更多"
            ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载"
            ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在刷新..."
            ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载..."
            ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成"
            ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败"
            ClassicsFooter.REFRESH_FOOTER_NOTHING = "没有更多数据了"
        }
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.YHSmartRefreshLayout)
        if (ta != null) {
            val refresh_srlTextPulling = ta.getText(R.styleable.YHSmartRefreshLayout_refresh_srlTextPulling)?.toString()
            val refresh_srlTextRelease = ta.getText(R.styleable.YHSmartRefreshLayout_refresh_srlTextRelease)?.toString()
            val loadMore_srlTextPulling = ta.getText(R.styleable.YHSmartRefreshLayout_loadMore_srlTextPulling)?.toString()
            val loadMore_srlTextRelease = ta.getText(R.styleable.YHSmartRefreshLayout_loadMore_srlTextRelease)?.toString()
            if (TextUtils.isEmpty(refresh_srlTextPulling)) {
                ClassicsHeader.REFRESH_HEADER_PULLING = "下拉可以刷新"
            } else {
                ClassicsHeader.REFRESH_HEADER_PULLING = refresh_srlTextPulling
            }
            if (TextUtils.isEmpty(refresh_srlTextRelease)) {
                ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新"
            } else {
                ClassicsHeader.REFRESH_HEADER_RELEASE = refresh_srlTextRelease
            }
            if (TextUtils.isEmpty(loadMore_srlTextPulling)) {
                ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉加载更多"
            } else {
                ClassicsFooter.REFRESH_FOOTER_PULLING = loadMore_srlTextPulling
            }
            if (TextUtils.isEmpty(loadMore_srlTextRelease)) {
                ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载"
            } else {
                ClassicsFooter.REFRESH_FOOTER_RELEASE = loadMore_srlTextRelease
            }
        }

        val header = ClassicsHeader(context, attrs)
        header.setEnableLastTime(false)
        addView(header, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(60f)))
        val footer = ClassicsFooter(context, attrs)
        addView(footer, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(60f)))
        mNestedChild.isNestedScrollingEnabled = true
    }

    @Deprecated("使用我们自己的方法",
            ReplaceWith("setOnRefreshMoreListener(listener)",
                    "com.yh.base.lib.widget.YHSmartRefreshLayout.OnRefreshMoreListener"))
    override fun setOnRefreshLoadMoreListener(listener: OnRefreshLoadMoreListener?): RefreshLayout {
        return super.setOnRefreshLoadMoreListener(listener)
    }

    @Deprecated("使用我们自己的方法",
            ReplaceWith("setOnRefreshMoreListener(listener)",
                    "com.yh.base.lib.widget.YHSmartRefreshLayout.OnRefreshMoreListener"))
    override fun setOnLoadMoreListener(listener: OnLoadMoreListener?): RefreshLayout {
        return super.setOnLoadMoreListener(listener)
    }

    @Deprecated("使用我们自己的方法",
            ReplaceWith("setOnRefreshMoreListener(listener)",
                    "com.yh.base.lib.widget.YHSmartRefreshLayout.OnRefreshMoreListener"))
    override fun setOnMultiListener(listener: OnMultiListener?): RefreshLayout {
        return super.setOnMultiListener(listener)
    }

    fun setOnRefreshMoreListener(listener: OnRefreshMoreListener?): RefreshLayout? {
        if (listener == null) {
            return super.setOnRefreshLoadMoreListener(null)
        }
        return super.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                listener.onLoadMore(refreshLayout as YHSmartRefreshLayout)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                listener.onRefresh(refreshLayout as YHSmartRefreshLayout)
            }
        })
    }

    fun dp2px(dipValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    interface OnRefreshMoreListener {
        fun onLoadMore(refreshLayout: YHSmartRefreshLayout?)

        fun onRefresh(refreshLayout: YHSmartRefreshLayout?)
    }


    //    private val mScrollOffset = IntArray(2)
//    private val mScrollConsumed = IntArray(2)
//    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
//        val action = MotionEventCompat.getActionMasked(e)
//        when (action) {
//            MotionEvent.ACTION_DOWN -> {
//                mLastTouchX = (e.x + 0.5f)
//                mLastTouchY = (e.y + 0.5f)
//                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
//            }
//            MotionEvent.ACTION_MOVE -> {
//                val x = (e.x + 0.5f).toInt()
//                val y = (e.y + 0.5f).toInt()
//                val dx = (mLastTouchX - x).toInt()
//                val dy = (mLastTouchY - y).toInt()
//                dispatchNestedPreScroll(dx, dy, mScrollConsumed, mScrollOffset)
//            }
//        }
//        return super.dispatchTouchEvent(e)
//    }
//
//    override fun startNestedScroll(axes: Int): Boolean {
//        return mNestedChild.startNestedScroll(axes)
//    }
//
//    override fun stopNestedScroll() {
//        mNestedChild.stopNestedScroll()
//    }
//
//    override fun hasNestedScrollingParent(): Boolean {
//        return mNestedChild.hasNestedScrollingParent()
//    }
//
//    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
//                                      dyUnconsumed: Int, offsetInWindow: IntArray?): Boolean {
//        return mNestedChild.dispatchNestedScroll(dxConsumed, dyConsumed,
//                dxUnconsumed, dyUnconsumed, offsetInWindow)
//    }
//
//    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
//        return mNestedChild.dispatchNestedPreScroll(
//                dx, dy, consumed, offsetInWindow)
//    }
//
//    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
//        return mNestedChild.dispatchNestedFling(velocityX, velocityY, consumed)
//    }
//
//    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
//        return mNestedChild.dispatchNestedPreFling(velocityX, velocityY)
//    }
}