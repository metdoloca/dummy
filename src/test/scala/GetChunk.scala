/**
 * Created by shkim on 15. 6. 18.
 */

import actor.SimpleActor
import akka.actor.{Props, ActorSystem}
import com.googlecode.scalascriptengine.ScalaScriptEngine
import org.scalatest._
import collection.mutable.Stack
import java.io.File

class GetChunk extends FlatSpec with Matchers{

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be (2)
    stack.pop() should be (1)
  }

  "Script File" should "run" in {
    val path = getClass.getResource("/script") // /SimpleTrait.scala
    val scriptFile = new File(path.getPath)

    val sse = ScalaScriptEngine.onChangeRefresh( scriptFile )
    sse.refresh

    val t = sse.newInstance[script.SimpleTrait]("script.Main")
    t.start
    t.start
    //println(t.result)
    //println(t.connect("a",4))
  }

//  "actor looping test" should "run" in {
//
//
//    val tick:Long = System.currentTimeMillis
//    var count:Int=0;
//    while(System.currentTimeMillis - tick < 5000){
//      count+=1
//      if(count %10000000 == 0)
//        println(s"simple=$count")
//    }
//    println( s"simple loop count $count")
//
//
//    val system = ActorSystem("HelloSystem")
//    // default Actor constructor
//    println("a")
//    val helloActor = system.actorOf(Props[SimpleActor], name = "helloactor")
//
//    println("b")
//    helloActor ! "start"
//    println("c")
//    helloActor ! "next"
//    //Thread.sleep(5000)
//    //helloActor ! "stop"
//
//
//
//    Thread.sleep(5000)
//
//
//    //Thread.sleep(51000)
//    //helloActor ! "buenos dias"
//  }

}
