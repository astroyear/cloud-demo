package com.atguigu.order.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ResponseBody
//@ControllerAdvice

@RestControllerAdvice   //全局异常处理器
public class GlobalExceptionHandler {

//    @ExceptionHandler(Throwable.class)
//    public String error(Throwable e){
//        return "";
//    }
}
