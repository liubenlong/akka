package com.lightbend.akka.receivetimeout;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class ReceiveTimeoutTestActor extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static public Props props() {
        return Props.create(ReceiveTimeoutTestActor.class, () -> new ReceiveTimeoutTestActor());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("hello", s -> {
                    getContext().setReceiveTimeout(Duration.create(5, TimeUnit.SECONDS));
                    log.info("msg={}, myPath={}", s, getSelf().path().toString());
                    getSender().tell("result", getSelf());
                })
                .match(ReceiveTimeout.class, r -> {
                    log.error("接收到了ReceiveTimeout消息");
                    getContext().setReceiveTimeout(Duration.Undefined());
                })
                .matchAny(o -> log.error("the msg:{}  is not support!", o))
                .build();
    }
}