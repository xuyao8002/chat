package com.xuyao.chat.service;


import com.xuyao.chat.bean.vo.Result;

public interface IDGen {
    Result get(String key);
    boolean init();
}
