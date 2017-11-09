package com.lightbend.akka.route;

import akka.actor.*;
import akka.routing.*;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PoolRouteActor  {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("routeAkka", ConfigFactory.load("dev.conf"));
        ActorRef actorRef = system.actorOf(new RoundRobinPool(3).props(Props.create(Printer.class)), "myRouterActor");
//        ActorRef actorRef = system.actorOf(FromConfig.getInstance().props(Props.create(Printer.class)), "myRouterActor");

        for (int i = 0; i < 7; i++) {
            actorRef.tell(new Printer.Greeting("msg" + i), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
}