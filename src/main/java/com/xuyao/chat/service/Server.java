package com.xuyao.chat.service;

import com.xuyao.chat.bean.dto.MessageDTO;
import com.xuyao.chat.util.JsonUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class Server {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    private static final Map<Long, ChannelHandlerContext> contextMap = new ConcurrentHashMap<>();

    private static final int port = 8888;

    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChildChannelHandler());
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            ch.pipeline()
            //换行符
            .addLast(new LineBasedFrameDecoder(500))
            .addLast(new ServerHandler());
        }
    }

    @PostConstruct
    public void init() {
        new Thread(() -> {
            try {
                new Server().bind(port);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    static class ServerHandler extends SimpleChannelInboundHandler<Object> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
            ByteBuf buf = (ByteBuf) msg;
            String body = buf.toString(CHARSET_UTF8);
            log.info("服务端收到消息：{}", body);
            MessageDTO messageDTO = JsonUtil.parseObject(body, MessageDTO.class);
            if (Objects.equals(messageDTO.getType(), 1)) {
                contextMap.put(messageDTO.getFromId(), ctx);
                log.info("userId:{}注册了", messageDTO.getFromId());
            }else{
                ChannelHandlerContext ctxTo = contextMap.get(messageDTO.getToId());
                if (ctxTo != null) {
                    //换行符
                    body = body + System.lineSeparator();
                    ByteBuf resp = Unpooled.copiedBuffer(body.getBytes(CHARSET_UTF8));
                    ctxTo.writeAndFlush(resp);
                }
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
        }
    }
}
