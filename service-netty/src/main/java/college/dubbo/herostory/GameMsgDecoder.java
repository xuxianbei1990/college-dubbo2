package college.dubbo.herostory;

import college.dubbo.herostory.msg.GameMsgProtocol;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * 消息解码器
 *
 * @author: xuxianbei
 * Date: 2020/4/28
 * Time: 20:41
 * Version:V1.0
 */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }
        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();
        //读取消息长度
        byteBuf.readShort();
        //读取消息编号
        int msgCode = byteBuf.readShort();

        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        GeneratedMessageV3 cmd = null;
        switch (msgCode) {
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                cmd = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
                break;
            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERO_CMD_VALUE:
                cmd = GameMsgProtocol.WhoElseIsHeroCmd.parseFrom(msgBody);
                break;
            case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                cmd = GameMsgProtocol.UserMoveToCmd.parseFrom(msgBody);
                break;
        }

        if (null != cmd) {
            ctx.fireChannelRead(cmd);
        }
    }
}
