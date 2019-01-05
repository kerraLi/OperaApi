package com.ywxt.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class Test {

    public static void main(String args[]) throws Exception {
        System.out.println("Hello World!");
        String date = "2019-02-07T16:00Z";
        date = date.replace("Z", " UTC");
        System.out.println(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm Z");
        Date d = format.parse(date);
        System.out.println(d);
    }
}
