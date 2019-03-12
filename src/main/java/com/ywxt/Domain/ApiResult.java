package com.ywxt.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ywxt.Enum.ReturnCode;
import com.ywxt.Exception.MTMException;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ApiResult<T> implements Serializable {
    private int code;
    private String message;
    private T result;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    public ApiResult() {
        this(ReturnCode.C525.getCode(), "服务器出了点意外...", null);
    }

    public ApiResult(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
        this.time = new Date();
    }

    public ApiResult(int code, String message) {
        this(code, message, null);
    }

    public ApiResult(MTMException ex) {
        this(ex.getCode(), ex.getMessage(), null);
    }

    public ApiResult(ReturnCode returnCode, T result) {
        this(returnCode.getCode(), returnCode.getMessage(), result);
    }

    public ApiResult(ReturnCode returnCode) {
        this(returnCode, null);
    }

    public static ApiResult success() {
        return new ApiResult(ReturnCode.C200, (Object)null);
    }

    public static ApiResult success(String msg) {
        return new ApiResult(ReturnCode.C200.getCode(), msg, (Object)null);
    }

    public static ApiResult successWithObject(Object object, String message) {
        return new ApiResult(ReturnCode.C200.getCode(), message, object);
    }

    public static ApiResult successWithObject(Object object) {
        return new ApiResult(ReturnCode.C200.getCode(), ReturnCode.C200.getMessage(), object);
    }

    public static ApiResult failWithMessage(String message) {
        return new ApiResult(ReturnCode.C525.getCode(), message, (Object)null);
    }

    public static ApiResult fail() {
        return new ApiResult(ReturnCode.C525.getCode(), ReturnCode.C525.getMessage(), (Object)null);
    }

    public static ApiResult failWithMessageAndObject(String message, Object object) {
        return new ApiResult(ReturnCode.C525.getCode(), message, object);
    }

    public static ApiResult fail(ReturnCode returnCode) {
        return new ApiResult(returnCode, (Object)null);
    }

    public static ApiResult failWithException(MTMException ex) {
        return new ApiResult(ex);
    }
}
