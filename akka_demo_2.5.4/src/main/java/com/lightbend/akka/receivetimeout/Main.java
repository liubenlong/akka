package com.lightbend.akka.receivetimeout;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ActorSystem system = ActorSystem.create("futureTest");
        ActorRef receiveTimeoutTestActor = system.actorOf(ReceiveTimeoutTestActor.props(), "receiveTimeoutTestActor");

        CompletableFuture<Object> future1 = ask(receiveTimeoutTestActor, "hello", 1000)
                .toCompletableFuture()
                .exceptionally(throwable -> {
                    System.out.println(throwable.getMessage());
                    return null;
                });

        Object o = future1.get();
        System.out.println(o);

        TimeUnit.SECONDS.sleep(3);

        future1 = ask(receiveTimeoutTestActor, "hello", 1000)
                .toCompletableFuture()
                .exceptionally(throwable -> {
                    System.out.println(throwable.getMessage());
                    return null;
                });

        o = future1.get();
        System.out.println(o);
    }
}