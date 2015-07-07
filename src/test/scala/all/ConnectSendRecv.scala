package all

import java.io.File

import com.googlecode.scalascriptengine.ScalaScriptEngine
import org.scalatest.{Matchers, FlatSpec}

class ConnectSendRecv extends FlatSpec with Matchers{

  "script" should "connect" in {
    val path = getClass.getResource("/scriptable")
    val scriptFile = new File(path.getPath)
    val sse = ScalaScriptEngine.onChangeRefresh( scriptFile )
    sse.refresh
    val t = sse.newInstance[scriptable.Script]("scriptable.Main")
    t.start

    Thread.sleep(50000)
  }
}
