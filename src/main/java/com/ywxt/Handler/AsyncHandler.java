package com.ywxt.Handler;

public abstract class AsyncHandler {

    // 内部调用新线程达到异步处理
    public void callback() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                handle();
            }
        });
        thread.start();
    }

    abstract public void handle();
}
