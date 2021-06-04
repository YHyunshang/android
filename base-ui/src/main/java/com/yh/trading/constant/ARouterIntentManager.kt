package com.yh.trading.constant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.yh.base.ui.toast.ToastUtil
import com.yh.base.utils.StringUtils
import com.yh.trading.router.LoginNavigationCallback

/**
 * @description ARouter跳转管理类
 * @date: 2021/4/17 5:47 PM
 * @author: guowanxin
 */
object ARouterIntentManager {

    fun <T> navigation(path: String, bundle: Bundle? = null): T? {
        return ARouter.getInstance().build(path).with(bundle).navigation() as? T
    }

    fun navigation(path: String, bundle: Bundle? = null) {
        ARouter.getInstance().build(path).with(bundle).navigation()
    }

    fun navigation(context: Context, path: String, requestCode: Int, bundle: Bundle? = null) {
        ARouter.getInstance().build(path).with(bundle).navigation(context as Activity, requestCode)
    }

    fun navigationString(path: String, key: String, value: String) {
        ARouter.getInstance().build(path).withString(key, value).navigation()
    }

    fun navigationInt(path: String, key: String, value: Int) {
        ARouter.getInstance().build(path)
                .withInt(key, value).navigation()
    }

    /**
     * 内部页面跳回到MainActivity，通过index切换到不同的tab
     */
    fun goToMainPage(index: Int) {
        ARouter.getInstance().build(ARouterPathManager.ActivityMain)
                .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .withInt(ARouterPathManager.KEY_INDEX, index).navigation()
    }

    /**
     * 跳转到订单商品界面
     */
    fun navigationToOrderGoodActivity(value: String, goodList: Any) {
        ARouter.getInstance().build(ARouterPathManager.ActivityOrderGood)
                .withString(ARouterPathManager.KEY_TITLE_NAME, value)
                .withObject(ARouterPathManager.KEY_GOOD_LIST, goodList)
                .navigation()
    }

    /**
     * 跳转到结算单界面
     */
    fun navigationToSettleOrderActivity(value: Int, ids: MutableList<Long>) {
        ARouter.getInstance().build(ARouterPathManager.ActivitySettleOrder)
                .withInt(ARouterPathManager.KEY_DELIVERY_TYPE, value)
                .withObject(ARouterPathManager.KEY_IDS, ids)
                .navigation()
    }

    /**
     * 跳转到商品详情界面
     * */
    fun navigationToGoodDetail(itemId: String, shopId: String) {
        ARouter.getInstance().build(ARouterPathManager.ActivityGoodInfo).with(bundleOf(
                ARouterPathManager.KEY_ITEM_ID to itemId,
                ARouterPathManager.KEY_SHOP_ID to shopId
        )).navigation(null, LoginNavigationCallback())
    }

    /**
     * 跳转到商品详情分享界面
     * */
    fun navigationToGoodShare(itemId: String, shopId: String) {
        ARouter.getInstance().build(ARouterPathManager.ActivityShareGood).with(bundleOf(
                ARouterPathManager.KEY_ITEM_ID to itemId,
                ARouterPathManager.KEY_SHOP_ID to shopId
        )).navigation()
    }

    /**
     * 跳转到店铺详情分享界面
     * */
    fun navigationToShopShare(shopId: String) {
        ARouter.getInstance().build(ARouterPathManager.ActivityShareShop).with(bundleOf(
                ARouterPathManager.KEY_SHOP_ID to shopId
        )).navigation()
    }

    /**
     * @description 加购界面
     * @time 2021/5/17 9:57 AM
     * @author zengbobo
     */
    fun navigationToCartEditDialog(context: Any, json: String, itemId: String? = "", shopId: String? = "", isShowNotice: Boolean = false) {
        if (MMKVUiManager.getToken().isEmpty()) {
            ARouterIntentManager.navigation(ARouterPathManager.ActivityLogin)
            return
        }
        if (StringUtils.isNullOrEmpty(json)) {
            ToastUtil.showShortMsg("商品规格不能为空")
            return
        }
        val mDialogFragment = ARouterIntentManager.navigation<DialogFragment>(ARouterPathManager.FragmentCartEditDialog,
                bundleOf(
                        ARouterPathManager.KEY_JSON to json,
                        ARouterPathManager.KEY_ITEM_ID to itemId,
                        ARouterPathManager.KEY_SHOP_ID to shopId,
                        ARouterPathManager.KEY_TYPE to isShowNotice
                ))
        if (context is Fragment) {
            mDialogFragment?.show(context.childFragmentManager, ARouterPathManager.FragmentCartEditDialog)
        } else if (context is FragmentActivity) {
            mDialogFragment?.show(context.supportFragmentManager, ARouterPathManager.FragmentCartEditDialog)
        }
    }

