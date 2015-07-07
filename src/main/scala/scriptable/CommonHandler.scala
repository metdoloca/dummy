package scriptable

import java.nio.ByteOrder

import codec.Message
import io.netty.buffer.ByteBuf
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}

class CommonHandler() extends ChannelInboundHandlerAdapter {
  var script:Script=null

  //val queue = new scala.collection.mutable.Queue[Message]
  override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef): Unit = {
    script.onRead(msg.asInstanceOf[Message])
    //queue+=Message(msg.asInstanceOf[ByteBuf])
  }

  @throws(classOf[Exception])
  override def channelActive(ctx: ChannelHandlerContext): Unit ={
    script.onConnect
    ctx.fireChannelActive();
  }
}
