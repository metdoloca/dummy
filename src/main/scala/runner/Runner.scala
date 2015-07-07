package runner

import java.io.File
import com.googlecode.scalascriptengine.ScalaScriptEngine

class Runner {

  def execute(path:String): Unit ={
    val scriptFile = new File(path)
    val sse = ScalaScriptEngine.onChangeRefresh( scriptFile )
    sse.refresh
    val t = sse.newInstance[scriptable.Script]("scriptable.Main")
    t.start
  }
  def print() = {
    println("test")
  }
}
