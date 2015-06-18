/**
 * Created by shkim on 15. 6. 18.
 */

import com.googlecode.scalascriptengine.ScalaScriptEngine
import java.io.File

import server.TryMeTrait

object Main extends App{
  println( s"app start, port")

  val sse = ScalaScriptEngine.onChangeRefresh( new File("/home/shkim/scalaChunk") )
  sse.refresh
  while (true) {

    val t = sse.newInstance[TryMeTrait]("server.TryMe")
    println("code version %d, result : %s".format(sse.versionNumber, t.result))
    Thread.sleep(1000)
  }
}
