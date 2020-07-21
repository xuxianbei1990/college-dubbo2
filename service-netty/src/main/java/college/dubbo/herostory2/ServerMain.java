package college.dubbo.herostory2;

import college.dubbo.herostory2.cmdHandler.CmdHandlerFactory;
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
 * @author: xuxianbei
 * Date: 2020/4/30
 * Time: 20:23
 * Version:V1.0
 */
@Slf4j
public class ServerMain {
    public static void main(String[] args) {
        CmdHandlerFactory.init();
        GameMsgRecognizer.init();
        EventLoopGroup boos = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boos, work).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpObjectAggregator(65535),
                                new WebSocketServerProtocolHandler("/websocket"),
                                //自定义消息解码
                                new GameMsgDecoder(),
                                //编码是放到处理之前，因为是out
                                new GameMsgEncoder(),
                                new GameMsgHandler()
                        );
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.bind(12345).sync();
            if (channelFuture.isSuccess()) {
                log.info("服务器启动");
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
