package com.twitter.hello.server;

import com.twitter.util.Future;
import com.twitter.util.FuturePool;
import com.twitter.util.FuturePools;
import scala.runtime.AbstractFunction0;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is a fake client to an RPC service in order to demonstrate how to making "blocking" calls to RPC servers.
 * For a real RPC service, you want to use Finagle library to create a client of the service. You get the benefit
 * of having a non-blocking client too.
 */
public class FakeNameServiceClient {
    private final FuturePool futurePool;

    public FakeNameServiceClient() {
        ExecutorService executors = Executors.newFixedThreadPool(50);
        this.futurePool = FuturePools.newInterruptibleFuturePool(executors);
    }

    public Future<Boolean> isNameSane(String name) {
        return futurePool.apply(new AbstractFunction0<Boolean>() {
            @Override
            public Boolean apply() {
                // This is to mimic a blocking RPC request a remote service.
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    // This is not production code. It's ok if we are interrupted.
                }
                // This check is for demonstration purpose only.
                return name.length() >= 5;
            }
        });
    }
}

