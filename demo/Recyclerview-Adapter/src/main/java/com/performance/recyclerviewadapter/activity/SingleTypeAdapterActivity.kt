package com.performance.recyclerviewadapter.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.performance.recyclerviewadapter.adapter.MySingleTypeAdapter
import com.performance.recyclerviewadapter.bean.Student
import com.performance.recyclerviewadapter.databinding.ActivityRecyclerviewBinding

class SingleTypeAdapterActivity : AppCompatActivity() {
    val bind: ActivityRecyclerviewBinding by lazy {
        ActivityRecyclerviewBinding.inflate(LayoutInflater.from(this@SingleTypeAdapterActivity))

    }
    val mAdapter by lazy {
        MySingleTypeAdapter(this@SingleTypeAdapterActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        bind.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SingleTypeAdapterActivity)
            adapter = mAdapter
        }

        mAdapter.data.clear()
        for (i in 0..20) {
            mAdapter.data.add(Student("$i"))
        }
        mAdapter.notifyDataSetChanged()
    }

}