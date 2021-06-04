package com.yh.base.utils;

import android.util.Log;

/**
 * @description 开关配置
 * @date: 2021/5/5 4:41 PM
 * @author: zhangzhiyuan
 */
public class Config {
    public static boolean isDebug = BuildConfig.DEBUG;
    private final static String TAG = "tobtrading";

    /**
     * 打开debug模式命令：adb shell setprop log.tag.tobtrading V
     */
    public static boolean isIsDebug() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            return true;
        }
        return isDebug;
    }
}
