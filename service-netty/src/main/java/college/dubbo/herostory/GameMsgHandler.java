package college.dubbo.herostory;

import college.dubbo.herostory.msg.GameMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    /**
     * 用户字典  为什么这里这么用，也没啥问题？因为业务上，一个用户不会同时存在登录和登出。
     * 1.8 hashmap 并发问题：同样的key 设置时候可能会丢失； size问题。
     */
    private static final Map<Integer, User> userMap = new HashMap<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        CHANNEL_GROUP.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到客户端消息, msg = " + msg);

        if (msg instanceof GameMsgProtocol.UserEntryCmd) {
            doHandleUserEntryCmd(ctx, (GameMsgProtocol.UserEntryCmd) msg);
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {
            doHandleWhoElseIsHere(ctx);
        } else if (msg instanceof GameMsgProtocol.UserMoveToCmd) {
            doHandleUserMove(ctx, msg);
        } else if (msg instanceof BinaryWebSocketFrame) {
            //HttpServerCodec 解码之后的
            doHandleBinaryWebSocketFrame((BinaryWebSocketFrame) msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        CHANNEL_GROUP.remove(ctx.channel());

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        userMap.remove(userId);
        GameMsgProtocol.UserQuitResult.Builder builder = GameMsgProtocol.UserQuitResult.newBuilder();
        builder.setQuitUserId(userId);

        GameMsgProtocol.UserQuitResult result = builder.build();
        CHANNEL_GROUP.writeAndFlush(result);
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
        CHANNEL_GROUP.writeAndFlush(newResult);
    }

    private void doHandleWhoElseIsHere(ChannelHandlerContext ctx) {
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        userMap.entrySet().forEach(entry -> {
            if (null == entry.getValue()) {
                return;
            }
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(entry.getValue().getUserId());
            userInfoBuilder.setHeroAvatar(entry.getValue().getHeroAvatar());
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
        userMap.putIfAbsent(newUser.getUserId(), newUser);
        //解决用户被客户端追踪问题，安全问题。防止用户修改自身数据
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
        // 构建结果并发送
        GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
        // 这玩意群发？是群发。
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
