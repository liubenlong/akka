package com.lightbend.akka.route;

import akka.actor.*;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Master extends AbstractActor {

    Router router;

    {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ActorRef r = getContext().actorOf(Props.create(Printer.class), "print_" + i);
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Printer.Greeting.class, message -> router.route(message, getSender()))
                .match(Terminated.class, message -> {
                    router = router.removeRoutee(message.actor());
                    System.out.println(message.actor().path() + " dead. size=" + router.routees().size());

                    ActorRef r = getContext().actorOf(Props.create(Printer.class));
                    getContext().watch(r);
                    router = router.addRoutee(new ActorRefRoutee(r));
                })
                .build();
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("routeAkka");
        ActorRef myActor = system.actorOf(Props.create(Master.class));

        for (int i = 0; i < 10; i++) {
            myActor.tell(new Printer.Greeting("a" + i), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
}