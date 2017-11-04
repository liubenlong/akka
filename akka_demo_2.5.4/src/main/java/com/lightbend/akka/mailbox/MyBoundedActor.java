package com.lightbend.akka.mailbox;

import akka.dispatch.BoundedMessageQueueSemantics;
import akka.dispatch.RequiresMessageQueue;

public class MyBoundedActor extends Printer
  implements RequiresMessageQueue<BoundedMessageQueueSemantics> {

}