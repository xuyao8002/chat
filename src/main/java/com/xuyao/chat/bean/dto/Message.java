package com.xuyao.chat.bean.dto;

import lombok.Data;

@Data
public class Message {

    private Long fromId;
    private Long toId;
    private String msg;

}
