package com.yh.trading.event

/**
 * @description $
 * @date: 2021/5/21 4:30 PM
 * @author: zengbobo
 */
data class CartEditEvent(var shopId: String, var itemId: String, var skuMap: HashMap<String, Int>, var total: Int = -1,var changeNumber: Int = -1,)
