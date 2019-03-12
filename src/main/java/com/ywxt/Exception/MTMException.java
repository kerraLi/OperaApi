package com.ywxt.Exception;

import com.ywxt.Enum.ReturnCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class MTMException extends RuntimeException implements Serializable {
    protected int code;
    protected String msg;

    public MTMException(int code, String msg) {
        super(msg);
        this.code = 525;
        this.msg = msg;
        this.code = code;
    }

    public MTMException(ReturnCode code) {
        super(code.getMessage());
        this.code = 525;
        this.msg = code.getMessage();
        this.code = code.getCode();
    }

    public MTMException(String msg) {
        super(msg);
        this.code = 525;
        this.msg = msg;
    }

    public MTMException() {
        this("服务器出了点意外...");
        this.msg = "服务器出了点意外...";
    }

    public MTMException(Exception cause) {
        super(cause);
        this.code = 525;
    }

    public MTMException(String msg, Exception cause) {
        super(msg, cause);
        this.code = 525;
        this.msg = msg;
    }
}
