package com.zx.exception;


import com.zx.spzx.model.vo.common.ResultCodeEnum;
import lombok.Data;


@Data
public class ZxException  extends  RuntimeException{

    private Integer code;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    public ZxException (ResultCodeEnum resultCodeEnum){
        this.resultCodeEnum = resultCodeEnum;
        this.code  = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

}
