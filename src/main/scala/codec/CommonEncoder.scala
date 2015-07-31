package codec

/**
 * Created by shkim on 15. 5. 12.
 */

import java.nio.ByteOrder

import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.jboss.netty.buffer.ChannelBuffers

class CommonEncoder(define: HeaderDefine) extends MessageToByteEncoder[Message]{
  override def encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf): Unit = {
    val buffer = msg.buffer
    var bodySize = buffer.writerIndex()
    val buf = Unpooled.buffer(bodySize+define.headerSize).order(ByteOrder.LITTLE_ENDIAN)

    // write protocol
    buf.writerIndex(define.beginProtocolOffset)
    writeNumeric(define.typeOfProtocolSize, msg.protocolId, buf)
    // write size
    buf.writerIndex(define.beginDataSizeOffset)
    if(define.isPacketSizeIncludeHeader){
      bodySize = buffer.writerIndex() /*+ define.headerSize*/
    }
    writeNumeric(define.typeOfDataSize,bodySize,buf)
    // flush
    buf.writerIndex(define.headerSize)
    buf.writeBytes(buffer)
    out.writeBytes(buf)
  }

  def writeNumeric(size:Int,number:Int,buf:ByteBuf) = {
    size match {
      case HeaderDefine.SIZE_INT =>
        if (define.byteOrderSwapInHeader) {
          buf.writeInt(ChannelBuffers.swapInt(number))
        }
        else {
          buf.writeInt(number)
        }
      case HeaderDefine.SIZE_SHORT =>
        if (define.byteOrderSwapInHeader) {
          buf.writeShort(ChannelBuffers.swapShort(number.toShort))
        }
        else{
          buf.writeShort(number.toShort)
        }
      case HeaderDefine.SIZE_BYTE =>
        buf.writeByte(number.toShort)
    }
  }
}
