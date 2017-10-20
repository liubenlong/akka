package com.lightbend.akka.strategy;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.event.Logging;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;

/**
 * Created by hzliubenlong on 2017/10/20.
 */
public class MyOneForOneStrategy extends OneForOneStrategy {
    public MyOneForOneStrategy(int maxNrOfRetries, Duration withinTimeRange, PartialFunction<Throwable, Directive> decider) {
        super(maxNrOfRetries, withinTimeRange, decider);
    }
    @Override
    public void logFailure(ActorContext context, ActorRef child, Throwable cause, Directive decision) {
        String errorMsg = child.path() + "  自定义log ";
        context.system().eventStream().publish(new Logging.Error(cause, child.path().toString(), this.getClass(), errorMsg));
    }
}
