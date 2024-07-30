package AkkaDSL

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source, Zip}

object AkkaStreamGraph {
  implicit val system = ActorSystem("vsdsdf")
  implicit val mat = ActorMaterializer()

  val graph = GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._
    //1
    val input = builder.add(Source(1 to 10))
    val inc = builder.add(Flow[Int].map(x => x + 1))
    val mul = builder.add(Flow[Int].map(x => x * 10))
    val output = builder.add(Sink.foreach[(Int, Int)](println))

    val broadcast = builder.add(Broadcast[Int](2))
    val zip = builder.add(Zip[Int, Int])

    //2.shape
    input ~> broadcast

    broadcast.out(0) ~> inc ~> zip.in0
    broadcast.out(1) ~> mul ~> zip.in1
    zip.out ~> output
    //4 close shape
    ClosedShape
  }

  def main(args: Array[String]): Unit = {
    RunnableGraph.fromGraph(graph).run()
  }


}