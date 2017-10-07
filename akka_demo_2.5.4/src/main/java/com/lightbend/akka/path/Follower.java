package com.lightbend.akka.path;

import akka.actor.*;

public class Follower extends AbstractActor {
  final Integer identifyId = 1;

  static public Props props() {
    return Props.create(Follower.class, () -> new Follower());
  }

  public Follower(){
    ActorSelection selection = getContext().actorSelection("/user/another");
    selection.tell(new Identify(identifyId), getSelf());
  }
  
  @Override
  public Receive createReceive() {
    return receiveBuilder()
      .match(ActorIdentity.class, id -> id.getActorRef().isPresent(), id -> {
        ActorRef ref = id.getActorRef().get();
        getContext().watch(ref);
        getContext().become(active(ref));
      })
      .match(ActorIdentity.class, id -> !id.getActorRef().isPresent(), id -> {
        getContext().stop(getSelf());
      })
      .build();
  }
  
  final AbstractActor.Receive active(final ActorRef another) {
    return receiveBuilder()
      .match(Terminated.class, t -> t.actor().equals(another), t ->
        getContext().stop(getSelf())
      )
      .build();
  }
}