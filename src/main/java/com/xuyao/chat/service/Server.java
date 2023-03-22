package com.xuyao.chat.service;

import com.xuyao.chat.bean.dto.Message;
import com.xuyao.chat.util.JsonUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Server {

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private static Map<Long, ChannelHandlerContext> contextMap = new ConcurrentHashMap<>();

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

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            ch.pipeline()
                    //换行符
                    .addLast(new LineBasedFrameDecoder(500))
                    .addLast(new ServerHandler());
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8888;
        new Server().bind(port);
    }

    @PostConstruct
    public void init() {
        int port = 8888;
        new Thread(() -> {
            try {
                new Server().bind(port);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    class ServerHandler extends SimpleChannelInboundHandler<Object> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
            ByteBuf buf = (ByteBuf) msg;
            String body = buf.toString(CHARSET_UTF8);
            System.out.println("read from client : " + body);
            Message message = JsonUtil.parseObject(body, Message.class);
            if (Objects.equals(message.getType(), 1)) {
                contextMap.put(message.getFromId(), ctx);
                System.out.println("user注册了,userId:" + message.getFromId() + "," + ctx);
            }else{
                ChannelHandlerContext ctxTo = contextMap.get(message.getToId());
                if (ctxTo != null) {
                    //换行符
                    body = body + System.getProperty("line.separator");
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
