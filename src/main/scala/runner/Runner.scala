package runner
import akka.actor.{ActorRef, Actor}
import java.io.File
import java.util.concurrent.{Executors, ExecutorService}
import akka.actor.ActorRef
import com.googlecode.scalascriptengine.ScalaScriptEngine
import scriptable.{LogLine, Script}

object Pool{
  implicit def runnable(f:()=> Unit):Runnable = new Runnable(){def run() = f() }
  val pool = Executors.newCachedThreadPool()
  var running = true
  def run(script:Script) = {
    pool.execute(
      ()=>{
        script.onStart
        val channel = script.connect(script.getHost,script.getPort)
        script.onConnected(channel)
        var bStop = true
        while(running && bStop){
          Thread.sleep(1000)
          if( script.tick() == false ){
            bStop =false
            if( channel != null ){
              channel.disconnect()
            }
          }
        }
      }
    )
  }
}

class Runner {
  def execute(path:String,actorRef:ActorRef) ={
    val scriptFile = new File(path)
    try{
      val sse = ScalaScriptEngine.onChangeRefresh( scriptFile )
      sse.refresh
      val scriptInstance = sse.newInstance[scriptable.Script]("scriptable.Main")
      scriptInstance.actor = actorRef
      Pool.run(scriptInstance)
    }catch {
      case e:Exception =>{
        actorRef ! LogLine(e.getMessage,0)
      }
    }finally {

    }
  }
}
