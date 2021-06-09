package com.yh.base.utils

import org.greenrobot.eventbus.EventBus

/**
 * @description $
 * @date: 2021/5/12 3:02 PM
 * @author: zengbobo
 */
object EventBusUtils {

    fun register(subscriber: Any) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            return
        }
        EventBus.getDefault().register(subscriber)
    }

    fun unregister(subscriber: Any) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber)
        }
    }

    fun post(event: Any) {
        EventBus.getDefault().post(event)
    }
}