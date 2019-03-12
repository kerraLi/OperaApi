package com.ywxt.Utils;

import com.ywxt.Exception.MTMException;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常工具类
 */
@Slf4j
public class ExceptionUtil {

    public static void isTrue(Boolean boole,String msg){
        if(boole){
            log.debug(msg);
            throw new MTMException(msg);
        }
    }

    public static void isNull(Object obj,String msg){
        if(obj==null){
            log.debug(msg);
            throw new MTMException(msg);
        }
    }

    public static void isNotNull(Object obj,String msg){
        if(obj!=null){
            log.debug(msg);
            throw new MTMException(msg);
        }
    }
}
