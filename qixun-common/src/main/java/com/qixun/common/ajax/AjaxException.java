package com.qixun.common.ajax;

/**
 * Created by guozq on 2016/1/18 17:38.
 */
public class AjaxException extends Exception {
    private String code;


    public AjaxException(String message) {
        super(message);
        this.code="999";
    }

    public AjaxException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
