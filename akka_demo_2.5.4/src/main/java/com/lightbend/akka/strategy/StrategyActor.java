package com.lightbend.akka.strategy;

import akka.actor.AbstractActor;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class StrategyActor extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(10, Duration.create(1, TimeUnit.MINUTES),
                DeciderBuilder
                        .match(ArithmeticException.class, e -> SupervisorStrategy.resume())
                        .match(NullPointerException.class, e -> SupervisorStrategy.restart())
                        .match(IllegalArgumentException.class, e -> SupervisorStrategy.stop())
                        .matchAny(o -> SupervisorStrategy.escalate())
                        .build());
    }

    static public Props props() {
        return Props.create(StrategyActor.class, () -> new StrategyActor());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("createChild", s -> getContext().actorOf(ChildActor.props(), "childActor"))
                .matchAny(o -> log.error("the msg:{}  is not support!", o))
                .build();
    }
}