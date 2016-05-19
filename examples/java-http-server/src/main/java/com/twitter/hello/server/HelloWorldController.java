package com.twitter.hello.server;

import com.twitter.finatra.http.JavaController;

import javax.inject.Inject;

public class HelloWorldController extends JavaController {

    @Inject
    HelloService helloService;

    public void configureRoutes() {
        get("/hello", request ->
            helloService.hi(request.getParam("name")));

        get("/goodbye", request ->
                new GoodbyeResponse("guest", "cya", 123));
    }
}
