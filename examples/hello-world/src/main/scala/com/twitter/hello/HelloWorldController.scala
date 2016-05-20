package com.twitter.hello

import java.util.concurrent.Executors

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.{Future, FuturePools, FuturePool}

class HelloWorldController extends Controller {

  val futurePool = FuturePools.newInterruptibleFuturePool(Executors.newFixedThreadPool(50))

  get("/hi") { request: Request =>
    info("hi")
    val ret: Future[String] = futurePool {
      Thread sleep 5000
      "Hello " + request.params.getOrElse("name", "unnamed")
    }

    ret
  }

  post("/hi") { hiRequest: HiRequest =>
    "Hello " + hiRequest.name + " with id " + hiRequest.id
  }
}
