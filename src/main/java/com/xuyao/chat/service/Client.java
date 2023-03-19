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

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class Client {

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private static ClientHandler handler;

    public static void sendMsg(String message){
        ChannelHandlerContext context = handler.getContext();
        byte[] req = (message + System.getProperty("line.separator")).getBytes(CHARSET_UTF8);
        ByteBuf msg = context.alloc().buffer();
        msg.writeBytes(req);
        context.channel().writeAndFlush(msg);
    }

    public void connect(int port, String host) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
        handler = new ClientHandler();
        try {
            client.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
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
    public static void main(String[] args) {
        int port = 8888;
        Client client = new Client();
        try {
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Message message = new Message();
                message.setType(2);
                message.setFromId(1L);
                message.setToId(2L);
                message.setMsg("生日快乐！\r\n哈哈！");
                sendMsg(JsonUtil.toString(message));
            }).start();
            client.connect(port, "localhost");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ClientHandler extends ChannelInboundHandlerAdapter {
        private ChannelHandlerContext context;
//        private String sendMsg = "你好啊！";
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            this.context = ctx;
            Message message = new Message();
            message.setFromId(1L);
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
            System.out.println("read from server: " + body);
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