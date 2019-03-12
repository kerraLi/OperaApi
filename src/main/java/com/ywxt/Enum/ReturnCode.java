package com.ywxt.Enum;

public enum ReturnCode {
    C200(200, "操作成功"),
    C505(505, "对不起，您的服务已经过期！如须操作，请进行续费"),
    C525(525, "操作失败");

    private final int code;
    private final String message;

    private ReturnCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
