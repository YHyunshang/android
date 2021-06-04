package com.performance.recyclerviewadapter.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.performance.recyclerviewadapter.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tv_SingleTypeAdapter).setOnClickListener {
            startActivity(Intent(this@MainActivity, SingleTypeAdapterActivity::class.java))
        }
        findViewById<View>(R.id.tv_MultiItemTypeAdapter).setOnClickListener {
            startActivity(Intent(this@MainActivity, MultiTypeAdapterActivity::class.java))
        }
        findViewById<View>(R.id.tv_HeaderAndFooterWrapper).setOnClickListener {
            startActivity(Intent(this@MainActivity, HeadAndFootActivity::class.java))

        }
    }
}