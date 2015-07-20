package session

import java.nio.ByteOrder

import codec.{HeaderDefine, CommonEncoder, CommonDecoder, Message}
import flint.{FlintDecoder, FlintEncoder}
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import org.scalatest.{FlatSpec, Matchers}
import scriptable.SessionCreator


/**
 * Created by shkim on 15. 6. 28.
 */
class ConnectToServer extends FlatSpec with Matchers{
  class CommonHandler extends ChannelInboundHandlerAdapter {
    val queue = new scala.collection.mutable.Queue[Message]
    override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef): Unit = {
      val commonMsg = Message(msg.asInstanceOf[ByteBuf].order(ByteOrder.LITTLE_ENDIAN))
      queue+=commonMsg
    }
  }
  "session" should "connect to 127.0.0.1" in{
    //val host:String = "127.0.0.1"
    val host:String = "172.16.1.244"
    val port:Int = "9999".toInt
    val workerGroup:EventLoopGroup = new NioEventLoopGroup();
    try {
      val define = new HeaderDefine
      define.beginDataSizeOffset = 4
      define.headerSize = 8
      define.typeOfDataSize = 4
      define.beginProtocolOffset = 0
      define.typeOfProtocolSize = 4

      val handler = new CommonHandler
      val decoder = new CommonDecoder(define)

      val b = new Bootstrap(); // (1)
      b.group(workerGroup)
        .channel(classOf[NioSocketChannel])
        .option[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)
//        .handler({ ch: SocketChannel =>

  //      })
      // Start the client.
      val f=b.connect(host, port).sync(); // (5)
      f.channel().pipeline().addLast(decoder, new CommonEncoder(define), handler)
      //write test msg
//      for( count<-0 until 5){
//        val buf:ByteBuf = Unpooled.buffer(4096).order(ByteOrder.LITTLE_ENDIAN)
//        buf.writeInt(101)
//        buf.writeInt(4)
//        buf.writeInt(4)
//        f.channel().writeAndFlush(buf)
//      }
      for( count<-0 until 5){
        val testMsg = Message()
        testMsg.protocolId=101
        testMsg.writeInt(4)
        f.channel().writeAndFlush(testMsg.buffer)
      }
      //wait close
      f.channel().closeFuture().sync();
    }finally {
      workerGroup.shutdownGracefully();
    }
  }

  def connect(): Option[Channel] = {
    val a:Int = 4
    val inputStream = "aa"
    val define = new HeaderDefine
    define.beginDataSizeOffset = 4
    define.headerSize = 8
    define.typeOfDataSize = 4
    define.beginProtocolOffset = 0
    define.typeOfProtocolSize = 4
    val channel:Channel = SessionCreator.connect("172.16.1.54",9999,define,null)
    return Option(channel)
  }
  "sessionCreator" should "return some" in{
//    val net = connect() match{
//      case None => println("can't connect")
//      case Some(x) => x
//    }

    val net = connect()
    if( !net.isEmpty ){
      println(net.getClass)
    }
    else{
      println(net)
    }

    //tmep.
    //temp.
  }
}
