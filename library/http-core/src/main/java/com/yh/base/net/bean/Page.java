package com.yh.base.net.bean;

import java.util.List;

public class Page<T> {
    private String order;// ['ASC', 'DESC'],
    private String orderBy;
    private int pageNo;
    private int pageSize;
    private List<T> result;
    private int totalNum;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public boolean isLastPage() {
        return pageNo >= ((totalNum / pageSize) + (totalNum % pageSize > 0 ? 1 : 0));
    }
}
