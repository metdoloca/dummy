package scriptable

import java.util.concurrent.TimeUnit

import codec.{CommonDecoder, CommonEncoder, HeaderDefine}
import io.netty.bootstrap.Bootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel

object SessionCreator {
  lazy val b = init()
  var started = false
  def init(): Bootstrap ={
    println("start")
    new Bootstrap()
  }

  def connect(host:String, port:Int, headerDefine:HeaderDefine, script:Script ):Channel = {

    if( !started ){
      val workerGroup:EventLoopGroup = new NioEventLoopGroup();
      b.group(workerGroup)
        .channel(classOf[NioSocketChannel])
        .option[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)
            .handler({ ch: SocketChannel =>
//            val handler = new CommonHandler
//            handler.script = script
//            ch.pipeline().addLast(new CommonDecoder(headerDefine),new CommonEncoder(headerDefine),handler)
            ch.pipeline()/*.addLast(new CommonDecoder(headerDefine),new CommonEncoder(headerDefine) )*/
          })
      started=true
    }
    println("connect")
    val f=b.connect(host, port)
    if(f.awaitUninterruptibly(3,TimeUnit.SECONDS)){
      val handler = new CommonHandler
      handler.script = script

      f.channel().pipeline().addLast(new CommonDecoder(headerDefine))
      f.channel().pipeline().addLast(new CommonEncoder(headerDefine))
      f.channel().pipeline().addLast(handler)
      return f.channel()
    }
    println( s"can't connect ${host}:${port}")
    return null
  }
}