package receiver

import akka.actor.{ActorRef, Actor}
import runner.{Pool, Runner}
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
      cmd.op match {
        case "execute"=>{
          val path: Path = Path ("./scriptable")
          path.createDirectory(failIfExists=false)
          File("./scriptable/"+cmd.fileName).writeAll(cmd.code)
          // run exec
          val runner = new Runner
          remoteHost = sender
          runner.execute("./scriptable",cmd.fileName, self, cmd.hostIp, cmd.sessionKey)
        }
        case "kill" => {
          //println("kill")
          Pool.kill(cmd.hostIp, cmd.sessionKey)
        }
        case _ => {}
      }
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
