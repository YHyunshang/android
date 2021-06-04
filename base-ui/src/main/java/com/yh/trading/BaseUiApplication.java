package com.yh.trading;

import com.yh.base.BaseApplication;
import com.yh.trading.http.HttpUtils;

/**
 * base-ui层Application
 * @author guowanxin
 */
public class BaseUiApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        HttpUtils.init();
    }
}
