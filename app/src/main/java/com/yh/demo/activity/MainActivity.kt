package com.yh.demo.activity

import android.app.ProgressDialog
import android.os.Looper
import com.alibaba.android.arouter.facade.annotation.Route
import com.yh.android.demo.databinding.ActivityMainBinding
import com.yh.base.ui.toast.ToastUtil
import com.yh.base.utils.GsonUtil
import com.yh.baseui.activity.BaseUIActivity
import com.yh.baseui.utils.ImageLoaderUtils
import com.yh.sdk.download.DownloadUtil
import com.yh.trading.constant.ARouterPathManager
import com.yh.demo.viewmodel.MainViewModel


@Route(path = ARouterPathManager.ActivityMain)
class MainActivity : BaseUIActivity<MainViewModel, ActivityMainBinding>() {

    override fun initListener() {
        mViewBinding.run {
            testNet.setOnClickListener {
                mViewModel.getTels()
            }
            testDownload.setOnClickListener {
                testDownload()
            }
            testLoadImage.setOnClickListener {
                ImageLoaderUtils.load(this@MainActivity,
                        "https://image.yonghuivip.com/image/1459821227635dfaaeed694eae97c85b434d102c0fe199eb9c461?imageMogr2/thumbnail/300x/",
                        icon)
            }
            testVideo.setOnClickListener {

            }
        }
    }

    override fun lifecycleObserver() {
        mViewModel.telsLiveData1.observe(this, {
            ToastUtil.showShortMsg("成功:" + GsonUtil.toJson(it))
        })
    }

    override fun loadData() {
    }

    fun testDownload() {
        val dialog = ProgressDialog(this)
        dialog.setTitle("版本更新")
        dialog.setMessage("下载中...")
        dialog.setCancelable(false)
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.show()
        DownloadUtil.init(this)
        val filePath = "$filesDir/download/1.apk"
        DownloadUtil.requestDownload("https://oss.pgyer.com/6007e0238c5745312748ddd0469e5132.apk?auth_key=1623208423-296ea67e30104fd610395c7eaeeef61e-0-dd48aa43417ffbf1b1c32741c1856621&response-content-disposition=attachment%3B+filename%3D%25E9%2597%25A8%25E5%25BA%2597%25E9%2580%259A_1.0.64.apk",
                filePath,
                null,
                null,
                null, Long.MAX_VALUE, object : DownloadUtil.DownloadListener {
            override fun onSuccess() {
                ToastUtil.showShortMsg("下载成功")
                onFail(0)
            }

            override fun onDownloading(curBytes: Long, totalBytes: Long) {
                dialog.progress = curBytes.toInt()
                dialog.max = totalBytes.toInt()
            }

            override fun onFail(errorCode: Int) {
                android.os.Handler(Looper.getMainLooper()).post {
                    dialog.hide()
                }
            }
        })
    }
}