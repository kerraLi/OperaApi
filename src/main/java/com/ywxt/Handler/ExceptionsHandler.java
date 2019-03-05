package com.ywxt.Handler;

import com.aliyuncs.exceptions.ClientException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;

// 统一异常处理类
@ControllerAdvice
public class ExceptionsHandler {

    // 校验参数异常捕获处理 todo
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(ConstraintViolationException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("status", "error");
        for (ConstraintViolation<?> s : e.getConstraintViolations()) {
            jsonObject.put("message", s.getInvalidValue() + ": " + s.getMessage());
        }
        return jsonObject.toString();
    }

    // 阿里云异常统一处理
    @ResponseBody
    @ExceptionHandler(ClientException.class)
    private String aliExceptionHandler(HttpServletRequest request, ClientException e) {
        String msg = "";
        if (e.getErrCode().equals("InvalidInstance.UnPaidOrder") && e.getErrMsg().equals("The specified Instance has unpaid order.")) {
            msg = "指定的实例有未支付的订单，请您先支付再进行操作。";
        } else if (e.getErrCode().equals("InvalidInstance.UnPaidOrder") && e.getErrMsg().equals("Unpaid order exists in your account, please complete or cancel the payment in the expense center.\t")) {
            msg = "您的账号里有未支付的订单。";
        } else if (e.getErrCode().equals("Forbidden.RAM") && e.getErrMsg().equals("User not authorized to operate on the specified resource, or this API doesn't support RAM.")) {
            msg = "子账号鉴权不通过。";
        } else if (e.getErrCode().equals("Forbidden.RAM") && e.getErrMsg().equals("User not authorized to operate on the specified resource")) {
            msg = "当前操作未被授权，请联系主账号进行授权后再执行操作。";
        } else if (e.getErrCode().equals("InvalidStatus.Upgrading") && e.getErrMsg().equals("The instance is upgrading; please try again later.")) {
            msg = "实例正在升级，请稍后重试。";
        } else if (e.getErrCode().equals("InvalidStatus.Upgrading") && e.getErrMsg().equals("The dedicated host is upgrading, please try it later.")) {
            msg = "宿主机续费中，请稍后操作。";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("status", "error");
        jsonObject.put("message", msg.isEmpty() ? e.getMessage() : msg);
        jsonObject.put("class", e.getClass());
        jsonObject.put("line", e.toString());
        jsonObject.put("path", request.getPathInfo());
        return jsonObject.toString();
    }


    // 可以直接写@EceptionHandler，IOExeption继承于Exception
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