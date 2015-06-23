/**
 * Created by shkim on 15. 6. 18.
 */

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
    //println(t.result)
    //println(t.connect("a",4))
  }


}
