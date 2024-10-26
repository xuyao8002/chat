package com.xuyao.chat.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息表
 */
@Data
@TableName("message")
public class Message {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送方id
     */
    @TableField("from_id")
    private Long fromId;

    /**
     * 接收方id
     */
    @TableField("to_id")
    private Long toId;

    /**
     * 消息内容
     */
    @TableField("msg")
    private String msg;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 消息类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 删除标识，0未删除，1已删除
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 已读标识，0未读，1已读
     */
    @TableField("is_read")
    private Integer isRead;

}
