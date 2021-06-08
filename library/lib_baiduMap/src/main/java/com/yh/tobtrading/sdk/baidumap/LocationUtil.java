package com.yh.tobtrading.sdk.baidumap;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.text.DecimalFormat;

/**
 * 距离计算
 */
public class LocationUtil {
    protected static boolean isEnableDebug = true;
    /**
     * 如果要判断是否出错通过NO_ERROR != 200
     */
    public static final int OTHER_ERROR = 100;
    public static final int NO_ERROR = 200;
    public static final double DISTANCE_ERROR = -1;//获取距离异常
    private static final int MAX_DISTANCE_SHOW = Integer.MAX_VALUE;//最大显示30公里

    public final static String ERRO_LAT = "4.9E-324";
    public final static String ERRO_LNG = "4.9E-324";

    public static void setEnableDebug(boolean isEnableDebug) {
        LocationUtil.isEnableDebug = isEnableDebug;
    }

    /**
     * /返回单位是米
     * 根据经纬度来返回直线距离
     *
     * @return
     */
    public static double getDistance(String latitude1, String longitude1,
                                     String latitude2, String longitude2) {
        double lat1, lng1;
        if (TextUtils.isEmpty(latitude1) || TextUtils.isEmpty(longitude1)) {
            lat1 = lng1 = 0;
        } else {
            lat1 = Double.parseDouble(latitude1);
            lng1 = Double.parseDouble(longitude1);
        }
        return getDistance(lat1, lng1, latitude2, longitude2);
    }


    public static double getDistance(double latitude1, double longitude1,
                                     String latitude2, String longitude2) {
        double lat2, lng2;
        if (TextUtils.isEmpty(latitude2) || TextUtils.isEmpty(longitude2)) {
            lat2 = lng2 = 0;
        } else {
            lat2 = Double.parseDouble(latitude2);
            lng2 = Double.parseDouble(longitude2);
        }
        LatLng var0 = new LatLng(latitude1, longitude1);
        LatLng var1 = new LatLng(lat2, lng2);
        Log.i("Location", "LocationEntity LatLng " + var0 + " ———— " + var1);
        return DistanceUtil.getDistance(var0, var1);

    }

    public static double getDistance(double latitude, double longitude, LatLng end) {
        Log.i("Location", "LocationEntity latitude=" + latitude + ",longitude=" + longitude + " ———— end=" + end);
        if (latitude == -1 || longitude == -1
                || latitude == Double.MAX_VALUE || longitude == Double.MAX_VALUE) {
            return -1;
        }
        LatLng start = new LatLng(latitude, longitude);
        return DistanceUtil.getDistance(start, end);
    }


    public static double getDistance(LatLng start, LatLng end) {
        Log.i("Location", "LocationEntity start=" + start + " ———— end=" + end);
        return DistanceUtil.getDistance(start, end);
    }


    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 格式化距离值
     *
     * @param distance
     * @return
     */
    public static String formatDistance(double distance) {
        String kmUnit = "千米";
        String unit = "米";
        String value = "";
        if (distance < 1000) {
//            if (distance < 100) {
//                value = new StringBuffer().append("<").append(100).append(unit).toString();
//            } else {
            value = getFormat(distance) + unit;
//            }
        } else {
            if (distance <= MAX_DISTANCE_SHOW) {
                value = getFormat(distance / 1000) + kmUnit;
            } else {
                value = new StringBuffer().append(">").append(getFormat(MAX_DISTANCE_SHOW / 1000)).append(kmUnit).toString();
            }
        }

        Log.i("Location", "LocationEntity formatDistance value=" + value);
        return value;
    }

    private static String getFormat(double d) {
        String s = new DecimalFormat("#.#").format(d);
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static void d(String str) {
        if (isEnableDebug) {
            Log.d("YHMap", str);
        }
    }
}
