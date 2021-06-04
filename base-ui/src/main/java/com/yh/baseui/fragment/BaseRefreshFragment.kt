package com.yh.baseui.fragment

import androidx.viewbinding.ViewBinding
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.yh.base.lib.adapter.BaseAdapter
import com.yh.base.net.bean.Page
import com.yh.base.ui.BaseViewModel
import com.yh.base.ui.recyclerView.RecyclerAdapter
import com.yh.base.lib.widget.YHSmartRefreshLayout

/**
 * @description 下拉刷新上拉加载Fragment
 * @date: 2021/4/25 4:55 PM
 * @author: guowanxin
 */
abstract class BaseRefreshFragment<VM : BaseViewModel, VB : ViewBinding> : BaseUIFragment<VM, VB>(), YHSmartRefreshLayout.OnRefreshMoreListener {

    /**
     * 当前页数
     */
    protected var mPage = 1

    override fun initView() {
        super.initView()
        getRefreshLayout().apply {
            setEnableRefresh(isEnableRefresh())
            setEnableLoadMore(isEnableLoad())
            setOnRefreshMoreListener(this@BaseRefreshFragment)
        }
    }

    abstract fun getRefreshLayout(): YHSmartRefreshLayout

    open fun isEnableRefresh(): Boolean {
        return true
    }

    open fun isEnableLoad(): Boolean {
        return true
    }

    /**
     * 下拉刷新上拉加载请重写该方法
     */
    abstract fun loadListData(pageNo: Int)

    /**
     * 该方法只能用于带dialog的加载数据
     */
    override fun loadData() {
        mPage = 1
    }

    override fun onRefresh(refreshLayout: YHSmartRefreshLayout?) {
        getRefreshLayout().apply {
            if (state == RefreshState.Refreshing) {
                mPage = 1
                loadListData(mPage)
            } else {
                finishRefresh()
                setNoMoreData(false)
            }
        }

    }

    override fun onLoadMore(refreshLayout: YHSmartRefreshLayout?) {
        getRefreshLayout().apply {
            if (state == RefreshState.Loading) {
                mPage++
                loadListData(mPage)
            } else {
                finishLoadMoreWithNoMoreData()
            }
        }
    }

    fun <T, B : ViewBinding> initData(page: Page<T>?, baseAdapter: BaseAdapter<T, B, Any>) {
        if ((page != null && page.pageNo == 1) || mPage == 1) {
            mPage = 1
            if (page == null || page.result == null || page.result.size == 0) {
                showEmptyView()
            } else {
                mErrorLayer.hide()
                baseAdapter.data.clear()
                baseAdapter.data.addAll(page.result)
                baseAdapter.notifyDataSetChanged()
            }
            finishRefresh()
        } else {
            page?.let {
                if (it.pageNo > 0) {
                    mPage = it.pageNo
                }
            }

            if (page == null || page.result == null || page.result.size == 0) {
                finishLoadMoreWithNoMoreData()
            } else {
                val size = page.result.size
                baseAdapter.data.addAll(page.result)
                baseAdapter.notifyItemRangeChanged(baseAdapter.data.size - size, baseAdapter.data.size)
            }
            finishLoad()
        }

    }

    @JvmName("RecyclerAdapter")
    fun <T, B : ViewBinding> initData(page: Page<T>?, baseAdapter: RecyclerAdapter<T, B>) {
        if ((page != null && page.pageNo == 1) || mPage == 1) {
            mPage = 1
            if (page == null || page.result == null || page.result.size == 0) {
                showEmptyView()
            } else {
                mErrorLayer.hide()
                baseAdapter.list.clear()
                baseAdapter.list.addAll(page.result)
                baseAdapter.notifyDataSetChanged()
            }
            finishRefresh()
        } else {
            page?.let {
                if (it.pageNo > 0) {
                    mPage = it.pageNo
                }
            }
            if (page == null || page.result == null || page.result.size == 0) {
                finishLoadMoreWithNoMoreData()
            } else {
                val size = page.result.size
                baseAdapter.list.addAll(page.result)
                baseAdapter.notifyItemRangeChanged(baseAdapter.list.size - size, baseAdapter.list.size)
            }
            finishLoad()
        }
    }

    /**
     * 请求失败调用
     */
    fun finishRefreshAndLoad() {
        if (mPage == 1) {
            finishRefresh()
        } else {
            mPage--
            finishLoad()
        }
    }

    fun finishRefresh() {
        getRefreshLayout().apply {
            if (state == RefreshState.Refreshing) {
                finishRefresh()
            }
            setNoMoreData(false)
        }
    }

    fun finishLoad() {
        getRefreshLayout().apply {
            if (state == RefreshState.Loading) {
                finishLoadMore()
            }
        }
    }

    fun finishLoadMoreWithNoMoreData() {
        getRefreshLayout().finishLoadMoreWithNoMoreData()
    }

    /**
     * content("没有订单").button("随便逛逛").icon(R.drawable.ic_nodata_order).onClick {}.show()
     */
    open fun showEmptyView() {}

}