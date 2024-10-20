package com.xuyao.chat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyao.chat.bean.dto.MessageDTO;
import com.xuyao.chat.bean.po.Message;
import com.xuyao.chat.bean.vo.MessageVO;
import com.xuyao.chat.bean.vo.UserVO;
import com.xuyao.chat.dao.MessageMapper;
import com.xuyao.chat.service.IBatchService;
import com.xuyao.chat.service.IMessageService;
import com.xuyao.chat.util.ContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private IBatchService batchService;

    @Override
    public boolean add(Message message) {
        return super.save(message);
    }

    @Override
    public List<MessageVO> list(MessageDTO messageDTO) {
        UserVO user = ContextUtil.getUser();
        messageDTO.setFromId(user.getUserId());
        List<Message> list = baseMapper.list(messageDTO);
        return list.stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(message, messageVO);
            return messageVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void read(Long fromId) {
        UserVO user = ContextUtil.getUser();
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(user.getUserId());
        message.setIsRead(1);
        applicationEventPublisher.publishEvent(message);
    }

    @Override
    public void read(List<Message> messages) {
        batchService.update(MessageMapper.class, messages, message -> Wrappers.lambdaUpdate(Message.class).eq(Message::getFromId, message.getFromId())
                    .eq(Message::getToId, message.getToId()).eq(Message::getIsDelete, 0)
                    .eq(Message::getIsRead, 0).set(Message::getIsRead, 1));
    }

    @Override
    public Long unreadCount(Long fromId) {
        UserVO user = ContextUtil.getUser();
        return this.baseMapper.selectCount(Wrappers.lambdaQuery(Message.class)
                .eq(Message::getFromId, fromId).eq(Message::getToId, user.getUserId())
                .eq(Message::getIsDelete, 0).eq(Message::getIsRead, 0)
                .last("limit 100"));
    }

    @Override
    public void delete(List<Message> messages) {
        messages.forEach(message -> super.remove(Wrappers.lambdaUpdate(Message.class).eq(Message::getFromId, message.getFromId()).eq(Message::getToId, message.getToId())
                .or(up -> up.eq(Message::getFromId, message.getToId()).eq(Message::getToId, message.getFromId()))));
    }
}
