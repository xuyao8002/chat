package com.xuyao.chat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyao.chat.bean.po.User;
import com.xuyao.chat.dao.UserMapper;
import com.xuyao.chat.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public boolean register(User user) {
        return super.save(user);
    }

    @Override
    public User getOne(Long userId) {
        return super.getOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
    }
}
