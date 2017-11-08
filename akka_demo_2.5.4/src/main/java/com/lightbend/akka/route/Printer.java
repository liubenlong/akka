package com.lightbend.akka.route;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Random;

public class Printer extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    static public Props props() {
        return Props.create(Printer.class, () -> new Printer());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Greeting.class, greeting -> {
                    log.info(greeting.message+"  hashcode="+this.hashCode());
                    if(new Random().nextInt(2) == 0){
                        log.info("i am dead. hashcode="+this.hashCode());
                        getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
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