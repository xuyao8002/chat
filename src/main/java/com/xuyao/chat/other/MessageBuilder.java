package com.xuyao.chat.other;

import com.xuyao.chat.bean.dto.Message;

public class MessageBuilder {

    public static Message build(Long fromId, Long toId, String msg){
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setMsg(msg);
        return message;
    }

}
