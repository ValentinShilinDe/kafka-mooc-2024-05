object scalainto extends App {
  //1. everything is object
  val a = 1 + 1
  val b: Unit = println("sdsdg")

  val inc: Int => Int = x => x + 1
  lazy val cond : Boolean = true

  val x1: String = if (cond) "1" else "2"

  val x2: Any = if (cond) println("asasf") else "2"

  val x: Any = do {
   println("1111")
  } while(!cond)

  val l = List(1,2,3)
  l.foreach(println)


}