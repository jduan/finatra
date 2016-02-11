package com.twitter.hello.server;

import com.twitter.finagle.ListeningServer;
import com.twitter.finatra.http.HttpServerJ;
import com.twitter.finatra.http.routing.HttpRouter;

public class HelloWorldServer extends HttpServerJ {
    @Override
    public void configureHttp(HttpRouter router) {
        router.add(new UserController());
    }
}
