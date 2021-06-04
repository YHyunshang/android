package com.yh.baseui.utils

import com.yh.base.utils.StringUtils
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @description 价格计算工具类
 * @date: 2021/4/26 4:28 PM
 * @author: guowanxin
 */
object NumUtil {


    /**
     * 分转元，转换为bigDecimal在toString
     *
     * @return
     */
    fun changeF2Y(price: Long): String {
        return BigDecimal.valueOf(price).divide(BigDecimal(100), 2, RoundingMode.HALF_UP).toString()
    }

    /**
     *
     * @description  库存小数点0去了
     * @time 2021/6/2 6:31 PM
     * @author zengbobo
     */
    fun changeStock(str: String?): String {
        if (StringUtils.isNullOrEmpty(str)) {
            return ""
        }
        if (!str!!.contains(".")) {
            return str
        }
        var indexOf = str.length
        for (i in (str.length - 1) downTo 0) {
            var c = str[i]
            if (c == '.') {
                indexOf = i
                break
            } else if (c != '0') {
                break
            } else {
                indexOf = i
            }
        }
        return str.substring(0, indexOf)
    }


    /**
     * @param  number
     *
     * @description 数量转文本显示
     * @time 2021/4/30 6:01 PM
     * @author zengbobo
     */
    fun numberToText(number: Long): String {
        if (number > 10000) {
            return "${(number / 10000)}w+"
        }
        return "$number"
    }

    /**
     * @param  cartCount
     *
     * @description 购物车数量最大值显示转换
     * @time 2021/5/28 2:25 PM
     * @author zengbobo
     */
    fun numberMaxShow(cartCount: Int): String {
        return if (cartCount <= 99) "$cartCount" else "···"
    }
}