package com.xuyao.chat.controller;

import com.xuyao.chat.bean.Result;
import com.xuyao.chat.bean.dto.Login;
import com.xuyao.chat.bean.dto.Register;
import com.xuyao.chat.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(("/user"))
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/register")
    public Object register(@RequestBody @Valid Register register){
        return Result.success(userService.register(register));
    }

    @PostMapping("/login")
    public Object login(@RequestBody @Valid Login login){
        return Result.success(userService.login(login));
    }

}
