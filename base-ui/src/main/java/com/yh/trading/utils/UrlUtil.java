package com.yh.trading.utils;

import com.yh.base.constant.Config;

public class UrlUtil {
    public static String getLoginUrl() {
        if (Config.isIsDebug()) {
            return "https://pc-mid-p.order-fresh.apis.yonghui.cn/";
        }
        return "https://pc-mid-p.order-fresh.apis.yonghui.cn2/";
    }

    public static String getUrl() {
        if (Config.isIsDebug()) {
            return "http://cp-b2byjapp-sit.tob-trading-platform.sitapis.yonghui.cn";
        }
        return "http://cp-b2byjapp-sit.tob-trading-platform.sitapis.yonghui.cn";
    }
}
