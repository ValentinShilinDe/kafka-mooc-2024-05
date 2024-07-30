package AkkaDataStreams

import akka.NotUsed
import akka.actor.typed.{ActorSystem, ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors


sealed trait Command
case class StartChild(name: String) extends Command
case class SendMessageToChild(name: String, msg: String, num:Int) extends Command
case class StopChild(name: String) extends Command

object Parent{
  def apply(): Behavior[Command] = withChildren(Map())

  def withChildren(children: Map[String, ActorRef[Command]]): Behavior[Command] =
    Behaviors.setup{ctx =>
      Behaviors.receiveMessage{
        case StartChild(name) =>
          ctx.log.info(s"start child $name")
          val newChild = ctx.spawn(Child(), name)
          withChildren(children + (name-> newChild))
        case msg@SendMessageToChild(name, _, i) =>
          ctx.log.info(s"send message to child $name")
          val childOption = children.get(name)
          childOption.foreach(chilldref => chilldref ! msg)
          Behaviors.same
        case StopChild(name) =>
          ctx.log.info(s"stopping child $name")
          val childOption = children.get(name)
          childOption match {
            case Some(childref) =>
              ctx.stop(childref)
              Behaviors.same
          }

          Behaviors.same

      }
    }
}

object Child {
  def apply(): Behavior[Command] = Behaviors.setup{ctx =>
    Behaviors.receiveMessage{msg=>
      ctx.log.info(s"child got message $msg")
      Behaviors.same
    }
  }
}

object  StartStopSpec extends App {
  def apply(): Behavior[NotUsed] = {
    Behaviors.setup{ctx=>
      val parent = ctx.spawn(Parent(), "parent")
      parent ! StartChild("child1")
      parent ! SendMessageToChild("child1", "safsdfsdfsdf", 1)
      parent ! StopChild("child1")

      for (i<- 1 to 15) parent ! SendMessageToChild("child1", "1111", i)
      Behaviors.same
    }
  }

  val value = StartStopSpec()
  implicit  val system = ActorSystem(value, "akka")
  Thread.sleep(5000)
  system.terminate()
}