package com.yh.service;

import com.yh.base.net.bean.Rsp
import com.yh.bean.ModelTels
import retrofit2.http.GET
import retrofit2.http.Query


open interface ApiService {
    @GET("home/tels")
    suspend fun getTels(@Query("shopId") shopId: String? = null): Rsp<List<ModelTels>>
}
