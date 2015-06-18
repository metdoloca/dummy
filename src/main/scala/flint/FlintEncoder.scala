package flint

/**
 * Created by shkim on 15. 5. 12.
 */
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class FlintEncoder extends MessageToByteEncoder[ByteBuf]{
  override def encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf): Unit = {
    msg.resetReaderIndex
    out.writeBytes( msg )
  }
}
