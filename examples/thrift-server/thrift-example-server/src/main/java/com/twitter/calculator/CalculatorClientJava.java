package com.twitter.calculator;

import com.twitter.calculator.thriftscala.Calculator;
import com.twitter.calculator.thriftscala.Calculator$Increment$Args;
import com.twitter.calculator.thriftscala.Calculator$Increment$Result;
import com.twitter.calculator.thriftscala.Calculator.ServiceIfaceBuilder$;
import com.twitter.finagle.Service;
import com.twitter.finagle.SimpleFilter;
import com.twitter.finagle.thrift.ClientId;
import com.twitter.util.Await;
import com.twitter.util.Future;
import scala.runtime.AbstractFunction1;

public class CalculatorClientJava {
    public static void main(String[] args) throws Exception {
        ServiceIfaceBuilder$ builder = ServiceIfaceBuilder$.MODULE$;
        Calculator.ServiceIface serviceIface = new ThriftClient().withClientId(new ClientId("client123")).
                newServiceIface("localhost:9999", "thrift_client", builder);

        SimpleFilter<Calculator$Increment$Args, Calculator$Increment$Result> simpleFilter = new SimpleFilter<Calculator$Increment$Args, Calculator$Increment$Result>() {
            @Override
            public Future<Calculator$Increment$Result> apply(Calculator$Increment$Args request, Service<Calculator$Increment$Args, Calculator$Increment$Result> service) {
                Future<Calculator$Increment$Result> future = service.apply(request);
                return future.map(new AbstractFunction1<Calculator$Increment$Result, Calculator$Increment$Result>() {
                    @Override
                    public Calculator$Increment$Result apply(Calculator$Increment$Result result) {
                        if (!result.unknownClientIdError().isEmpty()) {
                            System.out.println("encountered UnknownClientIdError!");
                        }
                        return result;
                    }
                });
            }
        };

        Service<Calculator$Increment$Args, Calculator$Increment$Result> incrementService = simpleFilter.andThen(serviceIface.increment());
        Future<Calculator$Increment$Result> resultFuture = incrementService.apply(new Calculator$Increment$Args(99));
        Calculator$Increment$Result result = Await.result(resultFuture);
        System.out.println("result is: " + result);
    }
}
