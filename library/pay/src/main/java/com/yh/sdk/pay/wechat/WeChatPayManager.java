package com.yh.sdk.pay.wechat;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yh.sdk.pay.Constant;

/**
 * @description 微信支付封装类
 * @date: 2021/5/6 9:09 PM
 * @author: guowanxin
 */
public class WeChatPayManager {

    private static WeChatPayManager wechatPayManager;
    private IWXAPI iwxapi;

    private WeChatPayManager() {
    }

    public static WeChatPayManager getInstance() {
        if (wechatPayManager == null) {
            synchronized (WeChatPayManager.class) {
                if (wechatPayManager == null) {
                    wechatPayManager = new WeChatPayManager();
                }
            }
        }
        return wechatPayManager;
    }

    private void initPay(Context context) {
        if (iwxapi == null) {
            iwxapi = WXAPIFactory.createWXAPI(context, null);
            iwxapi.registerApp(Constant.weChat_key);
        }
    }

//    public void requestPay(Context context, WeChatOrderParam weChatOrderParam) {
//        initPay(context);
//
//        PayReq request = new PayReq();
//        request.appId = weChatOrderParam.getAppid();
//        request.partnerId = weChatOrderParam.getPartnerid();
//        request.prepayId = weChatOrderParam.getPrepayid();
//        request.packageValue = weChatOrderParam.getPackageStr();
//        request.nonceStr = weChatOrderParam.getNoncestr();
//        request.timeStamp = weChatOrderParam.getTimestamp();
//        request.sign = weChatOrderParam.getSign();
//        boolean isSuccess = iwxapi.sendReq(request);
//        Log.e("WeChatPayManager","==isSuccess=="+isSuccess+"==checkArgs=="+request.checkArgs());
//    }

}
