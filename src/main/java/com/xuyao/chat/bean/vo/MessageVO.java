package com.xuyao.chat.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVO {

    private Long id;

    /**
     * 发送方id
     */
    private Long fromId;

    /**
     * 接收方id
     */
    private Long toId;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 已读标识，0未读，1已读
     */
    private Integer isRead;

}
