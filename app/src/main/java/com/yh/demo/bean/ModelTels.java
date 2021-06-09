package com.yh.demo.bean;

import java.util.List;

/**
 * @description 获取客服电话bean
 * @date: 6/3/21 4:30 PM
 * @author: 张致远
 */

public class ModelTels {
    List<String> tel;//电话 ,
    int type;//电话类型，1平台；2店铺

    public List<String> getTel() {
        return tel;
    }

    public void setTel(List<String> tel) {
        this.tel = tel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}