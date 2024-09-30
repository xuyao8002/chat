package com.xuyao.chat.bean.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRelationVO implements Serializable {

    private Long id;

    private Long userId;

    private Long friendId;

    private String friendName;
}
