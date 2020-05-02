package college.dubbo.herostory2;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息解码器
 *
 * @author: xuxianbei
 * Date: 2020/4/28
 * Time: 20:41
 * Version:V1.0
 */
@Slf4j
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

        // 获取消息构建者
        Message.Builder msgBuilder = GameMsgRecognizer.getBuilderByMsgCode(msgCode);
        if (null == msgBuilder) {
            log.error("无法识别的消息, msgCode = {}", msgCode);
            return;
        }

        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        msgBuilder.clear();
        msgBuilder.mergeFrom(msgBody);

        // 构建消息
        Message newMsg = msgBuilder.build();

        if (null != newMsg) {
            ctx.fireChannelRead(newMsg);
        }
    }
}
