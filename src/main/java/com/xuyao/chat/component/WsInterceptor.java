package com.xuyao.chat.component;

import com.xuyao.chat.bean.vo.UserVO;
import com.xuyao.chat.util.JsonUtil;
import com.xuyao.chat.util.RedisUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WsInterceptor implements HandshakeInterceptor {
 
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String token = servletRequest.getParameter("token");
        if (StringUtils.hasText(token)) {
            String userInfo = RedisUtil.get(token);
            if (StringUtils.hasText(userInfo)) {
                RedisUtil.set(token, userInfo, 120, TimeUnit.MINUTES);
                attributes.put("userId", JsonUtil.parseObject(userInfo, UserVO.class).getUserId());
                return true; // 表示继续握手过程
            }
        }
        return false;
    }
 
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {

    }
}