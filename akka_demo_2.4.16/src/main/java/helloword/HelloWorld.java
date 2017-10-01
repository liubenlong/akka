package helloword;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.ActorRef;

public class HelloWorld extends UntypedActor {

  ActorRef greeter;

  @Override
  public void preStart() {
    // create the greeter actor
    greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
    System.out.println("Greeter actor path：" + greeter.path());
    // tell it to perform the greeting
    greeter.tell(Msg.GREET, getSelf());
  }

  @Override
  public void onReceive(Object msg) {
    if (msg == Msg.DONE) {
      // when the greeter is done, stop this actor and with it the application
      greeter.tell(Msg.GREET, getSelf());
      getContext().stop(getSelf());
      System.out.println("HelloWorld 已停止！");
    } else
      unhandled(msg);
  }
}