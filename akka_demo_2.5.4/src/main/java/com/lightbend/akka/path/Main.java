package com.lightbend.akka.path;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.lightbend.akka.sample.Printer;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = ActorSystem.create("akkaPath");
        ActorRef printerActor11 = system.actorOf(Printer.props(), "printerActor11");
        ActorRef printerActor22 = system.actorOf(Printer.props(), "printerActor22");
        ActorRef factory = system.actorOf(Factory.props(), "factory");

        factory.tell(new Factory.FactoryMsg("hi tom "), ActorRef.noSender());


        ActorSelection actorSelection = system.actorSelection("akka://akkaPath/user/printerActor*");
        actorSelection.tell(new Printer.Greeting("haha"), ActorRef.noSender());

        factory.tell(new Factory.FactoryMsg("find"), ActorRef.noSender());


//        TimeUnit.SECONDS.sleep(1);
//        system.terminate();
    }
}