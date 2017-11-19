package com.lightbend.akka.route;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import akka.routing.RoundRobinPool;
import com.typesafe.config.ConfigFactory;

import java.util.concurrent.TimeUnit;

public class GroupRouteActor {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("routeAkka", ConfigFactory.load("dev.conf"));
        ActorRef actorRef =  system.actorOf(FromConfig.getInstance().props(), "myGroupRouter");
        for (int n = 1; n <= 3; n++) {
            system.actorOf(Props.create(Printer.class), "worker_" + n);
        }
        for (int i = 0; i < 7; i++) {
            actorRef.tell(new Printer.Greeting("msg" + i), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
}