package com.lightbend.akka.path;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.PoisonPill;
import com.lightbend.akka.sample.Printer;
import com.lightbend.akka.生命周期.MyWatchActor;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = ActorSystem.create("akkaPath");
        ActorRef printerActor1 = system.actorOf(Printer1.props(), "printerActor1");
        ActorRef printerActor2 = system.actorOf(Printer2.props(), "printerActor2");

        printerActor1.tell(new Printer.Greeting("hi tom "), ActorRef.noSender());


        TimeUnit.SECONDS.sleep(1);
        system.terminate();
    }
}