package com.twitter.calculator

import com.twitter.calculator.thriftscala.Calculator
import com.twitter.calculator.thriftscala.Calculator._
import com.twitter.finatra.thrift.Controller
import com.twitter.util.Future
import javax.inject.Singleton

import com.twitter.finatra.thrift.thriftscala.NoClientIdError

@Singleton
class CalculatorController
  extends Controller
  with Calculator.BaseServiceIface {

  override val addNumbers = handle(AddNumbers) { args: AddNumbers.Args =>
    info(s"Adding numbers $args.a + $args.b")
    Future.value(args.a + args.b)
  }

  override val addStrings = handle(AddStrings) { args: AddStrings.Args =>
    Future.value(
      (args.a.toInt + args.b.toInt).toString)
  }

  override val increment = handle(Increment) { args: Increment.Args =>
    println("increment called")
//    Future.value(args.a + 1)
//    Future.exception(new RuntimeException("bam!"))
    Future.exception(new NoClientIdError("no client id"))
  }
}
