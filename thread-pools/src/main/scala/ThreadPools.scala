import akka.actor._
import akka.routing._

import scala.concurrent._
import scala.concurrent.duration.Duration

object Example0 extends App {
  println(s"\nOne by one")

  println(s"\nPress ENTER to continue. Press ENTER to stop.")
  scala.io.StdIn.readLine()

  val start = System.currentTimeMillis

  println(s"sending messages")
  (1 to 8).foreach { i =>
    val msg = i
    Thread.sleep(1000)
    if (msg % 4 == 0) {
      val end = System.currentTimeMillis
      println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
    }
  }

  val end = System.currentTimeMillis
  println(s"\nTook ${end - start} millis to send\n")

  scala.io.StdIn.readLine()
  println("\nDone")
}

object Example1 extends App {
  println(s"\nFutures with global ExecutionContext")

  println(s"\nPress ENTER to continue. Press ENTER to stop.")
  scala.io.StdIn.readLine()

  val start = System.currentTimeMillis

  println(s"sending messages")
  import scala.concurrent.ExecutionContext.Implicits.global
  (1 to 80).foreach { i =>
    Future {
      val msg = i
      Thread.sleep(1000)
      if (msg % 10 == 0) {
        val end = System.currentTimeMillis
        println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
      }
    }
  }

  println(s"sending 0")
  Future {
    val msg = 0
    val end = System.currentTimeMillis
    println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
  }

  val end = System.currentTimeMillis
  println(s"\nTook ${end - start} millis to send\n")

  scala.io.StdIn.readLine()
  println("\nDone")
}

object ActorA {
  case class Msg(start: Long, i: Int)
  case object Boom
}

class ActorA extends Actor {
  import ActorA._

  def receive = {
    case msg: Msg =>
      Thread.sleep(1000)
      if (msg.i % 10 == 0) {
        val end = System.currentTimeMillis
        println(s"${context.self.path}, ${Thread.currentThread} finished ${msg.i} in ${end - msg.start} millis")
      }

    case Boom =>
      Thread.sleep(1000)
      throw new Exception("BOOM!!!")
  }
}

object Example2 extends App {
  println(s"\nOne actor")

  println(s"\nPress ENTER to continue. Press ENTER to stop.")
  scala.io.StdIn.readLine()

  val actorSystem = ActorSystem("example")

  val start = System.currentTimeMillis

  println(s"sending messages")
  
  (1 to 80).foreach { i =>
    val a1 = actorSystem.actorOf(Props(classOf[ActorA]))
    a1 ! ActorA.Msg(start, i)
  }

  println(s"sending 0")
  import scala.concurrent.ExecutionContext.Implicits.global
  Future {
    val msg = 0
    val end = System.currentTimeMillis
    println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
  }

  val end = System.currentTimeMillis
  println(s"\nTook ${end - start} millis to send\n")

  scala.io.StdIn.readLine()
  Await.result(actorSystem.terminate(), Duration("10 seconds"))
  println("\nDone")
}

object Example3 extends App {
  println(s"\nRouter with 40 actors, default dispatcher")

  println(s"\nPress ENTER to continue. Press ENTER to stop.")
  scala.io.StdIn.readLine()

  val actorSystem = ActorSystem("example")

  val start = System.currentTimeMillis

  val r1: ActorRef = actorSystem.actorOf(RoundRobinPool(40).props(Props[ActorA]), "r1")

  println(s"sending messages")
  (1 to 80).foreach { i =>
    r1 ! ActorA.Msg(start, i)
  }

  println(s"sending 0")
  import scala.concurrent.ExecutionContext.Implicits.global
  Future {
    val msg = 0
    val end = System.currentTimeMillis
    println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
  }

  val end = System.currentTimeMillis
  println(s"\nTook ${end - start} millis to send\n")

  scala.io.StdIn.readLine()
  Await.result(actorSystem.terminate(), Duration("10 seconds"))
  println("\nDone")
}

object Example4 extends App {
  println(s"\nRouter with 40 actors, blocking-dispatcher")

  println(s"\nPress ENTER to continue. Press ENTER to stop.")
  scala.io.StdIn.readLine()

  val actorSystem = ActorSystem("example")

  val start = System.currentTimeMillis

  val r1: ActorRef = actorSystem.actorOf(RoundRobinPool(40).props(Props[ActorA]).withDispatcher("blocking-dispatcher"), "r1")

  println(s"sending messages")
  (1 to 80).foreach { i =>
    r1 ! ActorA.Msg(start, i)
  }

  println(s"sending 0")
  import scala.concurrent.ExecutionContext.Implicits.global
  Future {
    val msg = 0
    val end = System.currentTimeMillis
    println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
  }

