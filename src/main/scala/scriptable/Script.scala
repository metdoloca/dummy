package scriptable

import akka.actor.ActorRef
import codec.{HeaderDefine, Message}
import io.netty.channel._

trait Script {
  final var actor:ActorRef=null
  def getPort:Int
  def getHost:String
  final def connect(host:String,port:Int): Channel = {
    SessionCreator.connect(host, port, defineHeader, this)
  }
  final def connect(): Channel = {
    SessionCreator.connect(getHost, getPort, defineHeader, this)
  }

  final def writeConsole(log:String):Unit={
    if( actor != null ){
      actor ! LogLine( log, 1)
    }
  }
  def defineHeader:HeaderDefine

  def onStart
  def onConnected(channel:Channel)
  def tick():Boolean
  def onRead(message:Message)
  def onDisconnect
  def onException(cause: Throwable)

}
