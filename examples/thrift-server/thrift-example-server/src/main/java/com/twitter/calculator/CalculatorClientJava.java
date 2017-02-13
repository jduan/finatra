package com.twitter.calculator;

import com.twitter.calculator.thriftscala.Calculator;
import com.twitter.calculator.thriftscala.Calculator.ServiceIfaceBuilder$;
import com.twitter.finagle.thrift.ClientId;

public class CalculatorClientJava {
    public static void main(String[] args) {
        ServiceIfaceBuilder$ builder = ServiceIfaceBuilder$.MODULE$;
        Calculator.ServiceIface serviceIface = new ThriftClient().withClientId(new ClientId("client123")).
                newServiceIface("localhost:9999", "thrift_client", builder);
    }
}
