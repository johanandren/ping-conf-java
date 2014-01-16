package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import tyrex.services.UUID;

public class KittenStreamController extends Controller {

    static ActorRef kittenStreamActor = Akka.system().actorOf(Props.create(KittenEventStreamActor.class));

    public static Result streamKittenEvents() {
        Chunks<String> chunks = new StringChunks() {

            public void onReady(Chunks.Out<String> out) {
                // register that output to someone that will push events
                // already formatted as strings, probably an actor
                final String id = UUID.create();
                kittenStreamActor.tell(new SendMeKittenEvents(id, out), ActorRef.noSender());

                out.onDisconnected(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        // unregister the output from the actor
                        kittenStreamActor.tell(new StopListeningToKittenEvents(id), ActorRef.noSender());
                    }
                });
            }
        };

        return ok(chunks).as("text/event-stream");
    }
}
