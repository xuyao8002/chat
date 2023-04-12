package com.xuyao.chat.component;

import com.xuyao.chat.bean.event.LoginEvent;
import com.xuyao.chat.service.Client;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginListener {

    @Resource
    private Client client;

    @EventListener
    public void onLogin(LoginEvent loginEvent){
        client.init(loginEvent.getUserId());
    }


}
