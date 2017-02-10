package com.twitter.calculator

import com.twitter.finagle.{Service, SimpleFilter, Thrift}
import com.twitter.calculator.thriftscala.Calculator
import com.twitter.calculator.thriftscala.Calculator._
import com.twitter.finagle.service.{Backoff, RetryBudget, RetryFilter, RetryPolicy}
import com.twitter.finagle.stats.NullStatsReceiver
import com.twitter.finagle.thrift.ClientId
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.{Await, Future, Return, Try}
import com.twitter.conversions.time._

object CalculatorClient {
  def main(args: Array[String]): Unit = {
    val client = Thrift.client.withClientId(new ClientId("client123")).
      newServiceIface[Calculator.ServiceIface]("localhost:9999", "thrift_client")

    val logExceptionFilter = new SimpleFilter[Increment.Args, Increment.Result] {
      def apply(req: Increment.Args, service: Service[Increment.Args, Increment.Result]): Future[Increment.Result] = {
        val future = service(req)
        future.map(result => {
          // Note: only declared exceptions can be handled here
          result.noClientIdError.foreach(ex => println("Encountered a NoClientIdError: " + ex))
          result.unknownClientIdError.foreach(ex => println("Encountered a UnknownClientIdError: " + ex))
          result.serverError.foreach(ex => println("Encountered a NoClientIdError: " + ex))
          result
        })
      }
    }

    val budget: RetryBudget = RetryBudget()
    val policy: RetryPolicy[(Increment.Args, Try[Increment.Result])] =
      RetryPolicy.backoff(Backoff.equalJittered(10.milliseconds, 10.seconds)) {
        case (_, Return(Increment.Result(success, serverError, unknownClientIdError, noClientIdError))) if ! serverError.isEmpty => true
        case (_, Return(Increment.Result(success, serverError, unknownClientIdError, noClientIdError))) if ! unknownClientIdError.isEmpty => true
        case (_, Return(Increment.Result(success, serverError, unknownClientIdError, noClientIdError))) if ! noClientIdError.isEmpty => true
        case _ => false
      }
    val retryFilter = new RetryFilter[Increment.Args, Increment.Result](
      policy,
      DefaultTimer.twitter,
      NullStatsReceiver,
      budget
    )

    val filteredClient = retryFilter andThen logExceptionFilter andThen client.increment
    val result = filteredClient(Increment.Args(33))
    // onFailure gets called if there's any non-declared exceptions were thrown by the server
    result.onFailure(t => println("onFailure: " + t))
    result.onSuccess(result => println("onSuccess result is: " + result))
    val v = Await.result(result)
    println(v)
  }
}
