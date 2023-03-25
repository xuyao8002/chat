package com.xuyao.chat.component;


import com.xuyao.chat.bean.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseHandler implements ResponseBodyAdvice {
   @Override
   public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
   }

   @Override
   public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
       if(o instanceof Result){
           return o;
       }
       return Result.success(o);
   }

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(Exception e){
        e.printStackTrace();
        return Result.error(e.getMessage());
    }
}