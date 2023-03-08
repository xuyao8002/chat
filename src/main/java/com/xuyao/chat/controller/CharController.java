package com.xuyao.chat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class CharController {

    @RequestMapping("/msg")
    public String msg(){
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

}
