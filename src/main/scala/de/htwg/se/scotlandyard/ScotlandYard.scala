package de.htwg.se.scotlandyard

import com.google.inject.{Guice, Injector}
import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.aview.rest.Rest
import de.htwg.se.scotlandyard.controller.ControllerInterface

import scala.io.Source

object ScotlandYard {
  val stationsJsonFilePath = "./resources/stations.json"
  val injector: Injector = Guice.createInjector(new ScotlandYardModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

  def main(args: Array[String]): Unit = {
    //val stationsSource: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
    //controller.initializeStations(stationsSource)
    controller.initialize(3)
    Rest.startRestService(controller)
    new Gui(controller)
  }
}
