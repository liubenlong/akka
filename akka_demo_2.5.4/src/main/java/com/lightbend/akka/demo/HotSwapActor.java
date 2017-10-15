package com.lightbend.akka.demo;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class HotSwapActor extends AbstractActor {
    private AbstractActor.Receive angry;
    private AbstractActor.Receive happy;

    static public Props props() {
        return Props.create(HotSwapActor.class, () -> new HotSwapActor());
    }

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public HotSwapActor() {
        angry = receiveBuilder()
                .matchEquals("foo", s -> log.info("I am already angry?.param={}", s))
                .matchEquals("bar", s -> getContext().become(happy))
                .build();

        happy = receiveBuilder()
                .matchEquals("bar", s -> log.info("I am already happy :-).param={}", s))
                .matchEquals("foo", s -> getContext().become(angry))
                .build();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("foo", s -> {
                    log.info("收到foo消息，替换为angry。");
                    getContext().become(angry);
                })
                .matchEquals("bar", s -> {
                    log.info("收到bar消息，替换为happy。");
                    getContext().become(happy);
                })
                .build();
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("hotSwapAkka");
        ActorRef hotSwapActor = system.actorOf(HotSwapActor.props(), "hotSwapActor");

        hotSwapActor.tell("foo", ActorRef.noSender());
        hotSwapActor.tell("foo", ActorRef.noSender());
        hotSwapActor.tell("bar", ActorRef.noSender());
        hotSwapActor.tell("bar", ActorRef.noSender());
    }
}