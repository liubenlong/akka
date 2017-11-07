package com.lightbend.akka.mailbox;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.MailboxType;
import akka.dispatch.MessageQueue;
import akka.dispatch.ProducesMessageQueue;
import com.typesafe.config.Config;
import scala.Option;

//自定义邮箱
public class MyUnboundedMailbox implements MailboxType, ProducesMessageQueue<MyMessageQueue> {
    // This constructor signature must exist, it will be called by Akka
    public MyUnboundedMailbox(ActorSystem.Settings settings, Config config) {
        // put your initialization code here
        System.out.println("com.lightbend.akka.mailbox.MyUnboundedMailbox.MyUnboundedMailbox");
    }

    // The create method is called to create the MessageQueue
    public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
        return new MyMessageQueue();
    }
}