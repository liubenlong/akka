package com.lightbend.akka.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import static akka.pattern.Patterns.ask;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class AkkaQuickstartTest {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testGreeterActorSendingOfGreeting() throws Exception {
        final TestKit testProbe = new TestKit(system);
        final ActorRef helloGreeter = system.actorOf(Greeter.props("Hello", testProbe.getRef()));
        helloGreeter.tell(new Greeter.WhoToGreet("Akka"), ActorRef.noSender());

//        helloGreeter.tell(new Greeter.Greet(), ActorRef.noSender());
//        Printer.Greeting greeting = testProbe.expectMsgClass(Printer.Greeting.class);
//        assertEquals("Hello, Akka", greeting.message);

        assert Await.result(ask(helloGreeter, new Greeter.Greet(), 5000), Duration.create(5, SECONDS)).equals("Hello, Akka");



    }
}
