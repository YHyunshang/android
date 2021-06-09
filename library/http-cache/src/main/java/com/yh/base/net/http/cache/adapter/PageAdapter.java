package com.yh.base.net.http.cache.adapter;

import com.google.gson.Gson;
import com.yh.base.net.http.cache.MemCacheManager;
import com.yh.base.utils.NetworkUtils;
import com.yh.base.utils.Util;

public class PageAdapter extends RootAdapter {
    public PageAdapter() {
    }

    public PageAdapter(long outTime) {
        this.outTime = outTime;
    }

    public class Req {
        public int page;
    }

    @Override
    public String computeID(CContext context) {
        String key = super.computeID(context);
        Req req = new Gson().fromJson(context.bodyData, Req.class);
        if (req != null) {
            context.setPageNo(req.page);
            key += req.page;
        }
        return key;
    }

    @Override
    public boolean onCache(CContext context) {
        if (context.getPageNo() == null)
            return false;
        if (NetworkUtils.isNetAvailable(Util.getApplication())) {
            String key = super.computeID(context);
            for (int i = context.getPageNo(); ; i++) {
                if (!MemCacheManager.getManager(context.url).remove(key + i)) {
                    break;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public void onResult(CContext context, String responseData) {
        MemCacheManager.getManager(context.getUrl()).put(context.id, responseData, System.currentTimeMillis(), context.pageNo);
    }

    @Override
    public void makeDirty(String url) {
        MemCacheManager.getManager(url).clear();
    }
}