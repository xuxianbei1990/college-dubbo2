package college.dubbo.herostory;

import college.dubbo.herostory.msg.GameMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuxianbei
 * Date: 2020/4/28
 * Time: 15:03
 * Version:V1.0
 */
@Slf4j
public class GameMsgHandler extends SimpleChannelInboundHandler {

    /**
     * 存储通道
     */
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        CHANNEL_GROUP.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("收到客户端消息, msg = " + msg);

        if (msg instanceof GameMsgProtocol.UserEntryCmd) {
            doHandleUserEntryCmd((GameMsgProtocol.UserEntryCmd) msg);
        }

        //HttpServerCodec 解码之后的
        if (msg instanceof BinaryWebSocketFrame) {
            doHandleBinaryWebSocketFrame((BinaryWebSocketFrame) msg);
        }
    }

    private void doHandleUserEntryCmd(GameMsgProtocol.UserEntryCmd cmd) {
        int userId = cmd.getUserId();
        String heroAvatar = cmd.getHeroAvatar();
        GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        // 构建结果并发送
        GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
        // 这玩意群发？
        CHANNEL_GROUP.writeAndFlush(newResult);
    }

    private void doHandleBinaryWebSocketFrame(BinaryWebSocketFrame msg) {
        // WebSocket 二进制消息会通过 HttpServerCodec 解码成 BinaryWebSocketFrame 类对象
        BinaryWebSocketFrame frame = msg;
        ByteBuf byteBuf = frame.content();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        System.out.println("收到的字节 = ");
        for (byte b : bytes) {
            System.out.print(b);
            System.out.print(", ");
        }

        System.out.println();
    }
}
