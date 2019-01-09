package com.ywxt.Utils;

import com.ywxt.Handler.AsyncHandler;

public class AsyncUtils {

    // 异步调用
    public static void asyncWork(AsyncHandler handler) {
        handler.callback();
    }
}
