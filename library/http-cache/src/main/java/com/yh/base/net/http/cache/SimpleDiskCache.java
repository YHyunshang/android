package com.yh.base.net.http.cache;


import android.content.Context;

import com.bumptech.glide.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

public class SimpleDiskCache {

    public Entry get(String key) {
        try {
            DiskLruCache.Value value = mDiskLruCache.get(key);
            if (value == null) {
                return null;
            }
            String[] attrs = value.getString(1).split("\\|");
            return new Entry(value.getString(0), Long.parseLong(attrs[0]), Integer.parseInt(attrs[1]));
        } catch (Exception e) {
        }

        return null;
    }

    public void put(String key, String data, long time, int pageNo) {
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            editor.set(0, data);
            editor.set(1, time + "|" + pageNo);
            editor.commit();
            mDiskLruCache.flush();
        } catch (Exception e) {
        }
    }

    public void markDirty(String key) {
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            editor.set(1, "0|-1");
            editor.commit();
            mDiskLruCache.flush();
        } catch (Exception e) {
        }
    }

    public boolean remove(String key) {
        try {
            return mDiskLruCache.remove(key);
        } catch (Exception e) {
        }
        return false;
    }

    public void clear() {
        try {
            mDiskLruCache.delete();
        } catch (Exception e) {
        }
    }


    DiskLruCache mDiskLruCache;

    private SimpleDiskCache(Context context, String name, long size) throws IOException {
        mDiskLruCache = DiskLruCache.open(new File(context.getFilesDir().getAbsolutePath() + "/https/" + name + "/"), 0, 2, size);
    }

    public static SimpleDiskCache create(Context context, String name, long size) {
        try {
            return new SimpleDiskCache(context, name, size);
        } catch (Exception e) {
            return null;
        }
    }
}
