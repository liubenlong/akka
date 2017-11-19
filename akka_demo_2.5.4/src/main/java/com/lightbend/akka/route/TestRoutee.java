package com.lightbend.akka.route;

import akka.actor.ActorRef;
import akka.japi.Util;
import akka.routing.Routee;
import akka.routing.SeveralRoutees;
import org.junit.Assert;
import scala.collection.immutable.IndexedSeq;

import java.util.ArrayList;
import java.util.List;

public final class TestRoutee implements Routee {
    public final int n;

    public TestRoutee(int n) {
        this.n = n;
    }

    @Override
    public void send(Object message, ActorRef sender) {
    }

    @Override
    public int hashCode() {
        return n;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TestRoutee) && n == ((TestRoutee) obj).n;
    }

    public static void main(String[] args) throws InterruptedException {
        RedundancyRoutingLogic logic = new RedundancyRoutingLogic(3);

        List<Routee> routeeList = new ArrayList<>();
        for (int n = 1; n <= 4; n++) {
            routeeList.add(new TestRoutee(n));
        }
        IndexedSeq<Routee> routees = Util.immutableIndexedSeq(routeeList);

        SeveralRoutees r1 = (SeveralRoutees) logic.select("msg1", routees);
        Assert.assertEquals(r1.getRoutees().get(0), routeeList.get(0));
        Assert.assertEquals(r1.getRoutees().get(1), routeeList.get(1));
        Assert.assertEquals(r1.getRoutees().get(2), routeeList.get(2));

        SeveralRoutees r2 = (SeveralRoutees) logic.select("msg2", routees);
        Assert.assertEquals(r2.getRoutees().get(0), routeeList.get(3));
        Assert.assertEquals(r2.getRoutees().get(1), routeeList.get(0));
        Assert.assertEquals(r2.getRoutees().get(2), routeeList.get(1));
    }
}