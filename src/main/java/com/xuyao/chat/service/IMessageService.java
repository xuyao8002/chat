package com.xuyao.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyao.chat.bean.dto.MessageDTO;
import com.xuyao.chat.bean.po.Message;
import com.xuyao.chat.bean.vo.MessageVO;

import java.util.List;

public interface IMessageService extends IService<Message> {

    boolean add(Message message);

    List<MessageVO> list(MessageDTO messageDTO);

    void read(Long fromId);

    void read(List<Message> messages);

    Long unreadCount(Long fromId);

}
