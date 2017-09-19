package strategy;

import akka.Msg;
import akka.actor.*;
import com.typesafe.config.ConfigFactory;

/**
 * Created by liubenlong on 2017/1/12.
 */
public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef superVisor = system.actorOf(Props.create(SuperVisor.class), "SuperVisor");
        superVisor.tell(Props.create(RestartActor.class), ActorRef.noSender());

        ActorSelection actorSelection = system.actorSelection("akka://strategy/user/SuperVisor/restartActor");


        for(int i = 0 ; i < 100 ; i ++){
            actorSelection.tell(Msg.RESTART, ActorRef.noSender());
        }
    }

}
