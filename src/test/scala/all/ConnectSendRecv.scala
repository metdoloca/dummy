package all

import java.io.File

import com.googlecode.scalascriptengine.ScalaScriptEngine
import org.scalatest.{Matchers, FlatSpec}

import scala.collection.mutable

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
}
