package com.lightbend.akka.route;

import akka.actor.*;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.lightbend.akka.mailbox.MyControlMessage;
import com.typesafe.config.ConfigFactory;
import javafx.concurrent.Worker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Master extends AbstractActor {

    Router router;

    {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ActorRef r = getContext().actorOf(Props.create(Worker.class));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Work.class, message -> router.route(message, getSender()))
                .match(Terminated.class, message -> {
                    router = router.removeRoutee(message.actor());
                    ActorRef r = getContext().actorOf(Props.create(Worker.class));
                    getContext().watch(r);
                    router = router.addRoutee(new ActorRefRoutee(r));
                })
                .build();
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("timerAkka", ConfigFactory.load("dev.conf"));
        ActorRef myActor = system.actorOf(Props.create(Master.class));
    }
}