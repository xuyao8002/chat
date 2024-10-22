package com.xuyao.chat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyao.chat.bean.dto.Login;
import com.xuyao.chat.bean.dto.Register;
import com.xuyao.chat.bean.po.User;
import com.xuyao.chat.bean.vo.LoginVO;
import com.xuyao.chat.bean.vo.UserVO;
import com.xuyao.chat.dao.UserMapper;
import com.xuyao.chat.service.IUserService;
import com.xuyao.chat.service.SegmentService;
import com.xuyao.chat.util.JsonUtil;
import com.xuyao.chat.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private SegmentService segmentService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public boolean register(Register register) {
        User one = super.getOne(Wrappers.lambdaQuery(User.class).select(User::getUserId).eq(User::getUserName, register.getUserName()));
        if(one != null){
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUserId(segmentService.getId("userId").getId());
        user.setUserName(register.getUserName());
        user.setUserPwd(DigestUtils.md5DigestAsHex(register.getUserPwd().getBytes()));
        return super.save(user);
    }

    @Override
    public LoginVO login(Login login) {
        User one = super.getOne(Wrappers.lambdaQuery(User.class).eq(User::getUserName, login.getUserName()));
        if(one == null || !Objects.equals(one.getUserPwd(), DigestUtils.md5DigestAsHex(login.getUserPwd().getBytes()))){
            throw new RuntimeException("用户名或密码错误");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(one, userVO);
        String token = UUID.randomUUID().toString().replace("-", "");
        RedisUtil.set(token, JsonUtil.toString(userVO),120, TimeUnit.MINUTES);
//        applicationEventPublisher.publishEvent(new LoginEvent(one.getUserId()));
        return new LoginVO(one.getUserId(), token);
    }

    @Override
    public User get(Long userId) {
        return super.getOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
    }

    @Override
    public boolean exist(Long userId) {
        return super.getOne(Wrappers.lambdaQuery(User.class).select(User::getUserId).eq(User::getUserId, userId)) != null;
    }

    @Override
    public List<User> list(List<Long> userIds) {
        return super.list(Wrappers.lambdaQuery(User.class).in(User::getUserId, userIds));
    }

    @Override
    public Map<Long, User> userMap(List<Long> userIds) {
        List<User> list = list(userIds);
        return list.stream().collect(Collectors.toMap(User::getUserId, user -> user));
    }
}
