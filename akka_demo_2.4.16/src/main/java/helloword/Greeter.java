package helloword;

import akka.actor.UntypedActor;

public class Greeter extends UntypedActor {

    @Override
    public void onReceive(Object msg) throws InterruptedException {
        if (msg == Msg.GREET) {
            System.out.println("Hello World!");
            Thread.sleep(1000);
            getSender().tell(Msg.DONE, getSelf());//给发送至发送信息.
        } else
            unhandled(msg);
    }
}