package com.performance.recyclerviewadapter.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.performance.recyclerviewadapter.adapter.MyMultiItemTypeAdapter
import com.performance.recyclerviewadapter.bean.Student
import com.performance.recyclerviewadapter.bean.UserInfo
import com.performance.recyclerviewadapter.databinding.ActivityRecyclerviewBinding
import com.yh.base.lib.adapter.HeaderAndFooterWrapper

class HeadAndFootActivity : AppCompatActivity() {
    val bind: ActivityRecyclerviewBinding by lazy {
        ActivityRecyclerviewBinding.inflate(LayoutInflater.from(this@HeadAndFootActivity))

    }
    val innerAdapter by lazy {
        MyMultiItemTypeAdapter(this@HeadAndFootActivity)
    }
    val mAdapter by lazy {
        HeaderAndFooterWrapper(innerAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        bind.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HeadAndFootActivity)
            adapter = mAdapter
        }
        mAdapter.addHeaderView(TextView(this@HeadAndFootActivity).apply {
            text = "addHeaderView------1"
        })
        mAdapter.addHeaderView(TextView(this@HeadAndFootActivity).apply {
            text = "addHeaderView------2"
        })
        mAdapter.addFootView(TextView(this@HeadAndFootActivity).apply {
            text = "addFootView------1"
        })
        mAdapter.addFootView(TextView(this@HeadAndFootActivity).apply {
            text = "addFootView------2"
        })
        innerAdapter.data.clear()

        innerAdapter.data.add(Student("$0"))
        innerAdapter.data.add(UserInfo("$1"))
        innerAdapter.data.add(UserInfo("$2"))

        mAdapter.notifyDataSetChanged()
    }

}