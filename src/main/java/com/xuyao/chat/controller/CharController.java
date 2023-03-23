package com.xuyao.chat.controller;

import com.xuyao.chat.service.Client;
import com.xuyao.chat.service.Client1;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class CharController {

    private Client client;

    private Client1 client1;


    @RequestMapping("/msg")
    public String msg(){
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    @RequestMapping("/init")
    public String msg(@RequestParam String clientName){
        if ("client".equals(clientName)) {
            client = new Client();
            client.init();
        }else if("client1".equals(clientName)){
            client1 = new Client1();
            client1.init();
        }
        return "ok";
    }

    @RequestMapping("/sendMsg")
    public String msg(@RequestParam String clientName, String msg){
        if ("client".equals(clientName)) {
            Long toId = 2L;
            client.sendMessage(toId, msg);
        }else if("client1".equals(clientName)){
            Long toId = 1L;
            client1.sendMessage(toId, msg);
        }
        return "ok";
    }

}
