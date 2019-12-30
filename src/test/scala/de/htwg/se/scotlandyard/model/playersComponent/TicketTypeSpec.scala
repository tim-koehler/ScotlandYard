package de.htwg.se.scotlandyard.model.playersComponent

import de.htwg.se.scotlandyard.util.TicketType
import org.scalatest.{Matchers, WordSpec}

class TicketTypeSpec extends WordSpec with Matchers{
  "Enumeration TicketType" should {
    "should have have 3 values" in {
      TicketType.values.toList(0) shouldBe(TicketType.Taxi)
      TicketType.values.toList(1) shouldBe(TicketType.Bus)
      TicketType.values.toList(2) shouldBe(TicketType.Underground)
    }
  }
}
