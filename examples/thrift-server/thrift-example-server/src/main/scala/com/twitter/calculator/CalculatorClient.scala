package com.twitter.calculator

import com.twitter.finagle.Thrift
import com.twitter.calculator.thriftscala.Calculator
import com.twitter.calculator.thriftscala.Calculator._
import com.twitter.finagle.thrift.ClientId
import com.twitter.util.Await

object CalculatorClient {
  def main(args: Array[String]): Unit = {
    val client = Thrift.client.withClientId(new ClientId("client123")).
      newServiceIface[Calculator.ServiceIface]("localhost:9999", "thrift_client")
    val result = client.increment(Increment.Args(33))
    val v = Await.result(result)
    println(v)
  }
}
