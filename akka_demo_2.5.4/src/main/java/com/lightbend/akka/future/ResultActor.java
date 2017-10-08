package com.lightbend.akka.future;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class ResultActor extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static public Props props() {
        return Props.create(ResultActor.class, () -> new ResultActor());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(List.class, this::receiveMsg)
                .matchAny(this::receiveErrorMsg)
                .build();
    }

    private void receiveMsg(List list) {
        log.info(JSONObject.toJSONString(list));
    }

    private void receiveErrorMsg(Object o) {
        log.error("the msg:{}  is not support!", o);
    }

}