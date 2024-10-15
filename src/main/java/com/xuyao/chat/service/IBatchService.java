package com.xuyao.chat.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.apache.ibatis.executor.BatchResult;

import java.util.List;
import java.util.function.Function;

public interface IBatchService {

    <E> List<BatchResult> update(Class<?> mapperClass, List<E> elements, Function<E, Wrapper<E>> wrapperFunction);

}
