package com.yh.baseui.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @description 时间相关工具类
 * @date: 2021/4/26 11:34 AM
 * @author: guowanxin
 */
object DateUtil {

    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")

    fun getDateTime(time: Long): String? {
        return sdf.format(Date(time))
    }
}