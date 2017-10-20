package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

//#greeter-messages
public class Greeter extends AbstractActor {
    private final String message;
    private final ActorRef printerActor;
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private String greeting = "";
    //#greeter-messages

    public Greeter(String message, ActorRef printerActor) {
        this.message = message;
        this.printerActor = printerActor;
    }

    //#greeter-messages
    static public Props props(String message, ActorRef printerActor) {
        return Props.create(Greeter.class, () -> new Greeter(message, printerActor));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WhoToGreet.class, wtg -> this.greeting = message + ", " + wtg.who)
                .match(Greet.class, x -> {
                    printerActor.tell(new Printer.Greeting(greeting), getSelf());
                    getSender().tell(greeting, getSelf());
                })
                .matchAny(o -> log.error("received unknown message"))
                .build();
    }

    //#greeter-messages
    static public class WhoToGreet {
        public final String who;

        public WhoToGreet(String who) {
            this.who = who;
        }
    }

    static public class Greet {
        public Greet() {
        }
    }
}
