package com.xuyao.chat.service;

import com.xuyao.chat.bean.dto.Message;
import com.xuyao.chat.util.JsonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Client {

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    private static ClientHandler handler;
    private static Map<Long, ClientHandler> handlerMap = new HashMap<>();
    private int port = 8888;
    private String ip = "localhost";

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

    public void sendMessage(Long fromId, Long toId, String msg){
        Message message = new Message();
        message.setType(2);
        message.setMsg(msg);
        message.setFromId(fromId);
        message.setToId(toId);
        ChannelHandlerContext context = handlerMap.get(fromId).getContext();
        byte[] req = (JsonUtil.toString(message) + System.getProperty("line.separator")).getBytes(CHARSET_UTF8);
        ByteBuf buf = context.alloc().buffer();
        buf.writeBytes(req);
        context.channel().writeAndFlush(buf);
    }

    class ClientHandler extends ChannelInboundHandlerAdapter {
        public ClientHandler(Long userId) {
            this.userId = userId;
        }

        private Long userId;
        private ChannelHandlerContext context;
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            this.context = ctx;
            Message message = new Message();
            message.setFromId(userId);
            message.setType(1);
            //换行符
            byte[] req = (JsonUtil.toString(message) + System.getProperty("line.separator")).getBytes(CHARSET_UTF8);
            ByteBuf msg = ctx.alloc().buffer();
            msg.writeBytes(req);
            ctx.channel().writeAndFlush(msg);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf buf = (ByteBuf)msg;
            String body = buf.toString(CHARSET_UTF8);
            Message message = JsonUtil.parseObject(body, Message.class);
            log.info("用户:{}收到用户:{}的消息:{}，时间:{}", userId, message.getFromId(), message.getMsg(), message.getTime());
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