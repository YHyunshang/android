package com.yh.base.net.http.cache;

import androidx.collection.LruCache;

import com.yh.base.utils.Util;

public class NetMemCache {
    public void put(String id, String responseData, long time, int pageNo) {
        Entry entry = lruCache.get(id);
        if (entry != null) {
            entry.value = responseData;
            entry.time = time;
            entry.pageNo = pageNo;
        }
        diskCache.put(id, responseData, time, pageNo);
    }

    public Entry get(String id) {
        try {
            Entry entry = lruCache.get(id);
            if (entry != null) {
                return entry;
            }
            entry = diskCache.get(id);
            if (entry != null) {
                lruCache.put(id, entry);
                return entry;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public void markDirty(String key) {
        Entry entry = lruCache.get(key);
        if (entry != null) {
            entry.setTime(0);
            entry.setPageNo(-1);
        }
        diskCache.markDirty(key);
    }

    public boolean remove(String key) {
        boolean success = lruCache.remove(key) != null;
        if (diskCache.remove(key)) {
            success = true;
        }
        return success;
    }

    LruCache<String, Entry> lruCache;
    SimpleDiskCache diskCache;

    private NetMemCache(String name, int memEntryCount, long fileSize) {
        diskCache = SimpleDiskCache.create(Util.getApplication(), name, fileSize);
        lruCache = new LruCache<>(memEntryCount);
    }

    public void clear() {
        lruCache.evictAll();
        diskCache.clear();
    }

    public static NetMemCache create(String name, int memEntryCount, long fileSize) {
        return new NetMemCache(name, memEntryCount, fileSize);
    }

    public static NetMemCache create(String name) {
        return new NetMemCache(name, 100, 10 * 1024 * 1024);
    }
}
