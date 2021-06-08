package com.yh;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.yh.base.constant.Config;
import com.yh.base.utils.LogUtils;
import com.yh.trading.BaseUiApplication;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

public class YHApplication extends BaseUiApplication {

    @Override
    protected void attachBaseContext(@NotNull Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
//        CrashReport.initCrashReport(getApplicationContext(), "appId", Config.isIsDebug());
        super.onCreate();
//        EventBus.builder()
//                .addIndex(new AppEventBusIndex())
//                .installDefaultEventBus();
        LogUtils.tag("monitor-tob").v("YHApplication onCreate=" + getCurrentCost());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
