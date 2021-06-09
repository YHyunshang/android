package com.yh.sdk.pay.alipay

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import com.alipay.sdk.app.OpenAuthTask
import com.alipay.sdk.app.PayTask
import com.yh.base.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @description 支付宝支付封装类
 * @date: 2021/5/7 4:37 PM
 * @author: guowanxin
 */
class AliPayManager private constructor() {

    companion object {
        val instance: AliPayManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AliPayManager()
        }
    }

    @SuppressLint("CheckResult")
    fun requestPay(activity: Activity, orderInfo: String, paySuccess: () -> Unit) {
        LogUtils.e("orderInfo====$orderInfo===")
        Observable.just(orderInfo)
                .map {
                    val aliPay = PayTask(activity)
                    val result = aliPay.payV2(orderInfo, true)
                    LogUtils.obj("result====$result===")
                    AliPayResult(result)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    it?.run {
                        LogUtils.i("==result=${result}===resultStatus=${resultStatus}")
                        when (resultStatus) {
                            OpenAuthTask.OK.toString() -> {
                                // 支付成功
                                paySuccess()
                            }
                            OpenAuthTask.Duplex.toString() -> {
                                // 3s内快速发起了多次支付
                                Toast.makeText(activity, "支付过快，请稍后重试", Toast.LENGTH_SHORT).show()
                            }
                            OpenAuthTask.NOT_INSTALLED.toString() -> {
                                // 用户未安装支付宝
                                Toast.makeText(activity, "未安装支付宝App", Toast.LENGTH_SHORT).show()
                            }
                            "6001" -> {
                                // 支付已取消
                                Toast.makeText(activity, "已取消支付", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                // 4000 - 其它错误，如参数传递错误
                                Toast.makeText(activity, result ?: "支付失败", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

    }

}