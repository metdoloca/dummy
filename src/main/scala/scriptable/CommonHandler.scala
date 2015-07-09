package scriptable

import codec.Message
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}

class CommonHandler() extends ChannelInboundHandlerAdapter {
  var script:Script=null
  override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef):Unit={
    script.onRead(msg.asInstanceOf[Message])
  }
  @throws(classOf[Exception])
  override def channelActive(ctx: ChannelHandlerContext):Unit={
    println( "channelActive" + ctx.channel().toString )
    ctx.fireChannelActive()
  }
}
