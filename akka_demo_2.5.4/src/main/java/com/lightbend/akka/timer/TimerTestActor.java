package com.lightbend.akka.timer;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.AskTimeoutException;
import akka.pattern.Patterns;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class TimerTestActor extends AbstractActorWithTimers {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private static Object TICK_KEY = "TickKey";

    private static final class FirstTick {
    }

    private static final class Tick {
    }

    private static final class CancelTick {
    }

    static public Props props() {
        return Props.create(TimerTestActor.class, () -> new TimerTestActor());
    }

    public TimerTestActor() {
        log.info("我是构造函数");
        //一次性的任务
        getTimers().startSingleTimer(TICK_KEY, new FirstTick(),
                Duration.create(500, TimeUnit.MILLISECONDS));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FirstTick.class, message -> {
                    log.info("收到了FirstTick类型的消息");
                    //周期性任务
                    getTimers().startPeriodicTimer(TICK_KEY, new Tick(),
                            Duration.create(1, TimeUnit.SECONDS));
                })
                .match(Tick.class, message -> log.info("收到了Tick类型的消息"))
                .match(CancelTick.class, message -> {
                    log.info("收到了CancelTick类型的消息");
                    getTimers().cancel(TICK_KEY);
                })
                .matchAny(o -> log.error("the msg:{}  is not support!", o))
                .build();
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("timerAkka");
        ActorRef timerTestActor = system.actorOf(TimerTestActor.props(), "timerTestActor");

        TimeUnit.SECONDS.sleep(3);

//        取消定时器
        timerTestActor.tell(new CancelTick(), ActorRef.noSender());

//        /**
//         * 通过Patterns.gracefulStop，停止掉timerTestActor
//         */
//        try {
//            Future<Boolean> future = Patterns.gracefulStop(timerTestActor, Duration.create(5, TimeUnit.SECONDS));
//            future.onComplete(new OnComplete<Boolean>() {
//                @Override
//                public void onComplete(Throwable throwable, Boolean aBoolean) throws Throwable {
//                    System.out.println(aBoolean);
//                    if (throwable != null) throwable.printStackTrace();
//                }
//            }, system.dispatcher());
//
////            Await.result(future, Duration.create(6, TimeUnit.SECONDS));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}