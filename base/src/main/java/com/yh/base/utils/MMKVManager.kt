package com.yh.base.utils

import android.os.Parcelable
import com.tencent.mmkv.MMKV


/**
 * @description MMKV使用管理类
 * @date: 2021/4/10 4:13 PM
 * @author: guowanxin
 */
class MMKVManager private constructor() {

    private var mMmkv: MMKV

    companion object {
        val instance: MMKVManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MMKVManager()
        }
    }

    init {
        mMmkv = MMKV.defaultMMKV()!!
    }

    fun remove(key: String) {
        mMmkv.remove(key)
    }

    fun put(key: String, value: Long?) {
        if (value != null)
            mMmkv.encode(key, value)
        else
            mMmkv.remove(key)
    }

    fun getLong(key: String, default: Long = 0L): Long {
        return mMmkv.decodeLong(key, default)
    }

    fun put(key: String, value: Int?) {
        if (value != null)
            mMmkv.encode(key, value)
        else
            mMmkv.remove(key)
    }

    fun getInt(key: String, default: Int = 0): Int {
        return mMmkv.decodeInt(key, default)
    }

    fun put(key: String, value: String?) {
        if (value != null)
            mMmkv.encode(key, value)
        else
            mMmkv.remove(key)
    }

    fun getString(key: String, default: String = ""): String {
        return mMmkv.decodeString(key) ?: default
    }

    fun put(key: String, value: Boolean?) {
        if (value != null)
            mMmkv.encode(key, value)
        else
            mMmkv.remove(key)
    }

    fun getBooleanToMM(key: String): Boolean {
        return mMmkv.decodeBool(key) ?: false
    }

    fun put(key: String, value: Double?) {
        if (value != null)
            mMmkv.encode(key, value)
        else
            mMmkv.remove(key)
    }

    fun getDouble(key: String, default: Double = 0.0): Double {
        return mMmkv.decodeDouble(key, default)
    }

    fun put(key: String, value: Parcelable?) {
        if (value != null)
            mMmkv.encode(key, value)
        else
            mMmkv.remove(key)
    }

    fun <T : Parcelable> getParcelableToMM(key: String, clazz: Class<T>): T? {
        return mMmkv.decodeParcelable(key, clazz)
    }

}