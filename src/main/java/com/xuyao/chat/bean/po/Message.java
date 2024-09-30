package com.xuyao.chat.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("from_id")
    private Long fromId;
    @TableField("to_id")
    private Long toId;
    @TableField("msg")
    private String msg;
    @TableField("create_time")
    private LocalDateTime createTime = LocalDateTime.now();
    @TableField("type")
    private Integer type;
    @TableField("is_delete")
    private Integer isDelete;
    @TableField("is_read")
    private Integer isRead;

}
