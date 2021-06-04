package com.yh.viewmodel

import com.yh.base.net.bean.Rsp
import com.yh.base.net.http.Https
import com.yh.base.ui.BaseViewModel
import com.yh.base.ui.livedata.EventLiveData
import com.yh.base.ui.livedata.RefreshLiveData
import com.yh.bean.ModelTels
import com.yh.service.ApiService
import com.yh.trading.utils.UrlUtil

open class MainViewModel : BaseViewModel() {
    companion object {
        val service: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Https.service(UrlUtil.getUrl(), ApiService::class.java, true)
        }
    }

    val telsLiveData1 = EventLiveData<List<ModelTels>?>()
    val telsLiveData2 = EventLiveData<Rsp<List<ModelTels>>>()
    fun getTels() {
        request {
            service.getTels()
        }.flag(FLAG_ERROR_LAYER or FLAG_LOADING).run(telsLiveData1)

        request {
            service.getTels()
        }.run(telsLiveData2)
    }

}