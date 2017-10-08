package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Optional;

//#printer-messages
public class Printer extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

//    public Printer() {
//        log.info("我是构造函数。,hashCode={}", this.hashCode());
//    }
//
//    @Override
//    public void preStart() throws Exception {
//        log.info("preStart() executed,hashCode={}", this.hashCode());
//        super.preStart();
//    }
//
//    @Override
//    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
//        log.info("preRestart() executed,hashCode={}", this.hashCode());
//        super.preRestart(reason, message);
//    }
//
//    @Override
//    public void postRestart(Throwable reason) throws Exception {
//        log.info("postRestart() executed,reason={},hashCode={}", reason.getMessage(), this.hashCode());
//        super.postRestart(reason);
//    }
//
//    @Override
//    public void postStop() throws Exception {
//        log.info("postStop() executed,hashCode={}", this.hashCode());
//        super.postStop();
//    }

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