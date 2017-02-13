package com.twitter.calculator

import com.twitter.finagle.Thrift

class ThriftClient extends Thrift.Client {
  override def newServiceIface[ServiceIface](dest: String, label: String): ServiceIface = {
    super.newServiceIface(dest, label)
  }
}
