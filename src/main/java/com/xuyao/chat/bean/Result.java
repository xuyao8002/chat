package com.xuyao.chat.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Result {

    private static int DEFAULT_ERROR_CODE = 0;
    private static int DEFAULT_SUCCESS_CODE = 1;

    private int code;

    private String message;

    private Object data;

    public static Result error(String message){
        return new Result(DEFAULT_ERROR_CODE, message, null);
    }

    public static Result success(Object data){
        return new Result(DEFAULT_SUCCESS_CODE, null, data);
    }

    public static Result success(){
        return new Result(DEFAULT_SUCCESS_CODE, null, null);
    }

}
