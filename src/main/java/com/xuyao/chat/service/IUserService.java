package com.xuyao.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyao.chat.bean.po.User;

public interface IUserService extends IService<User> {

    boolean register(User user);

    User getOne(Long userId);

}
