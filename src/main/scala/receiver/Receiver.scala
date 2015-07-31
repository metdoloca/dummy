package receiver

import akka.actor.{ActorRef, Actor}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import runner.{Pool, Runner}
import scriptable.{LogLine, Command}

import scala.reflect.io.{File, Path}

class Receiver extends Actor{
  val logger = Logger(LoggerFactory.getLogger(this.getClass))
  var remoteHost:ActorRef = null
  def receive = {
    case cmd:Command =>
      cmd.op match {
        case "execute"=>
          val path: Path = Path ("./scriptable")
          path.createDirectory(failIfExists=false)
          File("./scriptable/"+cmd.fileName).writeAll(cmd.code)
          // run exec
          val runner = new Runner
          remoteHost = sender()
          logger.debug(s"[op:execute] hostIp=${cmd.hostIp},fileName=${cmd.fileName},sessionKey=${cmd.sessionKey}")
          runner.execute("./scriptable",cmd.fileName, self, cmd.hostIp, cmd.sessionKey)
        case "kill" =>
          logger.debug(s"[op:kill] hostIp=${cmd.hostIp},sessionKey=${cmd.sessionKey}")
          Pool.kill(cmd.hostIp, cmd.sessionKey)
        case _ => None
      }
    case log:LogLine =>{
      remoteHost ! log
    }
    case _ => println("Received unknown msg ")
  }
}
