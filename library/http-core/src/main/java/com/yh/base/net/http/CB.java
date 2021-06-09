package com.yh.base.net.http;

import com.yh.base.net.bean.Rsp;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public abstract class CB<T> implements Observer<T> {

    Disposable disposable;

    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(@NonNull Disposable disposable) {
        this.disposable = disposable;
//        d.dispose();
    }

    @Override
    public void onNext(@NonNull T t) {
        disposable.dispose();
        Rsp rootRsp = (Rsp) t;
        if (rootRsp.getCode() == Code.SUCCESS) {
            onSuccess(t);
        } else {
            onFail(rootRsp.getCode(), rootRsp.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        onFail(-1, e.getMessage());
    }


    public abstract void onSuccess(T object);

    public abstract void onFail(int code, String msg);
}
