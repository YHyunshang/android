package com.yh.trading.router;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yh.trading.constant.ARouterPathManager;

import static com.yh.trading.constant.ARouterPathManager.KEY_NAVE_PATH;

public class LoginNavigationCallback implements NavigationCallback {
    @Override
    public void onFound(Postcard postcard) {

    }

    @Override
    public void onLost(Postcard postcard) {

    }

    @Override
    public void onArrival(Postcard postcard) {
    }

    @Override
    public void onInterrupt(Postcard postcard) {
        String path = postcard.getPath();
        Bundle bundle = postcard.getExtras();
        ARouter.getInstance().build(ARouterPathManager.ActivityLogin)
                .with(bundle)
                .withString(KEY_NAVE_PATH, path)
                .navigation();
    }
}
