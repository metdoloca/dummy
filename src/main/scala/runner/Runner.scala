package runner
import java.io.File
import java.util.concurrent.{Executors, Future}

import akka.actor.ActorRef
import com.googlecode.scalascriptengine.ScalaScriptEngine
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import scriptable.{LogLine, Script}

import scala.collection.mutable.{HashMap, MultiMap, Set}

object Pool{
  implicit def runnable(f:()=> Unit):Runnable = new Runnable(){def run() = f() }
  val logger = Logger(LoggerFactory.getLogger(this.getClass))
  var pool = Executors.newCachedThreadPool()
  var running = true
  val tasks = new HashMap[String, Set[Future[_]]] with MultiMap[String, Future[_]]
  def run(script:Script, hostIp:String) = {

    running = true
    val task = pool.submit(
      ()=>{
        script.onStart
        var bStop = true
        try{
          while(running && bStop){
            Thread.sleep(1000)
            if(!script.tick()){
              bStop =false
            }
          }
        } catch{
          case e:Exception=>logger.error(e.getMessage)
        } finally{
          logger.debug("forced killed")
          script.sessions foreach { s => s.disconnect() }
          Thread.sleep(1000)
          tasks(hostIp) foreach { task=>tasks.removeBinding(hostIp, task)}
          tasks.remove(hostIp)
        }
      }
    )
    tasks.addBinding(hostIp,task)
  }

  def kill(hostIp:String,sessionKey:String) = {
    if(hostIp=="all"){
      println("bye1")
      pool.shutdownNow()
      pool.shutdown()
      println("bye2")
    }
    else{
      val runningTasksOption = tasks.get(hostIp)

      if( runningTasksOption.nonEmpty )
      {
        val runningTask = runningTasksOption.get
        if( runningTask.nonEmpty ) {
          runningTask foreach { _.cancel(true)}
        }
      }
      println( tasks.size )
    }
  }
}

class Runner {
  def execute(path:String, fileName:String, actorRef:ActorRef, hostIp:String, sessionKey:String) ={
    val scriptFile = new File(path)
    try{
      val className = fileName.split('.')(0)
      val sse = ScalaScriptEngine.onChangeRefresh( scriptFile )
      sse.refresh
      val scriptInstance = sse.newInstance[scriptable.Script]("scriptable." + className)
      scriptInstance.sessionKey = sessionKey
      println( sessionKey )
      scriptInstance.actor = actorRef
      Pool.run(scriptInstance, hostIp)
    }catch {
      case e:Exception => actorRef ! LogLine(e.getMessage,0,sessionKey)
    }finally {

    }
  }
}
