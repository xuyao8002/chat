package com.xuyao.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyao.chat.bean.dto.Login;
import com.xuyao.chat.bean.dto.Register;
import com.xuyao.chat.bean.po.User;
import com.xuyao.chat.bean.vo.LoginVO;

import java.util.List;
import java.util.Map;

public interface IUserService extends IService<User> {

    boolean register(Register register);

    LoginVO login(Login login);

    User get(Long userId);

    boolean exist(Long userId);

    List<User> list(List<Long> userIds);

    Map<Long, User> userMap(List<Long> userIds);

    boolean logout();

}
