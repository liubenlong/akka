package com.lightbend.akka.mailbox;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;

import java.util.Arrays;

class Demo extends AbstractActor {
    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static public Props props() {
        return Props.create(Demo.class, () -> new Demo());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchAny(message -> {
            log.info(message.toString());
        }).build();
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("timerAkka", ConfigFactory.load("dev.conf"));

//        ActorRef myActor = system.actorOf(Demo.props().withDispatcher("akka.prio-dispatcher"), "demo");
//        Arrays.asList("lowpriority", "lowpriority", "highpriority", "pigdog", "pigdog2", "pigdog3", "highpriority")
//                .forEach(o -> myActor.tell(o, ActorRef.noSender()));


        ActorRef myActor = system.actorOf(Demo.props().withDispatcher("akka.control-aware-dispatcher"), "demo");
        Arrays.asList("foo", "bar", new MyControlMessage("123"), "tom", PoisonPill.getInstance())
                .forEach(o -> myActor.tell(o, ActorRef.noSender()));
    }
}

