package de.htwg.se.yourgame.model

import org.scalatest._

class StudentSpec extends FlatSpec with Matchers {
  "A Student" should "have a name" in {
    Student("Your Name").name should be("Your Name")
  }
}
