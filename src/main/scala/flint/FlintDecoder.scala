package flint

import java.nio.ByteOrder
import java.util
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

/**
 * Created by shkim on 15. 5. 11.
 */
class FlintDecoder extends ByteToMessageDecoder{

  val headerSize:Int = 8

  override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: util.List[AnyRef]): Unit = {



    //println( s"decode readableByte = ${in.readableBytes()}" )

    val littleEndian:ByteBuf = in.order(ByteOrder.LITTLE_ENDIAN)

    if (littleEndian.readableBytes() < headerSize) {
      return
    }
    //println( in.readerIndex() +"/"+ in.writerIndex() )

    val protocol:Long = littleEndian.getUnsignedInt(in.readerIndex())
    val bodySize:Long = littleEndian.getUnsignedInt(in.readerIndex()+4)
    if( bodySize + headerSize <= littleEndian.readableBytes() ) {

      //in.readBytes( bodySize.asInstanceOf[Int] + headerSize )
      out.add( littleEndian.readBytes( bodySize.asInstanceOf[Int] + headerSize ) )
    }
  }
}



//package io.netty.example.time;
//
//public class TimeDecoder extends ByteToMessageDecoder { // (1)
//  @Override
//  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
//    if (in.readableBytes() < 4) {
//      return; // (3)
//    }
//
//    out.add(in.readBytes(4)); // (4)
//  }
//}