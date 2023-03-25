package com.xuyao.chat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyao.chat.bean.dto.Login;
import com.xuyao.chat.bean.po.User;
import com.xuyao.chat.dao.UserMapper;
import com.xuyao.chat.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public boolean register(User user) {
        user.setUserPwd(DigestUtils.md5DigestAsHex(user.getUserPwd().getBytes()));
        return super.save(user);
    }

    @Override
    public boolean login(Login login) {
        User one = super.getOne(Wrappers.lambdaQuery(User.class).eq(User::getUserName, login.getUserName()));
        if(one == null || !Objects.equals(one.getUserPwd(), DigestUtils.md5DigestAsHex(login.getUserPwd().getBytes()))){
            throw new RuntimeException("用户名或密码错误");
        }
        return true;
    }

    @Override
    public User getOne(Long userId) {
        return super.getOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
    }
}
