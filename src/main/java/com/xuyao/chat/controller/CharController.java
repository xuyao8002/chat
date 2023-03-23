package com.xuyao.chat.controller;

import com.xuyao.chat.service.Client;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class CharController {

    private Client client = new Client();



    @RequestMapping("/msg")
    public String msg(){
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    @RequestMapping("/init")
    public String init(@RequestParam Long userId){
        client.init(userId);
        return "ok";
    }

    @RequestMapping("/sendMessage")
    public String sendMessage(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam String msg){
        client.sendMessage(fromId, toId, msg);
        return "ok";
    }

}
