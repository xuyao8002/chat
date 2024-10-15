package com.xuyao.chat.service.impl;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import com.xuyao.chat.service.IBatchService;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Function;

@Service
public class BatchServiceImpl implements IBatchService {

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public <E> List<BatchResult> update(Class<?> mapperClass, List<E> elements, Function<E, Wrapper<E>> wrapperFunction) {
        List<BatchResult> results = transactionTemplate.execute(status -> {
            MybatisBatch.Method<E> mapperMethod = new MybatisBatch.Method<>(mapperClass);
            return MybatisBatchUtils.execute(sqlSessionFactory, elements, mapperMethod.update(wrapperFunction));
        });
        return results;
    }
}
