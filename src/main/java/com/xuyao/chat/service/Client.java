package com.xuyao.chat.service;

import com.xuyao.chat.bean.dto.MessageDTO;
import com.xuyao.chat.bean.vo.UserVO;
import com.xuyao.chat.util.ContextUtil;
import com.xuyao.chat.util.JsonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class Client {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
    private static ClientHandler handler;
    private static final Map<Long, ClientHandler> handlerMap = new HashMap<>();
    private static final int port = 8888;
    private static final String ip = "localhost";

    public void connect(int port, String host, Long userId) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
        handler = new ClientHandler(userId);
        handlerMap.put(userId, handler);
        try {
            client.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline()
                    .addLast(new LineBasedFrameDecoder(500))
                    .addLast(handler);
                }
            });
            ChannelFuture future = client.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void init(Long userId){
        if (!handlerMap.containsKey(userId)) {
            Client client = new Client();
            new Thread(() -> {
                try {
                    client.connect(port, ip, userId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public void sendMessage(Long toId, String msg){
        UserVO user = ContextUtil.getUser();
        Long fromId = user.getUserId();
        if (!handlerMap.containsKey(fromId)) {
            throw new RuntimeException(String.format("用户%s未登录", fromId));
        }
        if (!handlerMap.containsKey(toId)) {
            throw new RuntimeException(String.format("目标用户%s未登录", toId));
        }
        if (Objects.equals(fromId, toId)) {
            throw new RuntimeException(String.format("用户%s不能给自己发消息", fromId));
        }
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType(2);
        messageDTO.setMsg(msg);
        messageDTO.setFromId(fromId);
        messageDTO.setToId(toId);
        messageDTO.setCreateTime(LocalDateTime.now());
        ChannelHandlerContext context = handlerMap.get(fromId).getContext();
        byte[] req = (JsonUtil.toString(messageDTO) + System.lineSeparator()).getBytes(CHARSET_UTF8);
        ByteBuf buf = context.alloc().buffer();
        buf.writeBytes(req);
        context.channel().writeAndFlush(buf);
    }

    static class ClientHandler extends ChannelInboundHandlerAdapter {
        public ClientHandler(Long userId) {
            this.userId = userId;
        }

        private final Long userId;
        private ChannelHandlerContext context;
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            this.context = ctx;
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setFromId(userId);
            messageDTO.setType(1);
            messageDTO.setCreateTime(LocalDateTime.now());
            //换行符
            byte[] req = (JsonUtil.toString(messageDTO) + System.lineSeparator()).getBytes(CHARSET_UTF8);
            ByteBuf msg = ctx.alloc().buffer();
            msg.writeBytes(req);
            ctx.channel().writeAndFlush(msg);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf buf = (ByteBuf)msg;
            String body = buf.toString(CHARSET_UTF8);
            MessageDTO messageDTO = JsonUtil.parseObject(body, MessageDTO.class);
            log.info("用户:{}收到用户:{}的消息:{}，时间:{}", userId, messageDTO.getFromId(), messageDTO.getMsg(), messageDTO.getCreateTime());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
        }

        public ChannelHandlerContext getContext() {
            return context;
        }
    }
}