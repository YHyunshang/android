package com.yh.baseui.utils

import java.util.regex.Pattern

/**
 * @description 数据相关工具类
 * @date: 2021/4/23 10:48 AM
 * @author: guowanxin
 */
object DataUtil {

    fun <T> listIsEmpty(list: List<T>?): Boolean {
        return list == null || list.isEmpty()
    }

    fun <T> listNotEmpty(list: List<T>?): Boolean {
        return list != null && list.isNotEmpty()
    }

    /**
     * 是否是正确的手机号
     */
    fun isPhone(phone: String): Boolean {
        val regex = "^1[0-9]{10}$"
        return Pattern.compile(regex).matcher(phone).matches()
    }

    /**
     * 是否是正确的邮箱
     */
    fun isEmail(match: String): Boolean {
        val regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$"
        return Pattern.compile(regex).matcher(match).matches()
    }
}