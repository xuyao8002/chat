package com.xuyao.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyao.chat.bean.dto.Login;
import com.xuyao.chat.bean.dto.Register;
import com.xuyao.chat.bean.po.User;

public interface IUserService extends IService<User> {

    boolean register(Register register);

    String login(Login login);

    User getOne(Long userId);

    boolean exist(Long userId);

}