  val end = System.currentTimeMillis
  println(s"\nTook ${end - start} millis to send\n")

  scala.io.StdIn.readLine()
  Await.result(actorSystem.terminate(), Duration("10 seconds"))
  println("\nDone")
}

object Example5 extends App {
  println(s"\nRouter with 40 actors, pinned-dispatcher")

  println(s"\nPress ENTER to continue. Press ENTER to stop.")
  scala.io.StdIn.readLine()

  val actorSystem = ActorSystem("example")

  val start = System.currentTimeMillis

  val r1: ActorRef = actorSystem.actorOf(RoundRobinPool(40).props(Props[ActorA]).withDispatcher("pinned-dispatcher"), "r1")

  println(s"sending messages")
  (1 to 80).foreach { i =>
    r1 ! ActorA.Msg(start, i)
  }

  println(s"sending 0")
  import scala.concurrent.ExecutionContext.Implicits.global
  Future {
    val msg = 0
    val end = System.currentTimeMillis
    println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
  }

  val end = System.currentTimeMillis
  println(s"\nTook ${end - start} millis to send\n")

  scala.io.StdIn.readLine()
  Await.result(actorSystem.terminate(), Duration("10 seconds"))
  println("\nDone")
}

object Example6 extends App {
  println(s"\nFutures with blocking-dispatcher")

  println(s"\nPress ENTER to continue. Press ENTER to stop.")
  scala.io.StdIn.readLine()

  val actorSystem = ActorSystem("example")

  val start = System.currentTimeMillis

  println(s"sending messages")
  implicit val blockingDispatcher = actorSystem.dispatchers.lookup("blocking-dispatcher")
  (1 to 80).foreach { i =>
    Future {
      val msg = i
      Thread.sleep(1000)
      if (msg % 10 == 0) {
        val end = System.currentTimeMillis
        println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
      }
    }
  }

  println(s"sending 0")
  Future {
    val msg = 0
    val end = System.currentTimeMillis
    println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
  }

  val end = System.currentTimeMillis
  println(s"\nTook ${end - start} millis to send\n")

  scala.io.StdIn.readLine()
  Await.result(actorSystem.terminate(), Duration("10 seconds"))
  println("\nDone")
}

object Example7 extends App {
  println(s"\nFutures with blocking-dispatcher, Exception")

  println(s"\nPress ENTER to continue. Press ENTER to stop.")
  scala.io.StdIn.readLine()

  val actorSystem = ActorSystem("example")

  val start = System.currentTimeMillis

  println(s"sending messages")
  implicit val blockingDispatcher = actorSystem.dispatchers.lookup("blocking-dispatcher")
  (1 to 80).foreach { i =>
    Future {
      try {
        val msg = i
        Thread.sleep(1000)
        if (msg % 40 == 0) {
          throw new Exception("BOOM!!!")
        }
        if (msg % 10 == 0) {
          val end = System.currentTimeMillis
          println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
        }
      } catch {
        case e: Exception =>
          println(s"Exception! $e")
      }
    }
  }

  println(s"sending 0")
  Future {
    val msg = 0
    val end = System.currentTimeMillis
    println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
  }

  val end = System.currentTimeMillis
  println(s"\nTook ${end - start} millis to send\n")

  scala.io.StdIn.readLine()
  Await.result(actorSystem.terminate(), Duration("10 seconds"))
  println("\nDone")
}

object Example8 extends App {
  println(s"\nRouter with 40 actors, pinned-dispatcher, Exception")

  println(s"\nPress ENTER to continue. Press ENTER to stop.")
  scala.io.StdIn.readLine()

  val actorSystem = ActorSystem("example")

  val start = System.currentTimeMillis

  val r1: ActorRef = actorSystem.actorOf(RoundRobinPool(40).props(Props[ActorA]).withDispatcher("pinned-dispatcher"), "r1")

  println(s"sending messages")
  (1 to 80).foreach { i =>
    if (i % 40 == 0) {
      r1 ! ActorA.Boom
    }
    r1 ! ActorA.Msg(start, i)
  }

  println(s"sending 0")
  import scala.concurrent.ExecutionContext.Implicits.global
  Future {
    val msg = 0
    val end = System.currentTimeMillis
    println(s"${Thread.currentThread} finished $msg in ${end - start} millis")
  }

  val end = System.currentTimeMillis
  println(s"\nTook ${end - start} millis to send\n")

  scala.io.StdIn.readLine()
  Await.result(actorSystem.terminate(), Duration("10 seconds"))
  println("\nDone")
}
