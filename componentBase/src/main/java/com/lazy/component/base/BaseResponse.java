package com.lazy.component.base;

import com.lazy.component.constant.ApiStatusCode;

/**
 * <p>BaseResponse </p>
 *
 * @author zdxiang
 * @date 2018-07-27 16:38
 */
public class BaseResponse<T> {

     private int statusCode;

     private String message;

     private T data;

    public boolean isSuccess() {
        return statusCode == ApiStatusCode.SUCCESS;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        data = data;
    }
}
