package server

import flint.{FlintDecoder, FlintEncoder}
import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.{ChannelOption, EventLoopGroup}

/**
 * Created by shkim on 15. 6. 18.
 */

object Main extends App{


//  val host:String = "127.0.0.1"
//  val port:Int = "8888".toInt
//
//  val workerGroup:EventLoopGroup = new NioEventLoopGroup();
//
//  try {
//    val b = new Bootstrap(); // (1)
//
//    b.group(workerGroup)
//      .channel(classOf[NioSocketChannel])
//      .option(ChannelOption.SO_KEEPALIVE, true)
//      .handler({ ch: SocketChannel =>
//      ch.pipeline().addLast(new FlintDecoder, new FlintEncoder)
//    })
//  }
}
