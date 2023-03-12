package com.xuyao.chat.service;


import com.xuyao.chat.bean.vo.Result;
import com.xuyao.chat.bean.vo.Results;

public interface IDGen {
    Result get(String key);
    Results getList(String key, int count);
    boolean init();
}
