package com.lightbend.akka.strategy;

import akka.actor.AbstractActor;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ChildActor extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public ChildActor() {
        log.info("我是构造函数。,hashCode={}", this.hashCode());
    }

    @Override
    public void preStart() throws Exception {
        log.info("preStart() executed,hashCode={}", this.hashCode());
        super.preStart();
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        log.info("preRestart() executed,hashCode={}", this.hashCode());
        super.preRestart(reason, message);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        log.info("postRestart() executed,reason={},hashCode={}", reason.getMessage(), this.hashCode());
        super.postRestart(reason);
    }

    @Override
    public void postStop() throws Exception {
        log.info("postStop() executed,hashCode={}", this.hashCode());
        super.postStop();
    }

    static public Props props() {
        return Props.create(ChildActor.class, () -> new ChildActor());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("ArithmeticException", s -> {
                    System.out.println(1 / 0);
                })
                .matchEquals("NullPointerException", s -> {
                    throw new NullPointerException("i am NullPointerException");
                })
                .matchEquals("IllegalArgumentException", s -> {
                    throw new IllegalAccessException("i am IllegalArgumentException");
                })
                .matchEquals("ok", s -> log.info(s))
                .matchAny(o -> log.error("the msg:{}  is not support!", o))
                .build();
    }
}