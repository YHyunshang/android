package com.yh.tobtrading.sdk.baidumap.listener

import com.yh.tobtrading.sdk.baidumap.bean.YHMapLocationBean
import com.yh.tobtrading.sdk.baidumap.bean.YHMapReverseGeoCodeBean

/**
 * @description $
 * @date: 2021/5/11 6:00 PM
 * @author: zengbobo
 */
interface OnYHMapGeoCoderResultListener {

    /**
     * @see com.baidu.mapapi.search.core.SearchResult.ERRORNO
     * @time 2021/5/11 6:07 PM
     * @author zengbobo
     */
    fun onResultErrorCode(code: Int)

    /**
     * @description 地理编码（地址转坐标）
     * @time 2021/5/11 6:01 PM
     * @author zengbobo
     */
    fun onGetGeoCodeResult(bean: YHMapLocationBean)

    /**
     * @description 逆地理编码（即坐标转地址）
     * @time 2021/5/11 6:01 PM
     * @author zengbobo
     */
    fun onGetReverseGeoCodeResult(bean: YHMapReverseGeoCodeBean)


}