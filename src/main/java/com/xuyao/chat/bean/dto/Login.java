package com.xuyao.chat.bean.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Login implements Serializable {

    private String userName;

    private String userPwd;
}
