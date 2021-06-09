package com.yh.base.net.http.cache;

import com.yh.base.utils.SecurityUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemCacheManager {
    static Map<String, NetMemCache> memCacheManagerMap = new ConcurrentHashMap<>();

    public static NetMemCache getManager(String url) {
        String name = SecurityUtil.getMD5(url.getBytes());
        NetMemCache manager = memCacheManagerMap.get(name);
        if (manager == null) {
            synchronized (MemCacheManager.class) {
                manager = memCacheManagerMap.get(name);
                if (manager == null) {
                    manager = NetMemCache.create(name);
                    memCacheManagerMap.put(name, manager);
                }
            }
        }
        return manager;
    }
}
