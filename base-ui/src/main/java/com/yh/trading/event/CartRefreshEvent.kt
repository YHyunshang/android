package com.yh.trading.event

/**
 * 刷新购物车请求
 * */
class CartRefreshEvent(val number: Int? = null) {
}

/**
 * 接收购物车数据变化
 * */
class CartSetNumberEvent(val number: Int? = null) {
}