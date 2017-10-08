package com.lightbend.akka.future;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class UnTypeResultActor extends UntypedAbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static public Props props() {
        return Props.create(UnTypeResultActor.class, () -> new UnTypeResultActor());
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof List) {
            receiveMsg((List) o);
        } else {
            unhandled(o);
        }
    }

    private void receiveMsg(List list) {
        log.info(JSONObject.toJSONString(list));
    }
}