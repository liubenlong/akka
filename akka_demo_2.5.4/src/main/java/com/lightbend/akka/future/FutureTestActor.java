package com.lightbend.akka.future;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FutureTestActor extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    static public Props props() {
        return Props.create(FutureTestActor.class, () -> new FutureTestActor());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Greeting.class, greeting -> {
                    try {
                        log.info("hi {}, my hashCode={}", greeting.message, this.hashCode());
                        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500));//随机休息500毫秒，模拟程序运行耗时
                        getSender().tell("hi " + greeting.message, getSelf());
                    }catch (Exception e){
                        getSender().tell(new akka.actor.Status.Failure(e), getSelf());
                        throw e;
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