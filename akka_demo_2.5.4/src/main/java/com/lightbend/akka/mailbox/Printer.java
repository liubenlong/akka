package com.lightbend.akka.mailbox;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Printer extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    static public Props props() {
        return Props.create(Printer.class, () -> new Printer());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Greeting.class, greeting -> {
                    if ("restart".equals(greeting.message)) {
                        int i = 1 / 0;
                    } else {
                        log.info(greeting.message+".hashcode="+this.hashCode());
                    }
                })
                .matchAny(o -> log.error("the msg:{}  is not support!", o))
                .build();
    }


    static public class Greeting {
        public final String message;
        public Greeting(String message) {
            this.message = message;
        }
    }
}