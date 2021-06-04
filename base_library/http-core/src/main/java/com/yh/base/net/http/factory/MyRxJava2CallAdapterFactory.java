package com.yh.base.net.http.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class MyRxJava2CallAdapterFactory extends CallAdapter.Factory {
    RxJava2CallAdapterFactory rxJava2CallAdapterFactory;

    public MyRxJava2CallAdapterFactory() {
        rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new MyCallAdapter(rxJava2CallAdapterFactory.get(returnType, annotations, retrofit));
    }

    private class MyCallAdapter<R> implements CallAdapter<R, Object> {

        private CallAdapter<R, Object> mAdapter;

        MyCallAdapter(CallAdapter<R, Object> adapter) {
            this.mAdapter = adapter;
        }

        @Override
        public Type responseType() {
            return mAdapter.responseType();
        }

        @Override
        public Object adapt(Call<R> call) {
            return ((Observable<R>) mAdapter.adapt(call))
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }
}
