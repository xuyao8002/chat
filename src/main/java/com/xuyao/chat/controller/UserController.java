package com.xuyao.chat.controller;

import com.xuyao.chat.bean.dto.Login;
import com.xuyao.chat.bean.po.User;
import com.xuyao.chat.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(("/user"))
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/register")
    public Object register(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public Object login(@RequestBody Login login){
        return userService.login(login);
    }

}
