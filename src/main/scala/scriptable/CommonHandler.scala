package scriptable

import codec.Message
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import org.jboss.netty.channel.SimpleChannelHandler

class CommonHandler() extends ChannelInboundHandlerAdapter {
  var script:Script=null
  override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef):Unit={

    script.onRead(ctx.channel(), msg.asInstanceOf[Message])
  }
  @throws(classOf[Exception])
  override def channelActive(ctx: ChannelHandlerContext):Unit={
    //println( "channelActive" + ctx.channel().toString )
    //ctx.fireChannelActive()
    //script.onConnected(ctx.channel())
  }

  @throws(classOf[Exception])
  override def channelInactive(ctx: ChannelHandlerContext):Unit={
    //println( "channelInactive" + ctx.channel().toString )
    script.onDisconnect(ctx.channel())
  }

  @throws(classOf[Exception])
  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit =  {
    script.onException(cause);
  }
}
