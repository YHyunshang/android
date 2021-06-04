package com.yh.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.mmkv.MMKV
import com.yh.base.constant.Config
import com.yh.base.net.http.Https
import com.yh.base.utils.LogUtils
import com.yh.base.utils.Util

/**
 * @description $
 * @date: 2021/4/6 2:07 PM
 * @author: zengbobo
 */
open class BaseApplication : Application() {
    val startTick = System.currentTimeMillis()

    fun getCurrentCost(): Long {
        return (System.currentTimeMillis() - startTick);
    }

    companion object {
        private lateinit var newInstance: BaseApplication
        fun getInstance(): BaseApplication {
            return newInstance
        }

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        newInstance = this
        LogUtils.initLogConfig()
        Util.init(this)
        MMKV.initialize(this)
        Https.init()
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (Config.isIsDebug()) {
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this)
    }
}