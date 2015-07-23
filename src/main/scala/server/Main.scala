package server

import java.io.File

import akka.actor.{ActorSystem, PoisonPill, Props}
import com.typesafe.config.ConfigFactory
import receiver.Receiver

/**
 * Created by shkim on 15. 6. 18.
 */

object Main extends App{
  val configFile = getClass.getClassLoader.getResource("application.conf").getFile
  val config = ConfigFactory.parseFile(new File(configFile))
  val system = ActorSystem("RemoteSystem" , config)
  val actor = system.actorOf(Props[Receiver], name="receiver")
  // exit wait stdin
  println("press enter : send poison pill 2 actor")
  Console.in.readLine()
  println("send poison pill")
  actor ! PoisonPill
  //val stopped:Future[Boolean] = gracefulStop( actor,5 seconds,Manager.Shutdown)
}
