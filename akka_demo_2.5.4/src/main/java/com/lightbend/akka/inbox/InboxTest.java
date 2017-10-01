package com.lightbend.akka.inbox;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by hzliubenlong on 2017/10/1.
 */
public class InboxTest extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Greeting.class, greeting -> {
                    log.info("hello " + greeting.message);
                    getSender().tell("hi", ActorRef.noSender());
                })
                .matchAny(o -> log.error("the msg:{}  is not support!", o))
                .build();
    }


    static public class Greeting {
        public final String message;

        public Greeting(String message) {
            this.message = message;
        }
    }

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("myInbox");
        ActorRef inboxTest = system.actorOf(Props.create(InboxTest.class), "MyInboxTest");
        Inbox inbox = Inbox.create(system);

        //监控inboxTest
//        inbox.watch(inboxTest);
//        杀死inboxTest
//        inboxTest.tell(PoisonPill.getInstance(), ActorRef.noSender());

        //通过inbox来发送消息
        inbox.send(inboxTest, new Greeting("tom"));

        try {
            Object receive = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            System.out.println(receive);
            System.out.println(receive instanceof Terminated);
        } catch (java.util.concurrent.TimeoutException e) {
            e.printStackTrace();
        }

        //中断ActorSystem
        system.terminate();
    }
}
