package college.dubbo.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * User: xuxianbei
 * Date: 2019/11/21
 * Time: 16:48
 * Version:V1.0
 *
 * SimpleChannelInboundHandler 和 ChannelInboundHandlerAdapter 区别就是 SimpleChannelInboundHandler自
 * 动释放内存channelRead0；而ChannelInboundHandlerAdapter不会。
 * 那么为什么SimpleChannelInboundHandler需要自动释放，而ChannelInboundHandlerAdapter不用呢？
 * 因为：对于客户端而言读完 读完就读完了，后续没了，由SimpleChannelInboundHandler释放。
 * 而ChannelInboundHandlerAdapter  读和写是异步，业务读时候然后写。简单再执行channelRead 过程已经收到了
 * channelReadComplete，所以不能释放
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello netty!", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client received:" + byteBuf.toString(CharsetUtil.UTF_8));
    }
}
