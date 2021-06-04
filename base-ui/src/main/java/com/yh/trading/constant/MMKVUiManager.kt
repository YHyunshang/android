package com.yh.trading.constant

import com.yh.base.utils.LogUtils
import com.yh.base.utils.MMKVManager

/**
 * @description MMKV缓存面向业务层管理类
 * @date: 2021/4/13 10:52 AM
 * @author: guowanxin
 */
class MMKVUiManager {

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_ICON = "user_icon"
        private const val KEY_PHONE = "phone"
        private const val KEY_USER_ID = "userId"
        private const val KEY_CITY = "city"
        private const val KEY_CITY_CODE = "city_code"
        private const val KEY_latitude = "latitude"
        private const val KEY_longitude = "longitude"

        fun setUserName(userName: String?) {
            MMKVManager.instance.put(KEY_USER_NAME, userName)
        }

        fun getUserName(): String {
            return MMKVManager.instance.getString(KEY_USER_NAME)
        }

        fun setUserIcon(userName: String?) {
            MMKVManager.instance.put(KEY_USER_ICON, userName)
        }

        fun getUserIcon(): String {
            return MMKVManager.instance.getString(KEY_USER_ICON)
        }

        fun setToken(token: String?) {
            MMKVManager.instance.put(KEY_TOKEN, token)
        }

        fun getToken(): String {
            return MMKVManager.instance.getString(KEY_TOKEN)
        }

        fun setPhone(phone: String) {
            LogUtils.d("MMKVUiManager phone=$phone")
            MMKVManager.instance.put(KEY_PHONE, phone)
        }

        fun getPhone(): String {
            return MMKVManager.instance.getString(KEY_PHONE)
        }

        fun setUserId(userId: String) {
            LogUtils.d("MMKVUiManager userId=$userId")
            MMKVManager.instance.put(KEY_USER_ID, userId)
        }

        fun getUserId(): String {
            return MMKVManager.instance.getString(KEY_USER_ID)
        }

        fun setCity(city: String?) {
            MMKVManager.instance.put(KEY_CITY, city)
        }

        fun getCity(): String {
            return MMKVManager.instance.getString(KEY_CITY)
        }

        fun setCityCode(city: String?) {
            MMKVManager.instance.put(KEY_CITY_CODE, city)
        }

        fun getCityCode(): String {
            return MMKVManager.instance.getString(KEY_CITY_CODE)
        }

        fun setLongitude(longitude: Double?) {
            MMKVManager.instance.put(KEY_longitude, longitude)
        }

        fun getLongitude(): Double {
            return MMKVManager.instance.getDouble(KEY_longitude)
        }

        fun setLatitude(latitude: Double?) {
            MMKVManager.instance.put(KEY_latitude, latitude)
        }

        fun getLatitude(): Double {
            return MMKVManager.instance.getDouble(KEY_latitude)
        }
    }

}