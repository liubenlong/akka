package com.lightbend.akka.route;

import akka.actor.*;
import akka.routing.*;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PoolRouteActor  {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("routeAkka", ConfigFactory.load("dev.conf"));
        //方式一：通过代码实现
//        ActorRef actorRef = system.actorOf(new RoundRobinPool(3).props(Props.create(Printer.class)), "myRouterActor");
        //方式二：通过配置文件实现。注意这里后面的name就是配置文件中配置的名称
        ActorRef actorRef = system.actorOf(FromConfig.getInstance().props(Props.create(Printer.class)), "myRouterActor");

        for (int i = 0; i < 7; i++) {
            actorRef.tell(new Printer.Greeting("msg" + i), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
}