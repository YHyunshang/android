package com.yh.tobtrading.sdk.baidumap;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

/**
 * 百度定位服务包装类
 */
public class BDLocationService {

    private LocationClient mClient;
    private LocationClientOption mOption;

    private volatile static BDLocationService sInstance;

    private BDLocationListener listener;

    private BDLocationService(Context context) {
        mClient = new LocationClient(context);
        mClient.setLocOption(getDefaultLocationClientOption(context));
    }

    /**
     * @param context applicationContext
     * @return
     */
    public static BDLocationService getSingleton(Context context) {
        if (sInstance == null) {
            synchronized (BDLocationService.class) {
                if (sInstance == null) {
                    sInstance = new BDLocationService(context);
                }
            }
        }
        return sInstance;
    }

    //    public static BDLocationService getInstance() {
//        return sInstance;
//    }

    public synchronized boolean addLocationListener(BDLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            if (listeners.isEmpty()) {
                Log.v("Location-tob", "registerLocationListener");
                mClient.registerLocationListener(locationListener);
            }
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
            Log.i("Location-tob", "registerBDListener=" + listener);
            this.listener = listener;
            isSuccess = true;
        }
        return isSuccess;
    }

    public synchronized void removeLocationListener(BDLocationListener listener) {
        if (listener != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                Log.v("Location-tob", "registerLocationListener");
                mClient.unRegisterLocationListener(locationListener);
            }
            Log.i("Location-tob", "unRegisterBDListener=" + listener);
        }
    }


    public BDLocationListener getListener() {
        return listener;
    }

    List<BDLocationListener> listeners = new ArrayList<>();

    BDAbstractLocationListener locationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            synchronized (BDLocationService.this) {
                for (int i = listeners.size() - 1; i >= 0; i--) {
                    BDLocationListener listener = listeners.get(i);
                    listener.onReceiveLocation(bdLocation);
                }
                handler.removeCallbacks(requestRunable);
            }
        }
    };

    RequestRunnable requestRunable = new RequestRunnable();

    final Handler handler = new Handler(Looper.getMainLooper());

    class RequestRunnable implements Runnable {
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        long startTime;

        @Override
        public void run() {
            int code = mClient.requestLocation();
            if (System.currentTimeMillis() - startTime < 10 * 1000) {
                Log.i("Location-tob", "requestCode=" + code);
                handler.postDelayed(this, 1000);
            } else {
                Log.i("Location-tob", "requestCode fail=" + code);
                locationListener.onReceiveLocation(null);
            }
        }
    }

    /**
     * start locate
     */
    public synchronized void start() {
        if (!mClient.isStarted()) {
            mClient.start();
        }
        requestRunable.setStartTime(System.currentTimeMillis());
        handler.post(requestRunable);

//            int code = mClient.requestLocation();
//            Log.i("Location-tob", "requestCode=" + code);
    }

    /**
     * stop locate
     */
    public synchronized void stop() {
        mClient.stop();
    }

    public synchronized BDLocation getLastKnownLocation() {
        return mClient.getLastKnownLocation();
    }

    /***
     *
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
//            if (mClient.isStarted())
            mClient.stop();
            mOption = option;
            mClient.setLocOption(option);
            isSuccess = true;
        }
        return isSuccess;
    }


    public LocationClientOption getDefaultLocationClientOption(Context context) {
        if (mOption == null) {
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");
            mOption.setScanSpan(500);
            mOption.setIsNeedAddress(true);
            mOption.setIsNeedLocationDescribe(true);
            mOption.setNeedDeviceDirect(false);
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);
            mOption.setIsNeedLocationDescribe(false);
            mOption.setIsNeedLocationPoiList(true);
            mOption.SetIgnoreCacheException(false);
            mOption.setIsNeedAltitude(false);
            mOption.setOpenGps(true); // 打开gps
            mOption.setProdName(context.getPackageName());
        }
        return mOption;
    }


}