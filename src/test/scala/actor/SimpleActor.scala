package actor

import akka.actor.Actor

/**
 * Created by shkim on 15. 6. 26.
 */
class SimpleActor extends Actor{
  var count:Int = 0
  var tick:Long = System.currentTimeMillis

  def inactive:Receive = {
    case "start" =>
      tick = System.currentTimeMillis
      println(count)
      context.become(active)
    case _ => ;
  }

  def active: Receive = { // This is the behavior when it's active
    case "stop" =>
      context.become(inactive)
    case "next" =>
      //println("doSomethingWith(stream.getItem)")
      count+=1
      if(count %10000000 == 0)
        println(s"actor=$count")
      if( System.currentTimeMillis - tick > 5000 )
        println(s"count/5sec = $count")
      else
        self ! "next"
    case _ => ;
  }

  def receive = inactive // Start out as inactive
}
