package com.yh.tobtrading.sdk.baidumap.bean

/**
 * @description $
 * @date: 2021/5/11 5:23 PM
 * @author: zengbobo
 */
data class YHMapReverseGeoCodeBean(var latitude: Double,
                                   var longitude: Double,
                                   var cityId: String,
                                   var provinceName: String,
                                   var cityName: String,
                                   var areaName: String
)
