package com.lightbend.akka.mailbox;

import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedStablePriorityMailbox;
import com.typesafe.config.Config;

public class MyPrioMailbox extends UnboundedStablePriorityMailbox {
    // 需要反射实例化
    public MyPrioMailbox(ActorSystem.Settings settings, Config config) {
        //创建一个优先级生成器
        super(new PriorityGenerator() {
            @Override
            public int gen(Object message) {
                if (message.equals("highpriority"))
                    return 0; // 高优先级消息会优先处理
                else if (message.equals("lowpriority"))
                    return 2; // 低优先级消息最后处理
                else if (message.equals(PoisonPill.getInstance()))
                    return 3; // 没有新消息时终止该actor
                else
                    return 1; // 默认情况下消息优先级介于高和低之间
            }
        });
    }
}