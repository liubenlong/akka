package com.lightbend.akka.生命周期;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.alibaba.fastjson.JSONObject;
import com.lightbend.akka.sample.Printer;

public class MyWatchActor extends AbstractActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    static public Props props() {
        return Props.create(MyWatchActor.class, () -> new MyWatchActor());
    }

    public MyWatchActor() {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DeadLetter.class, d -> {
                    log.info(JSONObject.toJSONString(d.message()));
                    log.info(d.toString());
                })
                .matchAny(o -> log.error("the msg:{}  is not support!", o))
                .build();
    }
}