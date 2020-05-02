package college.dubbo.herostory2;

import college.dubbo.herostory.msg.GameMsgProtocol;
import college.dubbo.herostory2.cmdHandler.CmdHandlerFactory;
import college.dubbo.herostory2.cmdHandler.ICmdHandler;
import college.dubbo.herostory2.model.User;
import college.dubbo.herostory2.model.UserManager;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author: xuxianbei
 * Date: 2020/4/28
 * Time: 15:03
 * Version:V1.0
 */
@Slf4j
public class GameMsgHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Broadcaster.addChannel(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到客户端消息, msg = " + msg);

        ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msg.getClass());
        if (null != cmdHandler) {
            cmdHandler.handle(ctx, cast(msg));
        }

    }

    static private <TCmd extends GeneratedMessageV3> TCmd cast(Object msg) {
        if (null == msg) {
            return null;
        } else {
            return (TCmd) msg;
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        Broadcaster.removeChannel(ctx.channel());

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        UserManager.removeUserById(userId);
        GameMsgProtocol.UserQuitResult.Builder builder = GameMsgProtocol.UserQuitResult.newBuilder();
        builder.setQuitUserId(userId);

        GameMsgProtocol.UserQuitResult result = builder.build();
        Broadcaster.broadcast(result);
    }

    private void doHandleUserMove(ChannelHandlerContext ctx, Object msg) {
        //为什么弄一个这么复杂的对象管理器
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (Objects.isNull(userId)) {
            return;
        }
        GameMsgProtocol.UserMoveToCmd cmd = (GameMsgProtocol.UserMoveToCmd) msg;
        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        resultBuilder.setMoveUserId(userId);
        resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
        resultBuilder.setMoveToPosY(cmd.getMoveToPosY());
        GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

    private void doHandleWhoElseIsHere(ChannelHandlerContext ctx) {
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        UserManager.listUser().forEach(entry -> {
            if (null == entry) {
                return;
            }
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(entry.getUserId());
            userInfoBuilder.setHeroAvatar(entry.getHeroAvatar());
            resultBuilder.addUserInfo(userInfoBuilder);
        });


        GameMsgProtocol.WhoElseIsHereResult newResult = resultBuilder.build();
        ctx.writeAndFlush(newResult);
    }

    private void doHandleUserEntryCmd(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd cmd) {
        int userId = cmd.getUserId();
        String heroAvatar = cmd.getHeroAvatar();
        GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        User newUser = new User();
        newUser.setUserId(userId);
        newUser.setHeroAvatar(heroAvatar);
        UserManager.addUser(newUser);
        //解决用户被客户端追踪问题，安全问题。防止用户修改自身数据
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
        // 构建结果并发送
        GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
        // 这玩意群发？是群发。
        Broadcaster.broadcast(newResult);
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
