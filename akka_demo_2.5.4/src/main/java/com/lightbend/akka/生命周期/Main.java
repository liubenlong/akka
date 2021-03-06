package com.lightbend.akka.生命周期;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.PoisonPill;
import akka.event.DeadLetterListener;
import com.lightbend.akka.sample.Printer;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
//        test1();
        testReStart();
    }

    static void testReStart() throws InterruptedException {
        ActorSystem system = ActorSystem.create("helloakka");
        ActorRef printerActor = system.actorOf(Printer.props(), "printerActor");

        printerActor.tell(new Printer.Greeting("hi tom "), ActorRef.noSender());
        printerActor.tell(new Printer.Greeting("restart"), ActorRef.noSender());
        printerActor.tell(new Printer.Greeting("restartsss"), ActorRef.noSender());


    }

    static void test1() throws InterruptedException {
        final ActorSystem system = ActorSystem.create("helloakka");
        ActorRef printerActor = system.actorOf(Printer.props(), "printerActor");

        //这两句用于从事件总线中监控DeadLetter消息
        ActorRef myWatchActor = system.actorOf(MyWatchActor.props(), "myWatchActor");
        system.eventStream().subscribe(myWatchActor, DeadLetter.class);


        printerActor.tell(new Printer.Greeting("hi tom "), ActorRef.noSender());
//        终止printerActor
        printerActor.tell(PoisonPill.getInstance(), ActorRef.noSender());

//        TimeUnit.SECONDS.sleep(1);
//        system.stop(printerActor);

        //向一个已经stop的actor发送消息
        printerActor.tell(new Printer.Greeting("hello "), ActorRef.noSender());


        TimeUnit.SECONDS.sleep(1);
        system.terminate();
    }
}