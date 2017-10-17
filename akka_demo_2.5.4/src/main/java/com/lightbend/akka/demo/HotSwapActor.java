package com.lightbend.akka.demo;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * 给小孩玩具就笑，拿走玩具就哭
 */
public class HotSwapActor extends AbstractActor {
    private AbstractActor.Receive smileReceive;
    private AbstractActor.Receive cryReceive;

    static public Props props() {
        return Props.create(HotSwapActor.class, () -> new HotSwapActor());
    }

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    enum ToyStatus{
        HAVETOY, NOTOY;
    }

    public HotSwapActor() {
        smileReceive = receiveBuilder()
                .matchEquals(ToyStatus.HAVETOY, s -> log.info("我已经有玩具了，很开心"))
                .matchEquals(ToyStatus.NOTOY, s -> {
                    log.info("拿走了玩具。");
                    getContext().become(cryReceive);
                })
                .build();

        cryReceive = receiveBuilder()
                .matchEquals(ToyStatus.NOTOY, s -> log.info("没有玩具了，我要哭了"))
                .matchEquals(ToyStatus.HAVETOY, s -> {
                    log.info("拿到了玩具。");
                    getContext().become(smileReceive);
                })
                .build();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(ToyStatus.NOTOY, s -> {
                    log.info("拿走玩具。");
                    getContext().become(cryReceive);
                })
                .matchEquals(ToyStatus.HAVETOY, s -> {
                    log.info("给小孩玩具。");
                    getContext().become(receiveBuilder()
                            .matchEquals(ToyStatus.HAVETOY, o -> log.info("我已经有玩具了，很开心"))
                            .matchEquals(ToyStatus.NOTOY, ss -> {
                                log.info("拿走了玩具。");
                                getContext().unbecome();
                            })
                            .build());
                })
                .build();
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("hotSwapAkka");
        ActorRef hotSwapActor = system.actorOf(HotSwapActor.props(), "hotSwapActor");

        hotSwapActor.tell(ToyStatus.NOTOY, ActorRef.noSender());
        hotSwapActor.tell(ToyStatus.NOTOY, ActorRef.noSender());
        hotSwapActor.tell(ToyStatus.HAVETOY, ActorRef.noSender());
        hotSwapActor.tell(ToyStatus.HAVETOY, ActorRef.noSender());
    }
}