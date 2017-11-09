package com.lightbend.akka.route;

import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.RoutingLogic;
import akka.routing.SeveralRoutees;
import scala.collection.immutable.IndexedSeq;

import java.util.ArrayList;
import java.util.List;

public class RedundancyRoutingLogic implements RoutingLogic {
    private final int nbrCopies;

    public RedundancyRoutingLogic(int nbrCopies) {
        this.nbrCopies = nbrCopies;
    }

    RoundRobinRoutingLogic roundRobin = new RoundRobinRoutingLogic();

    @Override
    public Routee select(Object message, IndexedSeq<Routee> routees) {
        List<Routee> targets = new ArrayList<>();
        for (int i = 0; i < nbrCopies; i++) {
            System.out.println(message+"  "+i);
            targets.add(roundRobin.select(message, routees));
        }
        return new SeveralRoutees(targets);
    }

}