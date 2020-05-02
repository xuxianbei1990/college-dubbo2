package college.dubbo.herostory2.cmdHandler;

import college.dubbo.herostory.msg.GameMsgProtocol;
import college.dubbo.herostory2.Broadcaster;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 用户攻击指令处理器
 * 作为架构师需要把业务优化到这种地步
 *
 * @author: xuxianbei
 * Date: 2020/5/2
 * Time: 20:43
 * Version:V1.0
 */
public class UserAttkCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd> {

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd cmd) {
        if (null == ctx || null == cmd) {
            return;
        }
        // 获取攻击者 Id
        Integer attkUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == attkUserId) {
            return;
        }
        // 获取被攻击者 Id
        int targetUserId = cmd.getTargetUserId();
        GameMsgProtocol.UserAttkResult.Builder resultBuilder = GameMsgProtocol.UserAttkResult.newBuilder();
        resultBuilder.setAttkUserId(attkUserId);
        resultBuilder.setTargetUserId(targetUserId);

        GameMsgProtocol.UserAttkResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);

        // 减血消息, 可以根据自己的喜好写...
        // 例如加上装备加成, 暴击等等.
        // 这些都属于游戏的业务逻辑了!
        GameMsgProtocol.UserSubtractHpResult.Builder builder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        builder.setTargetUserId(targetUserId);
        builder.setSubtractHp(10);
        GameMsgProtocol.UserSubtractHpResult newResult2 = builder.build();
        Broadcaster.broadcast(newResult2);
    }
}
