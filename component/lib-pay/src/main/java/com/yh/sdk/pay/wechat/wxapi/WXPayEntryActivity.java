package com.yh.sdk.pay.wechat.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yh.sdk.pay.Constant;

/**
 * @description 微信支付回调界面
 * @date: 2021/5/6 8:44 PM
 * @author: guowanxin
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.weChat_key);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("WXPayEntryActivity", "onResp==="+baseReq.getType());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("WXPayEntryActivity", "onResp==="+baseResp.errCode+"===="+baseResp.getType()+"==="+baseResp.errStr);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (baseResp.errCode == 0) {    //成功
                finish();
            } else if (baseResp.errCode == -1) {  //失败  可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//                CommonUtils.showShortToast(this, CommonUtils.getString(this, R.string.pay_fail));
                finish();
            } else if (baseResp.errCode == -2) {  //用户取消
//                CommonUtils.showShortToast(this, CommonUtils.getString(this, R.string.pay_cancel));
                finish();
            }
        }
    }

}
