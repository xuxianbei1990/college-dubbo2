package college.dubbo.herostory2;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 广播
 *
 * @author: xuxianbei
 * Date: 2020/4/30
 * Time: 20:22
 * Version:V1.0
 */
public final class Broadcaster {
    /**
     * 存储通道
     */
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster() {

    }

    /**
     * 添加信道
     *
     * @param channel
     */
    static public void addChannel(Channel channel) {
        CHANNEL_GROUP.add(channel);
    }

    /**
     * 移除信道
     *
     * @param channel
     */
    static public void removeChannel(Channel channel) {
        CHANNEL_GROUP.remove(channel);
    }

    /**
     * 广播消息
     *
     * @param msg
     */
    static public void broadcast(Object msg) {
        if (null == msg) {
            return;
        }

        CHANNEL_GROUP.writeAndFlush(msg);
    }
}
