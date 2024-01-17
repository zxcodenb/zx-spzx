package com.zx.exception;

import com.zx.spzx.model.vo.common.Result;
import com.zx.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public Result error(Exception e){
//        e.printStackTrace();
//        return Result.build(null ,  ResultCodeEnum.SYSTEM_ERROR) ;
//    }



    @ExceptionHandler(ZxException.class)
    @ResponseBody
    public Result error(ZxException e){
        e.printStackTrace();
        return Result.build(null ,  ResultCodeEnum.SYSTEM_ERROR) ;
    }
}
