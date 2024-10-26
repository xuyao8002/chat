package com.xuyao.chat.bean.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRelationVO implements Serializable {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 好友id
     */
    private Long friendId;

    /**
     * 好友用户名
     */
    private String friendName;
}
