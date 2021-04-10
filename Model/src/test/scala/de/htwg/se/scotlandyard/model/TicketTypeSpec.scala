package de.htwg.se.scotlandyard.model

import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

class TicketTypeSpec extends WordSpec with Matchers with PrivateMethodTester {
  "TicketType" when {
    "of(String) is called" should {
      "return Type Taxi" in {
        TicketType.of("t") should be(TicketType.Taxi)
      }
      "return Type Bus" in {
        TicketType.of("b") should be(TicketType.Bus)
      }
      "return Type Underground" in {
        TicketType.of("u") should be(TicketType.Underground)
      }
      "return Type Black" in {
        TicketType.of("x") should be(TicketType.Black)
      }
      "return Type Invalid" in {
        TicketType.of("adsf") should be(TicketType.Invalid)
      }

    }
    "of(Char) is called" should {
      "return Type Taxi" in {
        TicketType.of('t') should be(TicketType.Taxi)
      }
      "return Type Bus" in {
        TicketType.of('b') should be(TicketType.Bus)
      }
      "return Type Underground" in {
        TicketType.of('u') should be(TicketType.Underground)
      }
      "return Type Black" in {
        TicketType.of('x') should be(TicketType.Black)
      }
      "return Type Invalid" in {
        TicketType.of('?') should be(TicketType.Invalid)
      }
    }
    "apply is called" should {
      "return the correct value" in {
        TicketType.apply(0) should be(TicketType.Taxi)
      }
    }
  }
}