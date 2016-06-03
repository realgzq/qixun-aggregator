package com.qixun.common.ajax;


import com.qixun.util.GsonUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Created by guozq on 2016/1/18 17:41.
 */
public class AjaxResponse {
    private String status;
    private String errorCode;
    private String message;
    private Object data;

    public static final String SUCCESS = "0";
    public static final String FAIL = "1";

    public static AjaxResponse buildSuccessResp(){
        return buildSuccessResp(null);
    }


    public static AjaxResponse buildSuccessResp(Object data){
        return buildSuccessResp(null,data);
    }

    public static AjaxResponse buildSuccessResp(String message, Object data){
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setStatus(AjaxResponse.SUCCESS);
        if(message!=null){
            ajaxResponse.setMessage(message);
        }

        if(data!=null) {
            ajaxResponse.setData(data);
        }
        return ajaxResponse;
    }



    public static AjaxResponse buildFailResp(){
        return buildFailResp(null,null,null);
    }

    public static AjaxResponse buildFailResp(String message){
        return buildFailResp(null,message,null);
    }

    public static AjaxResponse buildFailResp(String errorCode, String message){
       return buildFailResp(errorCode,message,null);
    }

    public static AjaxResponse buildFailResp(String errorCode, String message,Object data){
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setStatus(AjaxResponse.FAIL);
        if(!StringUtils.isEmpty(message)) {
            ajaxResponse.setMessage(message);
        }
        if(!StringUtils.isEmpty(errorCode)) {
            ajaxResponse.setErrorCode(errorCode);
        }

        if(data!=null) {
            ajaxResponse.setData(data);
        }

        return ajaxResponse;
    }


    public String json(){
        return GsonUtils.toJson(this);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
