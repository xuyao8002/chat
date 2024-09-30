package com.xuyao.chat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuyao.chat.bean.dto.MessageDTO;
import com.xuyao.chat.bean.po.Message;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {
    List<Message> list(MessageDTO messageDTO);

}
