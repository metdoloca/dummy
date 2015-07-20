package scriptable

import akka.actor.ActorRef
import codec.{HeaderDefine, Message}
import io.netty.channel._

trait Script {
  final var actor:ActorRef=null
  final def connect(host:String,port:Int): Channel = {
    SessionCreator.connect(host, port, defineHeader, this)
  }
  final def writeConsole(log:String):Unit={
    actor ! LogLine( log, 1)
  }
  def onStart
  def defineHeader:HeaderDefine
  def onRead(message:Message)
  def onDisconnect
  def onException(cause: Throwable)
}
