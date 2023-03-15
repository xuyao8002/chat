package com.xuyao.chat;

import com.xuyao.chat.bean.vo.Result;
import com.xuyao.chat.bean.vo.Results;
import com.xuyao.chat.service.SegmentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = ChatApplication.class)
class ChatApplicationTests {

    @Resource
    private SegmentService segmentService;

    @Test
    void contextLoads() {
        Result userId = segmentService.getId("userId");
        System.out.println(userId);
        Results userIds = segmentService.getIdList("userId", 10);
        System.out.println(userIds);
    }

}
