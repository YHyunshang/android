package com.yh.baseui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.yh.base.ui.toast.ToastUtil.showShortMsg
import com.yh.base.utils.LogUtils
import com.yh.base.utils.StringUtils
import com.yh.base.utils.StringUtils.isNullOrEmpty
import com.yh.baseui.R
import com.yh.trading.constant.ARouterPathManager
import com.yh.trading.constant.MMKVUiManager
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*


/**
 * @description $
 * @date: 2021/4/20 11:12 AM
 * @author: zengbobo
 */
object Utils {
    /**
     * 打电话
     *
     * @description
     * @time 2021/4/27 3:28 PM
     * @author zengbobo
     */
    fun callPhone(context: Context, phone: CharSequence) {
        XXPermissions.with(context)
                .permission(Permission.CALL_PHONE)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: List<String>, all: Boolean) {
                        try {
                            val mIntent = Intent(Intent.ACTION_DIAL)
                            mIntent.data = Uri.parse("tel:$phone")
                            context.startActivity(mIntent)
                        } catch (e: Exception) {
                            showShortMsg(e.message)
                        }
                    }

                    override fun onDenied(permissions: List<String>, never: Boolean) {
                        showShortMsg(R.string.Permission_call_phone)
                    }
                })
    }

    /**
     *
     * @description  打开系统和app定位权限，需要调用方实现 Activity.onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
     * @time 2021/5/11 11:06 AM
     * @author zengbobo
     */
    fun openLocationPermission(activity: Activity, success: () -> Unit, failure: () -> Unit) {
        if (isSystemLocationEnabled(activity)) {
            if (XXPermissions.isGranted(activity, Permission.ACCESS_FINE_LOCATION)) {
                success()
            } else {
                openAppLocationPermission(activity, success, failure)
            }
        } else {
            openSystemLocationSettings(activity)
        }
    }

    /**
     * 打开app定位定位权限
     * @description
     * @time 2021/4/27 3:29 PM
     * @author zengbobo
     */
    fun openAppLocationPermission(context: Context, success: () -> Unit, failure: () -> Unit) {
        XXPermissions.with(context)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: List<String>, all: Boolean) {
                        success()
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        failure()
                    }
                })
    }

    /**
     * @description 打开系统定位设置,需要调用方实现 Activity.onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
     * @time 2021/5/11 10:33 AM
     * @author zengbobo
     */
    fun openSystemLocationSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(intent, ARouterPathManager.INTENT_REQUEST_CODE_1)
        } else {
            showShortMsg("该设备不支持位置服务")
        }
    }

    /**
     * @description 判断用户是否打开系统定位服务
     * @time 2021/5/11 10:47 AM
     * @author zengbobo
     */
    fun isSystemLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            locationMode = try {
                Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            locationMode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            locationProviders = Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            !isNullOrEmpty(locationProviders)
        }
    }

    /**
     * @description  是否登陆
     * @time 2021/5/14 3:19 PM
     * @author zengbobo
     */
    fun isLogin(): Boolean {
        return !StringUtils.isNullOrEmpty(MMKVUiManager.getToken()) and !StringUtils.isNullOrEmpty(MMKVUiManager.getPhone())
    }

    /**
     * 获取手机IP地址
     */
    fun getLocalIpAddress(context: Context): String? {
        try {
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf: NetworkInterface = en.nextElement()
                val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        return inetAddress.hostAddress.toString()
                    }
                }
            }
        } catch (ex: SocketException) {
            LogUtils.e("WifiPreference IpAddress", ex.toString())
        }
//        val ip: String? = null
//        val conMann = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val mobileNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//        val wifiNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//
//        if (mobileNetworkInfo!!.isConnected) {
//            ip = getLocalIpAddress()
//        } else if (wifiNetworkInfo!!.isConnected) {
//            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager?
//            val wifiInfo = wifiManager?.connectionInfo
//            val ipAddress = wifiInfo?.ipAddress
//            ip = intToIp(ipAddress)
//        }
        return null
    }

//    fun getLocalIpAddress() {
//        try {
//            String ipv4;
//            ArrayList< NetworkInterface  nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface ni: nilist)
//            {
//                ArrayList< InetAddress  ialist = Collections.list(ni.getInetAddresses());
//                for (InetAddress address: ialist){
//                if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4=address.getHostAddress()))
//                {
//                    return ipv4;
//                }
//            }
//
//            }
//
//        } catch (SocketException ex) {
//            Log.e("localip", ex.toString());
//        }
//        return null;
//    }


}