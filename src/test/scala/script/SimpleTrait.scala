package script

trait SimpleTrait {
  def result: String
  def connect(ip:String, port:Int): Unit = {
    println(s"on connect $ip:$port")
  }

  def start():Unit
}
