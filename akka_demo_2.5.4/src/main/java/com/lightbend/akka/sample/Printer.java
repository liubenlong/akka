package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Optional;

//#printer-messages
public class Printer extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public Printer() {
        log.info("我是构造函数。");
    }

    @Override
    public void preStart() throws Exception {
        log.info("preStart() executed");
        super.preStart();
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        log.info("preRestart() executed");
        super.preRestart(reason, message);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        log.info("postRestart() executed");
        super.postRestart(reason);
    }

    @Override
    public void postStop() throws Exception {
        log.info("postStop() executed");
        super.postStop();
    }

    static public Props props() {
        return Props.create(Printer.class, () -> new Printer());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Greeting.class, greeting -> log.info(greeting.message))
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