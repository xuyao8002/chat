package com.xuyao.chat.controller;

import com.xuyao.chat.bean.Result;
import com.xuyao.chat.service.IUserRelationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(("/friend"))
public class FriendController {

    @Resource
    private IUserRelationService userRelationService;

    @PostMapping("/add")
    public Object register(@RequestParam Long friendId){
        return Result.success(userRelationService.add(friendId));
    }

}
