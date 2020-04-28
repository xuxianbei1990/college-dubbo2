package college.dubbo.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 英雄传说
 *  http://cdn0001.afrxvk.cn/hero_story/demo/step010/index.html?serverAddr=127.0.0.1:12345&userId=1
 * @author: xuxianbei
 * Date: 2020/4/26
 * Time: 20:17
 * Version:V1.0
 *
 * protoc.exe --java_out=${目标目录} .\GameMsgProtocol.proto
 */
@Slf4j
public class ServerMain {
    public static void main(String[] args) {
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boosGroup, workGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(
                        //Http 服务器编解码器
                        new HttpServerCodec(),
                        // 内容长度限制
                        new HttpObjectAggregator(65535),
                        // WebSocket 协议处理器, 在这里处理握手、ping、pong 等消息
                        new WebSocketServerProtocolHandler("/websocket"),
                        //自定义消息解码
                        new GameMsgDecoder(),
                        new GameMsgHandler(),
                        new GameMsgEncoder()
                );
            }
        });
        try {
            ChannelFuture channelFuture = bootstrap.bind(12345).sync();
            if (channelFuture.isSuccess()) {
                log.info("服务器启动");
            }
            // 等待服务器信道关闭,
            // 也就是不要退出应用程序,
            // 让应用程序可以一直提供服务
            channelFuture.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
