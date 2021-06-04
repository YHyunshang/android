package com.performance.recyclerviewadapter.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.performance.recyclerviewadapter.adapter.MyMultiItemTypeAdapter
import com.performance.recyclerviewadapter.bean.Student
import com.performance.recyclerviewadapter.bean.UserInfo
import com.performance.recyclerviewadapter.databinding.ActivityRecyclerviewBinding

class MultiTypeAdapterActivity : AppCompatActivity() {
    val bind: ActivityRecyclerviewBinding by lazy {
        ActivityRecyclerviewBinding.inflate(LayoutInflater.from(this@MultiTypeAdapterActivity))

    }
    val mAdapter by lazy {
        MyMultiItemTypeAdapter(this@MultiTypeAdapterActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        bind.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MultiTypeAdapterActivity)
            adapter = mAdapter
        }

        mAdapter.data.clear()
        for (i in 0..20) {
            mAdapter.data.add(Student("$i"))
            mAdapter.data.add(UserInfo("$i"))
        }
        mAdapter.notifyDataSetChanged()
    }

}