package all

import java.io.File
import java.nio.charset.Charset
import java.util.Base64

import codec.{HeaderDefine, Message}
import com.googlecode.scalascriptengine.ScalaScriptEngine
import io.netty.channel.Channel
import org.jboss.netty.buffer.ChannelBuffers
import org.scalatest.{FlatSpec, Matchers}
import scriptable.Script

import scala.collection.mutable


class SR1 extends Script{
  var session:Channel=null
  var recvCount=0

  def defineHeader:HeaderDefine = {
    val hd = new HeaderDefine
    hd.headerSize = 2
    hd.beginDataSizeOffset = 0
    hd.typeOfDataSize = 2
    hd.beginProtocolOffset = 2
    hd.typeOfProtocolSize = 2
    hd.isPacketSizeIncludeHeader = true
    hd.byteOrderSwapInHeader = true
    return hd
  }

  def onStart = {
    // do something on start script
    session = connect("172.16.1.244",9999)
    if( session == null ){
      writeConsole("connect fail")
    }
    else{
      for( count<-0 until 5){
        println("write msg")
        val testMsg = Message()
        testMsg.protocolId = 101
        testMsg.writeString("abc")
        session.writeAndFlush(testMsg)
      }
    }
  }

  override def onRead(message:Message) = {
    val protocol = message.readShort
    println(ChannelBuffers.swapShort(protocol))
  }

  override def onDisconnect = {
    writeConsole("onDisconnect")
  }

  override def onException(cause:Throwable) = {
    writeConsole(cause.toString)
  }

  override def getPort: Int = {30200}

  override def getHost: String = "172.16.1.244"

  override def tick(): Boolean = { return true}

  override def onConnected(channel: Channel): Unit = {
    println("connect")
  }
}
class ConnectSendRecv extends FlatSpec with Matchers{

  "script" should "connect" in {
    val path = getClass.getResource("/scriptable")
    val scriptFile = new File(path.getPath)
    val sse = ScalaScriptEngine.onChangeRefresh( scriptFile )
    sse.refresh
    val t = sse.newInstance[scriptable.Script]("scriptable.Main")
    Thread.sleep(50000)
  }

  "list test" should "aa" in {
    val seq:mutable.MutableList[String] = new mutable.MutableList[String]
    seq+="a"
    seq+="b"
    seq foreach { x => println ( x )}
  }

  "sr" should "auth" in {
    val sr1 = new SR1
    //    val hd = new HeaderDefine
    //    hd.headerSize = 8
    //    hd.beginDataSizeOffset = 4
    //    hd.typeOfDataSize = 4
    //    hd.beginProtocolOffset = 0
    //    hd.typeOfProtocolSize = 4
    //    hd.isPacketSizeIncludeHeader = false
    val channel = sr1.connect( "172.16.1.244", 30200)
    if( channel != null ){

      val testMsg = Message()
      testMsg.protocolId = 102



      val userId = "dmg2@m.m"
      var dummyData = Array[Byte]()
      //S make
      //dummyData ++= Array[Byte](100,109,103,50,64,109,46,109,0)
      //E make

      //S make
      //      dummyData ++= writeString(userId,40+1)
      //      dummyData ++= writeString("1",32+1)
      //      val t = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN)
      //      t.writeInt(606051751)
      //      t.writeShort(1)
      //      t.writeShort(1)
      //      t.capacity(t.writerIndex())
      //      dummyData ++= t.array()
      //      dummyData = encrypt(dummyData,keys)
      val dummyMsg = Message()
      dummyMsg.writeShort( ChannelBuffers.swapShort(102))
      dummyMsg.writeString(userId,40+1)
      dummyMsg.writeString("1",32+1)
      dummyMsg.writeInt(606051751)
      dummyMsg.writeShort(1)
      dummyMsg.writeShort(1)
      dummyMsg.writeString("",6)
      dummyMsg.writeByte(0)
      dummyMsg.writeByte(2)

      dummyMsg.buffer.capacity(dummyMsg.buffer.writerIndex())
      dummyData ++= dummyMsg.buffer.array()
      dummyData = encrypt(dummyData,keys)
      println(dummyData.length)
      //E make
      testMsg.writeBytes(dummyData)
      channel.writeAndFlush(testMsg)
    }

    Thread.sleep(5000000)
  }

  def encrypt( source:Array[Byte], key:Array[Byte]) :Array[Byte] = {
    var keySeq = 0
    for( seq<-2 until source.length){
      val xor1 = source(seq)
      val xor2 = key(keySeq % key.length)
      val xor3 = xor1 ^ xor2
      source(seq) = xor3.asInstanceOf[Byte]
      keySeq+=1
    }
    return source
  }

  def writeString( str:String, size:Int) : Array[Byte]  ={
    val bytes = str.getBytes(Charset.forName("UTF-8"))
    val bytesLength = bytes.length
    var array = Array[Byte]()
    array ++= bytes
    array ++= Array.fill[Byte](size-bytesLength)(0)
    array
  }

  "sr" should "crypt" in {
    var dummyData = Array.fill[Byte](92*2)(0)
    println( dummyData.length)
    val temp = encrypt(dummyData,keys)
    println(temp.length)
  }
  val decoded = Base64.getDecoder.decode("G1ZsdwhlLRldHF4jFXspD1skSGZ5LA9BU1UJIxUPDENQSTRKGVEwCkt2QjA6QUsRVB93dHEJQg51TVkFYWw0RBl6UU5WURMHKUZoAHI9fk1AWR13cBIwcAFxbkgEW3t2BnhoZmY+CiF7QXNrdQsjc1RzHwV8chRzHAssehZNQgQ=")
  val keys = decoded ++ Array[Byte]( 0x00, 0x00)

}
