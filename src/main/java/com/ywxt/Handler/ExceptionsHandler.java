package com.ywxt.Handler;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

// 统一异常处理类
@ControllerAdvice
public class ExceptionsHandler {

    //@ExceptionHandler(CustomGenericException.class)//可以直接写@ExceptionHandler,不指明异常类，会自动映射
    //public ModelAndView customGenericExceptionHnadler(CustomGenericException exception) { //还可以声明接收其他任意参数
    //    ModelAndView modelAndView = new ModelAndView("generic_error");
    //    modelAndView.addObject("errCode", exception.getErrCode());
    //    modelAndView.addObject("errMsg", exception.getErrMsg());
    //    return modelAndView;
    //}

    //可以直接写@EceptionHandler，IOExeption继承于Exception
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public String defaultExceptionHandler(HttpServletRequest request, Exception exception) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("status", "error");
        jsonObject.put("message", exception.getMessage());
        jsonObject.put("class", exception.getClass());
        jsonObject.put("line", exception.toString());
        jsonObject.put("path", request.getPathInfo());
        return jsonObject.toString();
    }
}
