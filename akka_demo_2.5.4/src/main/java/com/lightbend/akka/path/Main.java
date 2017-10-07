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
        ActorRef follower = system.actorOf(Follower.props(), "follower");

        follower.tell(new Printer.Greeting("hi tom "), ActorRef.noSender());


        TimeUnit.SECONDS.sleep(1);
        system.terminate();
    }
}