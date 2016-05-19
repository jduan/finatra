package com.twitter.hello.server;

import com.twitter.util.Future;
import scala.runtime.AbstractFunction1;

import javax.inject.Inject;

public class HelloService {
    @Inject
    FakeNameServiceClient nameServiceClient;

    public Future<String> hi(String name) {
        return nameServiceClient.isNameSane(name).map(new AbstractFunction1<Boolean, String>() {
            @Override
            public String apply(Boolean isSane) {
                if (isSane) {
                    return "Hello " + name + "\n";
                } else {
                    return "Bad name!";
                }
            }
        });
    }
}
