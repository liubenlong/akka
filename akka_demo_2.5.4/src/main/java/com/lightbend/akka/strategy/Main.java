package com.lightbend.akka.strategy;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("strategyAkka");
        ActorRef strategyActor = system.actorOf(StrategyActor.props(), "strategyActor");
        strategyActor.tell("createChild", ActorRef.noSender());//创建了子child

        ActorSelection childActor = system.actorSelection("akka://strategyAkka/user/strategyActor/childActor");
        childActor.tell("ok", ActorRef.noSender());
        TimeUnit.SECONDS.sleep(1);
        childActor.tell("ArithmeticException", ActorRef.noSender());
        childActor.tell("ok", ActorRef.noSender());
        TimeUnit.SECONDS.sleep(1);
        childActor.tell("NullPointerException", ActorRef.noSender());
        childActor.tell("ok", ActorRef.noSender());
        TimeUnit.SECONDS.sleep(1);
        childActor.tell("IllegalArgumentException", ActorRef.noSender());
        childActor.tell("ok", ActorRef.noSender());
        childActor.tell("ok", ActorRef.noSender());

    }
}