package com.xuyao.chat.util;

import com.xuyao.chat.bean.vo.UserVO;

public class ContextUtil {

    static ThreadLocal<UserVO> local = new ThreadLocal<>();

    public static void setUser(UserVO user){
        local.set(user);
    }

    public static UserVO getUser(){
        return local.get();
    }

}
