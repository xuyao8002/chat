package com.xuyao.chat.bean.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private Long id;

    private Long userId;

    private String userName;

    private String token;
}
