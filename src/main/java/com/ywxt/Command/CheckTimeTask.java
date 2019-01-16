package com.ywxt.Command;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CheckTimeTask {

    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        //callable - 要执行的功能
        //delay - 从现在开始延迟执行的时间
        //unit - 延迟参数的时间单位

        // ali refresh
        service.scheduleAtFixedRate(new CheckThread("ali", "refresh"), 3, 60 * 60 * 5, TimeUnit.SECONDS);
        // go refresh
        service.scheduleAtFixedRate(new CheckThread("godaddy", "refresh"), 3, 60 * 60 * 5, TimeUnit.SECONDS);


        // ali account
        service.scheduleAtFixedRate(new CheckThread("ali", "account"), 60, 60 * 60, TimeUnit.SECONDS);
        // ali ecsExpired
        service.scheduleAtFixedRate(new CheckThread("ali", "ecsExpired"), 60 * 5, 60 * 60 * 5, TimeUnit.SECONDS);
        // go domain
        service.scheduleAtFixedRate(new CheckThread("godaddy", "domain"), 60 * 5, 60 * 60 * 5, TimeUnit.SECONDS);
        // go certificate
        service.scheduleAtFixedRate(new CheckThread("godaddy", "certificate"), 60 * 5, 60 * 60 * 5, TimeUnit.SECONDS);
    }

}
