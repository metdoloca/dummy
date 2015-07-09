package runner

import java.io.File
import akka.actor.ActorRef
import com.googlecode.scalascriptengine.ScalaScriptEngine
import scriptable.Script

class Runner {

  def execute(path:String,actorRef:ActorRef): Script ={
    val scriptFile = new File(path)
    val sse = ScalaScriptEngine.onChangeRefresh( scriptFile )
    sse.refresh
    val t = sse.newInstance[scriptable.Script]("scriptable.Main")
    t.actor = actorRef
    t.onStart
    t
  }
  def print() = {
    println("test")
  }
}
