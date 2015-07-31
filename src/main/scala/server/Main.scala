package server

import java.io.File

import akka.actor.{ActorSystem, PoisonPill, Props}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import receiver.Receiver
import runner.Pool

/**
 * Created by shkim on 15. 6. 18.
 */

object Main extends App{
  val logger = Logger(LoggerFactory.getLogger(this.getClass))
  //val configFile = getClass.getClassLoader.getResource("application.conf").getFile
  val config = ConfigFactory.parseFile(new File("./application.conf"))
  val system = ActorSystem("RemoteSystem" , config)
  val actor = system.actorOf(Props[Receiver], name="receiver")
  // exit wait stdin
  logger.info("press enter : send poison pill 2 actor")
  Console.in.readLine()
  logger.info("send poison pill")
  actor ! PoisonPill
  Pool.kill("all","")
  system.shutdown()
}
