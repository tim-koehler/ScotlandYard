package de.htwg.se.scotlandyard.model

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import spray.json.DefaultJsonProtocol.{IntJsonFormat, jsonFormat4, listFormat}

import scala.concurrent.Future
import scala.io.StdIn
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import spray.json.{DefaultJsonProtocol, JsonFormat, RootJsonFormat}

object ScotlandYardJsonProtocol extends DefaultJsonProtocol {
  implicit val ticketsFormat = jsonFormat4(Tickets)
  implicit val stationsFormat = jsonFormat8(Station)
  ///implicit val mrxFormat = jsonFormat7(MrX)
  //implicit val colorFormat = jsonFormat1(Col)
  //implicit val detectiveFormat = jsonFormat4(Detective)
}

object Rest {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    implicit val ticketsFormat = jsonFormat4(Tickets)

    def fetchTicket(itemId: Int): Future[Option[Tickets]] = Future {
      Some(Tickets(itemId.asInstanceOf[Int], 3, 4))
    }

    val route =
      concat(
        get {
          pathPrefix("item" / IntNumber) { id =>
            // there might be no item for a given id
            val maybeItem: Future[Option[Tickets]] = fetchTicket(id)

            onSuccess(maybeItem) {
              case Some(item) => complete(item)
              case None       => complete(StatusCodes.NotFound)
            }
          }
        }
      )

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}