    /**
     * @description 加购界面
     * @time 2021/5/17 9:57 AM
     * @author zengbobo
     */
//    fun navigationToCartEditDialog(context: Fragment, json: String, itemId: String? = "", shopId: String? = "") {
//        if (StringUtils.isNullOrEmpty(json)) {
//            ToastUtil.showShortMsg("商品规格不能为空")
//            return
//        }
//        val mDialogFragment = ARouterIntentManager.navigation<DialogFragment>(ARouterPathManager.FragmentCartEditDialog,
//                bundleOf(
//                        ARouterPathManager.KEY_JSON to json,
//                        ARouterPathManager.KEY_ITEM_ID to itemId,
//                        ARouterPathManager.KEY_SHOP_ID to shopId
//                ))
//        if (context is Fragment) {
//            mDialogFragment?.show(context.childFragmentManager, ARouterPathManager.FragmentCartEditDialog)
//        } else if (context is FragmentActivity) {
//            mDialogFragment?.show(context.supportFragmentManager, ARouterPathManager.FragmentCartEditDialog)
//        }
//    }

    /**
     * 跳转到收银台界面
     */
    fun navigationToCashierActivity(isSplit: Int, orderAmount: Long, orderNo: String) {
        ARouter.getInstance().build(ARouterPathManager.ActivityCashier)
                .withInt(ARouterPathManager.KEY_IS_SPLIT, isSplit)
                .withLong(ARouterPathManager.KEY_ORDER_AMOUNT, orderAmount)
                .withString(ARouterPathManager.KEY_ORDER_NO, orderNo)
                .navigation()
    }

    /**
     * @description  店铺详情
     * @time 2021/5/17 9:55 AM
     * @author zengbobo
     */
    fun navigationToShopDetail(shopId: String?) {
        if (StringUtils.isNullOrEmpty(shopId)) {
            ToastUtil.showShortMsg("店铺Id不能为空")
            return
        }
        ARouter.getInstance().build(ARouterPathManager.ActivityShopDetail).with(bundleOf(
                ARouterPathManager.KEY_SHOP_ID to shopId
        )).navigation(null, LoginNavigationCallback())
    }


    /**
     * @description 登陆
     * @time 2021/5/17 9:55 AM
     * @author zengbobo
     */
    fun navigationToLogin() {
        navigation(ARouterPathManager.ActivityLogin)
    }

    /**
     * 内部页面跳回到MainActivity，通过index切换到不同的tab
     */
    fun navigationToOrderList(index: Int? = null) {
        val postcard = ARouter.getInstance().build(ARouterPathManager.ActivityOrderList).withFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        if (index != null)
            postcard.withInt(ARouterPathManager.KEY_INDEX, index);
        postcard.navigation(null, LoginNavigationCallback())
    }

    /**
     * 支付成功之后跳转到订单详情
     */
//    fun navigationToOrderList() {
//        ARouter.getInstance().build(ARouterPathManager.ActivityOrderList)
//                .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP).navigation()
//    }

    fun navigationToSearchGoods(shopId: String) {
        navigation(ARouterPathManager.ActivitySearchGoods,
                bundleOf(
                        ARouterPathManager.KEY_SHOP_ID to shopId
                ))
    }

    fun navigationToInputInvoice(amount: Long, ids: MutableList<String>) {
        ARouter.getInstance().build(ARouterPathManager.ActivityMineInputInvoice)
                .withLong(ARouterPathManager.KEY_INVOICE_AMOUNT, amount)
                .withObject(ARouterPathManager.KEY_ORDER_NO, ids)
                .navigation()
    }

    fun navigationToInvoiceOrder(status: Int, ids: MutableList<String>) {
        ARouter.getInstance().build(ARouterPathManager.ActivityMineInvoiceOrder)
                .withInt(ARouterPathManager.KEY_STATUS, status)
                .withObject(ARouterPathManager.KEY_ORDER_NO, ids)
                .navigation()
    }
}