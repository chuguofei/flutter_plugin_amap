package com.lczp.flutter_plugin_amap.model;

import java.util.Map;

/**
 * @author guofei
 * @date 11/23/20 4:29 PM
 */
public class AmapModel {

    private int errCode;
    private String errDetail;
    private String errMsg;
    private Map<String, Object> data;


    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrDetail() {
        return errDetail;
    }

    public void setErrDetail(String errDetail) {
        this.errDetail = errDetail;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
