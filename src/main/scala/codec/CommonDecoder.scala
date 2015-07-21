package codec

import java.nio.ByteOrder
import java.util

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

/**
 * Created by shkim on 15. 5. 11.
 */
class CommonDecoder(headerDefine:HeaderDefine) extends ByteToMessageDecoder{
  override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: util.List[AnyRef]): Unit = {
    val littleEndian:ByteBuf = in.order(ByteOrder.LITTLE_ENDIAN)
    if (littleEndian.readableBytes() < headerDefine.headerSize) {
      return
    }
    val bodySize = headerDefine.typeOfDataSize match {
      case HeaderDefine.SIZE_INT =>   littleEndian.getInt(headerDefine.beginDataSizeOffset + littleEndian.readerIndex())
      case HeaderDefine.SIZE_SHORT => littleEndian.getShort(headerDefine.beginDataSizeOffset + littleEndian.readerIndex())
      case HeaderDefine.SIZE_BYTE =>  littleEndian.getByte(headerDefine.beginDataSizeOffset + littleEndian.readerIndex())
    }
    if( bodySize + headerDefine.headerSize <= littleEndian.readableBytes() ) {
      val readSize = headerDefine.isPacketSizeIncludeHeader match {
        case false => bodySize + headerDefine.headerSize
        case true => bodySize
      }
      val msg = Message(littleEndian.readBytes( readSize ))
      headerDefine.typeOfProtocolSize match {
        case HeaderDefine.SIZE_INT => msg.protocolId=msg.buffer.getInt(headerDefine.beginProtocolOffset)
        case HeaderDefine.SIZE_SHORT => msg.protocolId=msg.buffer.getShort(headerDefine.beginProtocolOffset)
        case HeaderDefine.SIZE_BYTE => msg.protocolId=msg.buffer.getByte(headerDefine.beginProtocolOffset)
      }
      msg.buffer.readerIndex(headerDefine.headerSize)
      out.add( msg )
    }
  }
}