import java.nio.charset.StandardCharsets
import java.util.Base64

import org.scalatest.{Matchers, FlatSpec}

class base64 extends FlatSpec with Matchers{

  "base64" should "encode" in {
    println( Base64.getEncoder.encodeToString("G1ZsdwhlLRldHF4jFXspD1skSGZ5LA9BU1UJIxUPDENQSTRKGVEwCkt2QjA6QUsRVB93dHEJQg51TVkFYWw0RBl6UU5WURMHKUZoAHI9fk1AWR13cBIwcAFxbkgEW3t2BnhoZmY".getBytes(StandardCharsets.UTF_8)) )
    println( Base64.getDecoder.decode("G1ZsdwhlLRldHF4jFXspD1skSGZ5LA9BU1UJIxUPDENQSTRKGVEwCkt2QjA6QUsRVB93dHEJQg51TVkFYWw0RBl6UU5WURMHKUZoAHI9fk1AWR13cBIwcAFxbkgEW3t2BnhoZmY".getBytes(StandardCharsets.UTF_8)).toString )


    var temp = Base64.getDecoder.decode("G1ZsdwhlLRldHF4jFXspD1skSGZ5LA9BU1UJIxUPDENQSTRKGVEwCkt2QjA6QUsRVB93dHEJQg51TVkFYWw0RBl6UU5WURMHKUZoAHI9fk1AWR13cBIwcAFxbkgEW3t2BnhoZmY+CiF7QXNrdQsjc1RzHwV8chRzHAssehZNQgQ=")
    //temp + 2 0x00, 0x00

    //Array.concat(
    //Array.cop
    //temp.
    println(temp.toString())

    temp ++= Array[Byte]( 0x00, 0x00)
    println(temp.toString())
    //Array.concat[Byte]( )

  }

}
