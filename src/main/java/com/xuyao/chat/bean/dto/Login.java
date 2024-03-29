package com.xuyao.chat.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class Login implements Serializable {

    @NotBlank
    private String userName;
    @NotBlank
    private String userPwd;
}
