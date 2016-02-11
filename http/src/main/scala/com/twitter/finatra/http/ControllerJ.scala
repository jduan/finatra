package com.twitter.finatra.http

import com.twitter.finagle.http.{Request, Response}

trait MyAction {
  def action(request: Request): Response
}

abstract class ControllerJ extends Controller {
  def addAction(path: String, action: MyAction): Unit = {
    get(path) { request: Request =>
      action.action(request)
    }
  }
}
