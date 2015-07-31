package codec

import java.nio.ByteOrder
import java.nio.charset.Charset
import java.util.concurrent.{LinkedBlockingQueue, BlockingQueue}

import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.channel.embedded.EmbeddedChannel
import org.scalatest.{Matchers, FlatSpec}
//http://www.scalatest.org/user_guide/using_matchers

class TestHandler extends ChannelInboundHandlerAdapter {
  val q:BlockingQueue[String] = new LinkedBlockingQueue[String]
  override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef): Unit = {
    val temp: ByteBuf = msg.asInstanceOf[ByteBuf].order(ByteOrder.LITTLE_ENDIAN)
//    val protocol: Long = temp.readUnsignedInt
//    val bodySize: Long = temp.readUnsignedInt
    temp.skipBytes(8)
    val nullLocation = temp.bytesBefore( temp.readerIndex(), temp.readableBytes(), 0x00)
    val tempString = new Array[Byte](nullLocation)
    temp.getBytes(temp.readerIndex(), tempString)
    temp.skipBytes(1)
    val data: String = new String(tempString)
    q.add(data)
  }
}

class CommonCodecTest extends FlatSpec with Matchers{

//  "byteBuf retain" should "not influence exist buf" in{
//    val buf:ByteBuf = Unpooled.buffer(100).order(ByteOrder.LITTLE_ENDIAN)
//    buf.writeInt(1)
//    buf.writeInt(2)
//    val wrappedBuffer = Unpooled.wrappedBuffer(buf)
//    wrappedBuffer.writerIndex(4)
//    buf.writerIndex() should not equal (wrappedBuffer.writerIndex())
//  }
//
//  "codec" should "process complete packet " in {
//    val handler:TestHandler = new TestHandler
//    val ec = new EmbeddedChannel(new CommonDecoder(new HeaderDefine), new CommonEncoder(new HeaderDefine), handler)
//    //val headerSize = 8
//    val buf:ByteBuf = Unpooled.buffer(100).order(ByteOrder.LITTLE_ENDIAN)
//    val stream:String = "test_한글test8"
//    //stream.length should equal (stream.getBytes(Charset.forName("UTF-8")).length)
//    buf.writeInt(0)
//    buf.writeInt(stream.getBytes(Charset.forName("UTF-8")).length+1)
//    buf.writeBytes(stream.getBytes(Charset.forName("UTF-8")))
//    buf.writeZero(1)
//    ec.writeInbound( Unpooled.wrappedBuffer(buf).retain())
//    ec.writeInbound( Unpooled.wrappedBuffer(buf).retain())
//    ec.writeInbound( Unpooled.wrappedBuffer(buf).retain())
//    ec.writeInbound( Unpooled.wrappedBuffer(buf).retain())
//    ec.writeInbound( Unpooled.wrappedBuffer(buf).retain())
//    val msg1 = handler.q.take()
//    val msg2 = handler.q.take()
//    val msg3 = handler.q.take()
//    val msg4 = handler.q.take()
//    val msg5 = handler.q.take()
//    msg1 should equal (stream)
//    msg2 should equal (stream)
//    msg3 should equal (stream)
//    msg4 should equal (stream)
//    msg5 should equal (stream)
//    handler.q.size() should be (0)
//    buf.refCnt() should be (1)
//    // continuous buf
//    val continuous:ByteBuf = Unpooled.buffer(200).order(ByteOrder.LITTLE_ENDIAN)
//    for( count<-0 until 5){
//      continuous.writeInt(0)
//      continuous.writeInt(stream.getBytes(Charset.forName("UTF-8")).length+1)
//      continuous.writeBytes(stream.getBytes(Charset.forName("UTF-8")))
//      continuous.writeZero(1)
//    }
//    ec.writeInbound(continuous)
//    for( count<-0 until 5){
//      handler.q.take() should equal (stream)
//    }
//    handler.q.size() should be (0)
//  }
//
//  "CommonCodec" should "reassemble fragmentation's packet " in {
//    val handler:TestHandler = new TestHandler
//    val ec = new EmbeddedChannel(new CommonDecoder(new HeaderDefine), new CommonEncoder(new HeaderDefine), handler)
//    //frag1
//    val stream:String = "test_test8"
//    val buf:ByteBuf = Unpooled.buffer(100).order(ByteOrder.LITTLE_ENDIAN)
//    buf.writeInt(0)
//    buf.writeInt(stream.length+1)
//    ec.writeInbound(buf)
//    //frag2
//    val buf2:ByteBuf = Unpooled.buffer(100).order(ByteOrder.LITTLE_ENDIAN)
//    buf2.writeBytes(stream.getBytes(Charset.forName("UTF-8")))
//    buf2.writeZero(1)
//    val stream2:String = "test_test2"
//    buf2.writeInt(0)
//    buf2.writeInt(stream2.length+1)
//    buf2.writeBytes(stream2.getBytes(Charset.forName("UTF-8")))
//    buf2.writeZero(1)
//    ec.writeInbound(buf2)
//    //reassemble1
//    val msg1 = handler.q.take()
//    msg1 should equal (stream)
//    val msg2 = handler.q.take()
//    msg2 should equal (stream2)
//    //frag3
//    val buf3:ByteBuf = Unpooled.buffer(100).order(ByteOrder.LITTLE_ENDIAN)
//    val stream3:String = "test_test777"
//    buf3.writeInt(0)
//    buf3.writeInt(stream3.length+1)
//    buf3.writeBytes(stream3.getBytes(Charset.forName("UTF-8")))
//    ec.writeInbound(buf3)
//    //frag4
//    val buf4:ByteBuf = Unpooled.buffer(100).order(ByteOrder.LITTLE_ENDIAN)
//    buf4.writeZero(1)
//    ec.writeInbound(buf4)
//    //reassemble2
//    val msg3 = handler.q.take()
//    msg3 should equal (stream3)
//  }
//
//  "decode" should "read empty string" in {
//    val handler:TestHandler = new TestHandler
//    val ec = new EmbeddedChannel(new CommonDecoder(new HeaderDefine), new CommonEncoder(new HeaderDefine), handler)
//    val buf:ByteBuf = Unpooled.buffer(100).order(ByteOrder.LITTLE_ENDIAN)
//    val stream:String = ""
//    buf.writeInt(0)
//    buf.writeInt(stream.getBytes(Charset.forName("UTF-8")).length+1)
//    buf.writeBytes(stream.getBytes(Charset.forName("UTF-8")))
//    buf.writeZero(1)
//    ec.writeInbound( buf)
//    val msg1 = handler.q.take()
//    msg1 should equal (stream)
//
//  }
//
//  class CommonHandler extends ChannelInboundHandlerAdapter {
//    val queue = new scala.collection.mutable.Queue[Message]
//    override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef): Unit = {
//      val commonMsg = Message(msg.asInstanceOf[ByteBuf].order(ByteOrder.LITTLE_ENDIAN))
//      queue+=commonMsg
//    }
//  }
//
//  "Message" should "create from handler" in{
//    val handler:CommonHandler = new CommonHandler
//    val ec = new EmbeddedChannel(new CommonDecoder(new HeaderDefine), new CommonEncoder(new HeaderDefine), handler)
//    val buf:ByteBuf = Unpooled.buffer(100).order(ByteOrder.LITTLE_ENDIAN)
//    val stream:String = "test_한글test8"
//    //stream.length should equal (stream.getBytes(Charset.forName("UTF-8")).length)
//    buf.writeInt(0)
//    buf.writeInt(stream.getBytes(Charset.forName("UTF-8")).length+1)
//    buf.writeBytes(stream.getBytes(Charset.forName("UTF-8")))
//    buf.writeZero(1)
//    ec.writeInbound( Unpooled.wrappedBuffer(buf).retain())
//    val msg = handler.queue.dequeue
//    msg.readString should equal (stream)
//  }


}
