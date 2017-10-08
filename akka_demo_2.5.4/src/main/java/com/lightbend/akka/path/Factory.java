package com.lightbend.akka.path;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.lightbend.akka.sample.Printer;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

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
                    if ("find".equals(f.message)) {
                        Future<ActorRef> future = getContext().actorSelection("printer*").resolveOne(Duration.create(2, TimeUnit.SECONDS));
                        future.onComplete(new OnComplete<ActorRef>(){
                            @Override
                            public void onComplete(Throwable throwable, ActorRef actorRef) throws Throwable {
                                actorRef.tell(new Printer.Greeting("hello word."), getSelf());
                            }
                        }, getContext().getSystem().dispatcher());
                    } else {
                        log.info("msg={}. I am factory.my path={}", f.message, getSelf().path().toString());
                        getContext().actorOf(Printer.props(), "printer444");
                        getContext().actorOf(Printer.props(), "printer333");
                    }
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