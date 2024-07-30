package AkkaStreams1

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object AkkaStreams extends App {
  implicit  val system = ActorSystem("system")
  implicit  val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 10)
  val flow = Flow[Int].map(x=>x+1)
  val sink = Sink.foreach[Int](println)

//  source.to(sink).run()
//  source.via(flow).to(sink).run()


  val simpleSource:Source[Int, NotUsed] = Source(1 to 10)
  val simpleFlow = Flow[Int].map(_+1)
  val simpleFlow2 = Flow[Int].map(_*10)
  val simpleSink = Sink.foreach[Int](println)

  simpleSource.async
    .via(simpleFlow).async
    .via(simpleFlow2).async
    .to(simpleSink).run()


}