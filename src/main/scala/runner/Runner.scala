package runner
import akka.actor.{ActorRef, Actor}
import java.io.File
import java.util.concurrent.{Future, Executors, ExecutorService}
import akka.actor.ActorRef
import com.googlecode.scalascriptengine.ScalaScriptEngine
import scriptable.{LogLine, Script}

import scala.collection.mutable.HashMap
import scala.collection.mutable.MultiMap
import scala.collection.mutable.Set

object Pool{
  implicit def runnable(f:()=> Unit):Runnable = new Runnable(){def run() = f() }
  var pool = Executors.newCachedThreadPool()
  var running = true
  //val runningSet = new mutable.HashMap[String,List[Future[Int]]] with mutable.MultiMap[String, Future[Int]]
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
        }
        catch{
          case e:Exception => {}
        }
        finally{
          println("forced killed")
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
      case e:Exception =>{
        actorRef ! LogLine(e.getMessage,0,sessionKey)
      }
    }finally {

    }
  }
}
