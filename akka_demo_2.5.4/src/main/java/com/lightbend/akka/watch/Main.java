package com.lightbend.akka.watch;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import com.lightbend.akka.sample.Printer;

public class Main {

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("helloakka");
        final ActorRef printerActor = system.actorOf(Printer.props(), "printerActor");
        //创建监控printerActor的MyWatchActor
        system.actorOf(MyWatchActor.props(printerActor), "myWatchActor");

        printerActor.tell(new Printer.Greeting("hhh"), ActorRef.noSender());
        //终止printerActor
        printerActor.tell(PoisonPill.getInstance(), ActorRef.noSender());

    }
}