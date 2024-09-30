package com.xuyao.chat.controller;

import com.xuyao.chat.bean.Result;
import com.xuyao.chat.bean.dto.MessageDTO;
import com.xuyao.chat.service.IMessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private IMessageService messageService;



    @GetMapping("/list")
    public Object list(MessageDTO messageDTO){
        return Result.success(messageService.list(messageDTO));
    }

    @PostMapping("/read")
    public Object read(@RequestParam Long fromId){
        messageService.read(fromId);
        return Result.success();
    }

    @GetMapping("/unreadCount")
    public Object unreadCount(@RequestParam Long fromId){
        return Result.success(messageService.unreadCount(fromId));
    }
}
