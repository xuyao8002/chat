package com.xuyao.chat.component;

import com.xuyao.chat.bean.po.Message;
import com.xuyao.chat.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageWebSocketHandler.class);
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<Object, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("接收的消息：{}", message.getPayload());
        Message msg = JsonUtil.parseObject(message.getPayload(), Message.class);
        msg.setType(2);
        msg.setFromId((Long) session.getAttributes().get("userId"));
        msg.setCreateTime(LocalDateTime.now());
        msg.setIsRead(0);
        applicationEventPublisher.publishEvent(msg);
        WebSocketSession toSession;
        if((toSession = sessionMap.get(msg.getToId())) != null){
            TextMessage textMessage = new TextMessage(JsonUtil.toString(msg));
            try {
                toSession.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            logger.warn("未找到消息接收者，msg:{}", JsonUtil.toString(msg));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("afterConnectionEstablished，userId:{}", session.getAttributes().get("userId"));
        sessionMap.put(session.getAttributes().get("userId"), session);
        // 连接建立后的逻辑
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketSession removedSession = sessionMap.remove(session.getAttributes().get("userId"));
        removedSession.close();
        logger.info("afterConnectionClosed, uri:{}", removedSession.getUri());
    }

    public MessageWebSocketHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}