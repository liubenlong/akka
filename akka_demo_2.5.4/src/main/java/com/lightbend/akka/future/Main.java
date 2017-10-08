package com.lightbend.akka.future;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("futureTest");
        ActorRef futureTestActorA = system.actorOf(FutureTestActor.props(), "futureTestActorA");
        ActorRef futureTestActorB = system.actorOf(FutureTestActor.props(), "futureTestActorB");
        ActorRef resultActor = system.actorOf(ResultActor.props(), "resultActor");

        Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));
        CompletableFuture<Object> future1 = ask(futureTestActorA, new FutureTestActor.Greeting("tom"), 1000)
                .toCompletableFuture()
                .exceptionally(throwable -> {
                    System.out.println(throwable.getMessage());
                    return null;
                });
        CompletableFuture<Object> future2 = ask(futureTestActorB, new FutureTestActor.Greeting("luffi"), t)
                .toCompletableFuture()
                .exceptionally(throwable -> {
                    System.out.println(throwable.getMessage());
                    return null;
                });

        CompletableFuture<List<String>> transformed =
                CompletableFuture.allOf(future1, future2)
                        .thenApply(v -> {
                            String x = (String) future1.join();
                            String s = (String) future2.join();
                            return Arrays.asList(x, s);
                        });

        pipe(transformed, system.dispatcher()).to(resultActor);
    }
}