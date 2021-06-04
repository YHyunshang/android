package com.yh.tobtrading.sdk.baidumap;

import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.List;

/**
 * @author POI搜索
 */
public class BDPoiSearchUtils implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    public PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch;

    private OnPoiSearchResultCallback mOnPoiSearchResultCallback;
    private OnSuggestionResultCallback mOnSuggestionResultCallback;
    private OnGetPoiDetailResultCallback mOnGetPoiDetailResultCallback;
    private OnSearchErrorCallback onSearchErrorCallback;

    private BDPoiSearchUtils() {
        init();
    }

    public static BDPoiSearchUtils newInstance() {
        return new BDPoiSearchUtils();
    }

    private void init() {
        mPoiSearch = PoiSearch.newInstance();    //创建POI搜索实例
        mSuggestionSearch = SuggestionSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }

    /**
     * 热点搜索结果
     *
     * @param mOnPoiSearchResultCallback
     */
    public void setOnPoiSearchResultCallback(OnPoiSearchResultCallback mOnPoiSearchResultCallback) {
        this.mOnPoiSearchResultCallback = mOnPoiSearchResultCallback;
    }

    /*
     * 搜索建议
     */
    public void setOnSuggestionResultCallback(OnSuggestionResultCallback mOnSuggestionResultCallback) {
        this.mOnSuggestionResultCallback = mOnSuggestionResultCallback;
    }

    /**
     * 搜搜详情
     *
     * @param mOnGetPoiDetailResultCallback
     */
    public void setOnGetPoiDetailResultCallback(OnGetPoiDetailResultCallback mOnGetPoiDetailResultCallback) {
        this.mOnGetPoiDetailResultCallback = mOnGetPoiDetailResultCallback;
    }

    /**
     * 搜索出错
     *
     * @param onSearchErrorCallback
     */
    public void setOnSearchErrorCallback(OnSearchErrorCallback onSearchErrorCallback) {
        this.onSearchErrorCallback = onSearchErrorCallback;
    }

    public void startSearch(String city, String keyWord, int pageNum) {
        startSearch(city, keyWord, pageNum, 10);
    }

    public void startSearch(String city, String keyWord, int pageNum, int pageCapacity) {
        LocationUtil.d("BDPoiSearchUtils startSearch city:" + city + ",keyWord:" + keyWord + ",pageNum:" + pageNum + ",pageCapacity:" + pageCapacity);
        mPoiSearch.searchInCity(new PoiCitySearchOption()
                .city(city)
                .keyword(keyWord)
                .pageCapacity(pageCapacity)
                .pageNum(pageNum));


    }

    public void requestSuggestion(String city, String keyWord) {
        LocationUtil.d("BDPoiSearchUtils startSearch city:" + city + ",keyWord:" + keyWord);
        mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                .city(city)
                .keyword(keyWord));
    }



    @Override
    public void onGetSuggestionResult(SuggestionResult result) {
        LocationUtil.d("BDPoiSearchUtils onGetSuggestionResult   result="+(result==null?null:result.error));
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            if (onSearchErrorCallback != null) {
                onSearchErrorCallback.onGetPoiDetailResult();
            }
            return;
        }
        if (mOnSuggestionResultCallback != null) {
            mOnSuggestionResultCallback.onSuggestionResult(result.getAllSuggestions());
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        LocationUtil.d("BDPoiSearchUtils onGetPoiDetailResult   result="+(result==null?null:result.error));
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            LocationUtil.d("BDPoiSearchUtils onGetPoiDetailResult 未找到结果");
            return;
        }
        if (mOnGetPoiDetailResultCallback != null) {
            mOnGetPoiDetailResultCallback.onGetPoiDetailResult(result);
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        LocationUtil.d("BDPoiSearchUtils onGetPoiDetailResult   result="+(poiDetailSearchResult==null?null:poiDetailSearchResult.error));
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        LocationUtil.d("BDPoiSearchUtils onGetPoiIndoorResult   result="+(poiIndoorResult==null?null:poiIndoorResult.error));
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        LocationUtil.d("BDPoiSearchUtils onGetPoiResult   result="+(result==null?null:result.error));
        if (mOnPoiSearchResultCallback != null) {
            mOnPoiSearchResultCallback.onPoiSearchResult(result);
        }
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            LocationUtil.d("BDPoiSearchUtils onGetPoiResult 未找到结果:" + result.error);
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            String strInfo = "在";
            for (CityInfo c : result.getSuggestCityList()) {
                strInfo += c.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            LocationUtil.d("BDPoiSearchUtils onGetPoiResult " + strInfo);
        }
    }

    public interface OnPoiSearchResultCallback {
        public void onPoiSearchResult(PoiResult result);
    }

    public interface OnSuggestionResultCallback {
        public void onSuggestionResult(List<SuggestionInfo> list);
    }

    public interface OnGetPoiDetailResultCallback {
        public void onGetPoiDetailResult(PoiDetailResult result);
    }

    public interface OnSearchErrorCallback {
        public void onGetPoiDetailResult();
    }

    public void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
    }


}