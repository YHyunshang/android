package com.yh.base.ui.livedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.ui.callback.UnPeekLiveData;

public abstract class MyLiveData<T> {
    protected LiveData<T> onSuccess;
    protected LiveData<Error> onFail;

    public MyLiveData(boolean unPeek) {
        if (unPeek) {
            onSuccess = new UnPeekLiveData.Builder<T>().setAllowNullValue(true).create();
            onFail = new UnPeekLiveData.Builder<Error>().setAllowNullValue(true).create();
        } else {
            onSuccess = new MutableLiveData<T>();
            onFail = new MutableLiveData<Error>();
        }
    }

    public void setValue(T value) {
        if (onSuccess instanceof UnPeekLiveData) {
            ((UnPeekLiveData<T>) onSuccess).setValue(value);
        } else if (onSuccess instanceof MutableLiveData) {
            ((MutableLiveData<T>) onSuccess).setValue(value);
        }
    }

    public void postValue(T value) {
        if (onSuccess instanceof UnPeekLiveData) {
            ((UnPeekLiveData<T>) onSuccess).postValue(value);
        } else if (onSuccess instanceof MutableLiveData) {
            ((MutableLiveData<T>) onSuccess).postValue(value);
        }
    }

    public T getValue() {
        return onSuccess.getValue();
    }

    public void postValue(Error value) {
        if (onFail instanceof UnPeekLiveData) {
            ((UnPeekLiveData<Error>) onFail).postValue(value);
        } else if (onFail instanceof MutableLiveData) {
            ((MutableLiveData<Error>) onFail).postValue(value);
        }
    }
}
