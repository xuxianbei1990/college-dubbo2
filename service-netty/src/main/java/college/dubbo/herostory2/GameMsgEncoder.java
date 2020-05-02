package college.dubbo.herostory2;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 解码，编码，为了把业务数据转换成二进制 减少网络带宽占用
 *
 * @author: xuxianbei
 * Date: 2020/4/28
 * Time: 21:03
 * Version:V1.0
 */
@Slf4j
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (null == msg || !(msg instanceof GeneratedMessageV3)) {
            super.write(ctx, msg, promise);
            return;
        }

        // 获取消息编码
        int msgCode = GameMsgRecognizer.getMsgCodeByMsgClazz(msg.getClass());
        if (msgCode <= -1) {
            log.error("无法识别的消息, msgClazz = {}", msg.getClass().getName());
            return;
        }

        byte[] msgBody = ((GeneratedMessageV3) msg).toByteArray();
        ByteBuf byteBuf = ctx.alloc().buffer();
        //占位 2个字节
        byteBuf.writeShort((short) 0);
        byteBuf.writeShort((short) msgCode);
        byteBuf.writeBytes(msgBody);

        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
        super.write(ctx, frame, promise);
    }
}
