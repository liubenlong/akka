package com.lightbend.akka.watch;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.lightbend.akka.sample.Printer;

public class MyWatchActor extends AbstractActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    static public Props props(ActorRef actorRef) {
        return Props.create(MyWatchActor.class, () -> new MyWatchActor(actorRef));
    }

    public MyWatchActor(ActorRef actorRef) {
        getContext().watch(actorRef);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Terminated.class, t -> log.info(t.getActor().path() + " has Terminated."))
                .match(Printer.Greeting.class, greeting -> log.info(greeting.message))
                .matchAny(o -> log.error("the msg:{}  is not support!", o))
                .build();
    }
}