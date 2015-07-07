package scriptable
import io.netty.channel._
import codec.{HeaderDefine, Message}
trait Script {
  final def connect(host:String,port:Int): Channel = {
    SessionCreator.connect(host, port, defineHeader, this)
  }
  final def send:Unit ={

  }

  def start
  def defineHeader:HeaderDefine
  def onConnect
  def onRead(message:Message)

}
