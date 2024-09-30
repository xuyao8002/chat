package com.xuyao.chat.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {

    private Long fromId;
    private Long toId;
    private String msg;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer type;

    private Long lastId;
    private Integer pageSize;

}
