package com.lightbend.akka.route;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Dispatchers;
import akka.routing.GroupBase;
import akka.routing.Router;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;

public class RedundancyGroup extends GroupBase {
    private final List<String> paths;
    private final int nbrCopies;

    public RedundancyGroup(List<String> paths, int nbrCopies) {
        this.paths = paths;
        this.nbrCopies = nbrCopies;
    }

    public RedundancyGroup(Config config) {
        this(config.getStringList("routees.paths"), config.getInt("nbr-copies"));
    }

    @Override
    public java.lang.Iterable<String> getPaths(ActorSystem system) {
        return paths;
    }

    @Override
    public Router createRouter(ActorSystem system) {
        return new Router(new RedundancyRoutingLogic(nbrCopies));
    }

    @Override
    public String routerDispatcher() {
        return Dispatchers.DefaultDispatcherId();
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("routeAkka", ConfigFactory.load("dev.conf"));

        for (int n = 1; n <= 10; n++) {
            system.actorOf(Props.create(Printer.class), "s" + n);
        }

        List<String> paths = new ArrayList<>();
        for (int n = 1; n <= 10; n++) {
            paths.add("/user/s" + n);
        }

        ActorRef redundancy1 = system.actorOf(new RedundancyGroup(paths, 3).props(), "redundancy1");
        redundancy1.tell(new Printer.Greeting("important"), ActorRef.noSender());
    }
}