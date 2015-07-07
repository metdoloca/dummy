package codec

import java.nio.ByteOrder
import java.nio.charset.Charset

import io.netty.buffer.{Unpooled, ByteBuf}

class Message(val buffer:ByteBuf) {
  var protocolId = 0
  def readInt:Int = {
    buffer.readInt()
  }

  def readUINT:Long = {
    buffer.readUnsignedInt()
  }

  def readString:String = {
    val nullLocation = buffer.bytesBefore( buffer.readerIndex(), buffer.readableBytes(), 0x00)
    if(nullLocation != -1){
      val byteStream = new Array[Byte](nullLocation)
      buffer.getBytes(buffer.readerIndex(), byteStream)
      buffer.skipBytes(1)
      return new String(byteStream,Charset.forName("UTF-8"))
    }
    ""
  }

  def writeLong(long:Long) = {
    buffer.writeLong(long)
  }

  def writeInt(int:Int) = {
    buffer.writeInt(int)
  }

  def writeShort(short:Int) = {
    buffer.writeShort(short)
  }

  def writeByte(byte:Int) = {
    buffer.writeByte(byte)
  }

  def writeString(string:String) = {
    buffer.writeBytes(string.getBytes(Charset.forName("UTF-8")))
    buffer.writeZero(1)
  }
}

object Message{
  val bufSize=4096
  def apply():Message = {
    new Message(Unpooled.buffer(bufSize).order(ByteOrder.LITTLE_ENDIAN))
  }
  def apply(byteBuf: ByteBuf) = {
    new Message(byteBuf)
  }
}