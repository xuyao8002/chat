package com.xuyao.chat.service;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.nio.charset.Charset;

public class Client {

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    public void connect(int port, String host) throws Exception{

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
        try {
            client.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LineBasedFrameDecoder(50))
                                    .addLast(new ClientHandler());
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
            client.connect(port, "localhost");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ClientHandler extends ChannelInboundHandlerAdapter {
        private String sendMsg = "你好啊！";
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            //换行符
            byte[] req = (sendMsg + System.getProperty("line.separator")).getBytes(CHARSET_UTF8);
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
    }
}