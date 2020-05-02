package college.dubbo.herostory2.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: xuxianbei
 * Date: 2020/5/2
 * Time: 15:41
 * Version:V1.0
 */
public interface ICmdHandler<TCmd extends GeneratedMessageV3> {
    void handle(ChannelHandlerContext ctx, TCmd cmd);
}
