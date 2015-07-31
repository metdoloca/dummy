package scriptable

import java.net.{InetSocketAddress, SocketAddress}

import akka.actor.ActorRef
import codec.{HeaderDefine, Message}
import io.netty.channel._
import io.netty.util.AttributeKey

import scala.collection.mutable

trait Script {
  var sessionKey:String = ""
  final var actor:ActorRef=null
  val sessions:mutable.MutableList[Channel]=new mutable.MutableList[Channel]
  def getPort:Int
  def getHost:String
  final def connect(host:String,port:Int): Channel = {
    val channel = SessionCreator.connect(host, port, defineHeader, this)
    sessions+=channel

    //onConnected(channel)
    channel
//    val aa:AttributeKey[Int] = new AttributeKey[Int]("f")
//    channel.attr[Int](aa)
//    channel.remoteAddress().asInstanceOf[InetSocketAddress].getPort()
  }
//  final def connect(): Channel = {
//    SessionCreator.connect(getHost, getPort, defineHeader, this)
//  }

  final def writeConsole(log:String):Unit={
    if( actor != null ){
      actor ! LogLine(log, 1, sessionKey)
    }
  }
  def defineHeader:HeaderDefine

  def onStart
  def onConnected(channel:Channel)
  def tick():Boolean
  def onRead(channel:Channel, message:Message)
  def onDisconnect(channel:Channel)
  def onException(cause: Throwable)

}
