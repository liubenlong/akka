package com.lightbend.akka.demo;

import akka.actor.AbstractActorWithStash;

/**
 * stash
 */
public class ActorWithProtocol extends AbstractActorWithStash {

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .matchEquals("open", s -> {
                    getContext().become(receiveBuilder()
                            .matchEquals("write", ws -> { /* do writing */ })
                            .matchEquals("close", cs -> {
                                unstashAll();
                                getContext().unbecome();
                            })
                            .matchAny(msg -> stash())
                            .build(), false);
                })
                .matchAny(msg -> stash())
                .build();
    }
}