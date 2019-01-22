package com.ywxt.Utils;

import java.io.Serializable;

/***
 *
 * @Author:
 * @Description:
 * @date: 2019/11/22
 *
 ****/
public class Result implements Serializable {

    private boolean success;

    private String message;

    public Result(boolean success) {
        this.success = success;
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
