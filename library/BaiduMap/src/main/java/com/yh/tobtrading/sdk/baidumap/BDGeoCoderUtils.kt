package com.yh.tobtrading.sdk.baidumap

import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.yh.tobtrading.sdk.baidumap.bean.YHMapLocationBean
import com.yh.tobtrading.sdk.baidumap.bean.YHMapReverseGeoCodeBean
import com.yh.tobtrading.sdk.baidumap.listener.OnYHMapGeoCoderResultListener


/**
 * @description 地理编码是地址信息和地理坐标之间的相互转换。可分为正地理编码（地址信息转换为地理坐标）和逆地理编码（地理坐标转换为地址信息）。
 *
 * @date: 2021/5/11 5:02 PM
 * @author: zengbobo
 */
class BDGeoCoderUtils(val listener: OnYHMapGeoCoderResultListener) {
    private val mGeoCoder: GeoCoder by lazy {
        GeoCoder.newInstance().apply {
            setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener)
        }
    }

   private val mOnGetGeoCoderResultListener by lazy {
        object : OnGetGeoCoderResultListener {
            /**
             * @description 地理编码（地址转坐标）
             * @time 2021/5/11 5:14 PM
             * @author zengbobo
             */
            override fun onGetGeoCodeResult(geoCodeResult: GeoCodeResult) {
                LocationUtil.d("BDPoiSearchUtils mOnGetGeoCoderResultListener onGetGeoCodeResult  geoCodeResult=${geoCodeResult?.error?:null}")
                if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    listener.onResultErrorCode(LocationUtil.OTHER_ERROR)
                } else {
                    var latitude = geoCodeResult.getLocation().latitude
                    var longitude = geoCodeResult.getLocation().longitude
                    listener.onGetGeoCodeResult(YHMapLocationBean(latitude, longitude))
                }
            }

            /**
             * @description 逆地理编码（即坐标转地址）
             * @time 2021/5/11 5:14 PM
             * @author zengbobo
             */
            override fun onGetReverseGeoCodeResult(result: ReverseGeoCodeResult?) {
                LocationUtil.d("BDPoiSearchUtils mOnGetGeoCoderResultListener onGetReverseGeoCodeResult  result=${result?.error?:null}")
                if (result == null || result.error !== SearchResult.ERRORNO.NO_ERROR) {
                    listener.onResultErrorCode(LocationUtil.OTHER_ERROR)
                } else {
                    var latitude: Double = result.location.latitude
                    var longitude: Double = result.location.longitude
                    //行政区号
                    var cityId: String = result.adcode.toString()
                    var provinceName: String = result.addressDetail.province
                    var cityName: String = result.addressDetail.city
                    var areaName: String = result.addressDetail.district
                    listener.onGetReverseGeoCodeResult(YHMapReverseGeoCodeBean(
                            latitude, longitude, cityId, provinceName, cityName, areaName
                    ))
                }
            }
        }
    }

    /**
     * @param   latitude
     * @param   longitude
     *
     * @description 逆地理编码（即坐标转地址）
     * @time 2021/5/11 5:11 PM
     * @author zengbobo
     */
    fun reverseGeoCode(latitude: Double, longitude: Double) {
        LocationUtil.d("BDPoiSearchUtils onGetPoiResult  latitude=$latitude  longitude=$longitude")
        mGeoCoder.reverseGeoCode(ReverseGeoCodeOption()
                .location(LatLng(latitude, longitude)) // 设置是否返回新数据 默认值0不返回，1返回
                .newVersion(1) // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                .radius(500))
    }

    /**
     * @param   cityName    北京
     * @param   address     北京上地十街10号
     *
     * @description 地理编码（地址转坐标）
     * @time 2021/5/11 5:11 PM
     * @author zengbobo
     */
    fun geocode(cityName: String, address: String) {
        LocationUtil.d("BDPoiSearchUtils onGetPoiResult cityName=$cityName  address=$address")
        mGeoCoder.geocode(GeoCodeOption()
                .city(cityName)
                .address(address))
    }

    /**
     *
     * @description 释放检索实例
     * @time 2021/5/11 5:12 PM
     * @author zengbobo
     */
    fun destroy() {
        mGeoCoder.destroy()
    }
}