package com.xuyao.chat.controller;

import com.xuyao.chat.bean.Result;
import com.xuyao.chat.service.Client;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class CharController {

    @Resource
    private Client client;



    @RequestMapping("/msg")
    public Object msg(){
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Result.success(format.format(new Date()));
    }

    @RequestMapping("/init")
    public Object init(@RequestParam Long userId){
        client.init(userId);
        return Result.success("ok");
    }

    @RequestMapping("/sendMessage")
    public Object sendMessage(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam String msg){
        client.sendMessage(fromId, toId, msg);
        return Result.success("ok");
    }

}
