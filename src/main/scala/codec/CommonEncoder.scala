package codec

/**
 * Created by shkim on 15. 5. 12.
 */

import java.nio.ByteOrder

import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class CommonEncoder(define: HeaderDefine) extends MessageToByteEncoder[Message]{
  override def encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf): Unit = {
    val buffer = msg.buffer
    var bodySize = buffer.writerIndex()
    val buf = Unpooled.buffer(bodySize+define.headerSize).order(ByteOrder.LITTLE_ENDIAN)
    //write protocol
    buf.writerIndex(define.beginProtocolOffset)
    define.typeOfProtocolSize match {
      case HeaderDefine.SIZE_INT => buf.writeInt(msg.protocolId)
      case HeaderDefine.SIZE_SHORT => buf.writeShort(msg.protocolId)
      case HeaderDefine.SIZE_BYTE => buf.writeByte(msg.protocolId)
    }
    //write size
    buf.writerIndex(define.beginDataSizeOffset)
    if( define.isPacketSizeIncludeHeader == true ){
      bodySize = buffer.writerIndex() - define.headerSize
    }
    define.typeOfDataSize match {
      case HeaderDefine.SIZE_INT => buf.writeInt(bodySize)
      case HeaderDefine.SIZE_SHORT => buf.writeShort(bodySize)
      case HeaderDefine.SIZE_BYTE => buf.writeByte(bodySize)
    }
    //flush
    buf.writerIndex(define.headerSize)
    buf.writeBytes(buffer)
    out.writeBytes(buf)
  }
}
