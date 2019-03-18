package com.ywxt.Utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 把一天的时间变成这一天最后的时间 23:59:59
     * @param date
     * @return
     */
    public static Date setEndDate(Date date){
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }
}
