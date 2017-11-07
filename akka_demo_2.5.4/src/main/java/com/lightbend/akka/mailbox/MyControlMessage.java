package com.lightbend.akka.mailbox;

import akka.dispatch.ControlMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MyControlMessage implements ControlMessage {
    private String id;
}
