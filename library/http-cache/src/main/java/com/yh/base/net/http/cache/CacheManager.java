package com.yh.base.net.http.cache;

import com.yh.base.net.http.cache.adapter.ChainAdapter;
import com.yh.base.net.http.cache.adapter.LastAdapter;
import com.yh.base.net.http.cache.adapter.LazyAdapter;
import com.yh.base.net.http.cache.adapter.PageAdapter;
import com.yh.base.net.http.cache.adapter.RefreshAdapter;
import com.yh.base.net.http.cache.adapter.RootAdapter;
import com.yh.base.net.http.cache.adapter.TimedAdapter;
import com.yh.base.utils.SecurityUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

//    static Map<String, TypeItem> stringMap = new ConcurrentHashMap<>();
//
//    public static void notify(String id, Object obj) {
//        TypeItem item;
//        synchronized (stringMap) {
//            item = stringMap.remove(id);
//        }
//        if (item != null) {
//            item.value = obj;
//            item.condition.signalAll();
//        }
//    }
//
//    static class TypeItem<T> {
//        T value;
//        Lock lock = new ReentrantLock();
//        Condition condition = lock.newCondition();
//    }
//
//    public static Object waitForObject(String id) {
//        TypeItem item;
//        synchronized (stringMap) {
//            item = stringMap.get(id);
//            if (item == null) {
//                item = new TypeItem();
//                stringMap.put(id, item);
//            }
//        }
//        try {
//            do {
//                item.condition.await(20 * 1000, TimeUnit.MILLISECONDS);
//            } while (stringMap.containsKey(id));
//            return item.value;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    static class Ref {
//        int cnt = 0;
//
//        public synchronized int add() {
//            return ++cnt;
//        }
//
//        public synchronized int sub() {
//            return --cnt;
//        }
//
//        public synchronized int get() {
//            return cnt;
//        }
//    }
//
//    static Map<String, Ref> reqMap = new ConcurrentHashMap<>();
//
//    public static void startReq(String id) {
//        Ref req;
//        synchronized (reqMap) {
//            req = reqMap.get(id);
//            if (req == null) {
//                reqMap.put(id, new Ref());
//            } else {
//                req.add();
//            }
//        }
//    }
//
//    public static boolean isReq(String id) {
//        Ref req;
//        synchronized (reqMap) {
//            req = reqMap.get(id);
//            if (req == null) {
//                return false;
//            }
//            return true;
//        }
//    }
//
//    public static void endReq(String id) {
//        Ref req;
//        synchronized (reqMap) {
//            req = reqMap.get(id);
//            if (req != null) {
//                if (req.sub() < 0) {
//                    reqMap.remove(req);
//                }
//            }
//        }
//    }

    static Map<String, RootAdapter> rulesMap = new ConcurrentHashMap<>();

    public static RootAdapter getRuleAdapter(String url) {
        return rulesMap.get(url);
    }

    static final LastAdapter lastAdapter = new LastAdapter();
    static final PageAdapter pageAdapter = new PageAdapter();
    static final TimedAdapter timedAdapter = new TimedAdapter(3 * 60 * 1000);
    static final LazyAdapter lazyAdapter = new LazyAdapter(3 * 60 * 1000);
    static final RefreshAdapter refreshAdapter = new RefreshAdapter(3 * 60 * 1000);

    public static void add(String url, RootAdapter adapter) {
        rulesMap.put(url, adapter);
    }

    public static void addLast(String url) {
        rulesMap.put(url, lastAdapter);
    }

    public static void addTimed(String url) {
        rulesMap.put(url, timedAdapter);
    }

    public static void addLazy(String url) {
        rulesMap.put(url, lazyAdapter);
    }

    public static void addRefresh(String url) {
        rulesMap.put(url, refreshAdapter);
    }


    public static void addPage(String url) {
        rulesMap.put(url, pageAdapter);
    }

    public static void addChain(String url, List<String> urls) {
        rulesMap.put(url, new ChainAdapter(urls));
    }

    static Map<String, String> mockMap = new ConcurrentHashMap<>();

    /**
     * url: 请求url（带query参数）如：http://cp-b2byjapp-sit.tob-trading-platform.sitapis.yonghui.cn/item/detail?a=b&c=d
     * requestBody: 请求json，可为空
     * responseJson: 返回json，不可为空
     */
    public static void addMocked(String url, String requestBody, String json) {
        if (requestBody == null) {
            requestBody = "";
        }
        String key = SecurityUtil.getMD5((url + requestBody).getBytes());
        mockMap.put(key, json);
    }

    public static String getMocked(String url, String requestBody) {
        if (requestBody == null) {
            requestBody = "";
        }
        String key = SecurityUtil.getMD5((url + requestBody).getBytes());
        return mockMap.get(key);
    }


}
