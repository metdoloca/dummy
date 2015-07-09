package receiver

import akka.actor.{ActorRef, Actor}
import runner.Runner
import scriptable.{LogLine, Command}

import scala.reflect.io.{File, Path}

object Manager {
  case object Shutdown
}

class Receiver extends Actor{

  var remoteHost:ActorRef = null

  def receive = {
    case msg: String => {
      println("remote received " + msg + " from " + sender)
    }
    case cmd:Command =>{
      // create file
      println( s"fileName = ${cmd.fileName}" )
      val path: Path = Path ("./scriptable")
      path.createDirectory(failIfExists=false)
      File("./scriptable/"+cmd.fileName).writeAll(cmd.code)
      // run exec
      val runner = new Runner
      runner.execute("./scriptable",self)
      remoteHost = sender
    }
    case log:LogLine =>{
      remoteHost ! log
    }
    case Manager.Shutdown =>{
      context stop self
    }
    case _ => println("Received unknown msg ")
  }
}
