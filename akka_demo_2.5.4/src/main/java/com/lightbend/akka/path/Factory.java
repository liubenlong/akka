package com.lightbend.akka.path;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.lightbend.akka.sample.Printer;

//#printer-messages
public class Factory extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static public Props props() {
        return Props.create(Factory.class, () -> new Factory());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FactoryMsg.class, f -> {
                    log.info(f.message+". I am factory");
                    ActorRef printer333 = getContext().actorOf(Printer.props(), "printer333");
                })
                .matchAny(o -> log.error("the msg:{}  is not support!", o))
                .build();
    }

    static public class FactoryMsg {
        public final String message;

        public FactoryMsg(String message) {
            this.message = message;
        }
    }
}