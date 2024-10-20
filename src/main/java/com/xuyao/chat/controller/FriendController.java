package com.xuyao.chat.controller;

import com.xuyao.chat.bean.Result;
import com.xuyao.chat.service.IUserRelationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(("/friend"))
public class FriendController {

    @Resource
    private IUserRelationService userRelationService;

    @PostMapping("/add")
    public Object add(@RequestParam Long friendId){
        return Result.success(userRelationService.add(friendId));
    }

    @GetMapping("/list")
    public Object list(@RequestParam Long lastId, @RequestParam Integer size){
        return Result.success(userRelationService.list(lastId, size));
    }

    @PostMapping("/delete")
    public Object delete(@RequestParam Long friendId){
        return Result.success(userRelationService.delete(friendId));
    }

}
