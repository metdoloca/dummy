package script

class Main extends SimpleTrait{
	val r = "%d : change me while example runs!  asfsdf"

	def start():Unit={
		println( "script start")
		connect("172.16.1.12",20493)
		run()
	}

	def run()={
		println("run")
		end()
	}

	def end()={
		println( "script end")
	}

	override def result = {
		import Counter._
		counter += 1
		r.format(counter)
	}


}

object Counter
{
	var counter = 0
}
