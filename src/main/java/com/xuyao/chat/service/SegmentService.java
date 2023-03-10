package com.xuyao.chat.service;

import com.xuyao.chat.bean.vo.Result;
import com.xuyao.chat.dao.IDAllocDao;
import com.xuyao.chat.dao.impl.IDAllocDaoImpl;
import com.xuyao.chat.service.impl.SegmentIDGenImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

@Service("SegmentService")
public class SegmentService {
    private Logger logger = LoggerFactory.getLogger(SegmentService.class);

    private IDGen idGen;

    @Resource
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        // Config Dao
        IDAllocDao dao = new IDAllocDaoImpl(dataSource);

        // Config ID Gen
        idGen = new SegmentIDGenImpl();
        ((SegmentIDGenImpl) idGen).setDao(dao);
        if (idGen.init()) {
            logger.info("Segment Service Init Successfully");
        } else {
            throw new RuntimeException("Segment Service Init Fail");
        }
    }

    public Result getId(String key) {
        return idGen.get(key);
    }

    public SegmentIDGenImpl getIdGen() {
        if (idGen instanceof SegmentIDGenImpl) {
            return (SegmentIDGenImpl) idGen;
        }
        return null;
    }
}
