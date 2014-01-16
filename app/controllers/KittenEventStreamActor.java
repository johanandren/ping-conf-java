package controllers;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Results;
import scala.concurrent.duration.FiniteDuration;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Accepts the {@link controllers.SendMeKittenEvents}, {@link StopListeningToKittenEvents} and
 * {@link controllers.KittenEvent}
 *
 * Will send any kitten event to all currently registered listeners
 */
public class KittenEventStreamActor extends UntypedActor {

    private final Map<String, Results.Chunks.Out<String>> listeners = new HashMap<>();

    public KittenEventStreamActor() {
        getContext().system().scheduler().schedule(
            new FiniteDuration(1, TimeUnit.SECONDS),
            new FiniteDuration(1, TimeUnit.SECONDS),
            self(),
            new KittenEvent("sound", "mjau"),
            getContext().system().dispatcher(),
            ActorRef.noSender()
        );
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SendMeKittenEvents) {
            SendMeKittenEvents listener = (SendMeKittenEvents) message;
            listeners.put(listener.id, listener.output);

        } else if (message instanceof StopListeningToKittenEvents) {
            listeners.remove(((StopListeningToKittenEvents) message).id);

        } else if (message instanceof KittenEvent) {
            KittenEvent event = (KittenEvent) message;
            ObjectNode json = Json.newObject();
            json.put("type", event.getType());
            json.put("text", event.getText());

            // transform to text and wrap in EventSource protocol
            String eventAsString = "data: " + json.toString() + "\n\n";
            for (Results.Chunks.Out<String> listener: listeners.values()) {
                listener.write(eventAsString);
            }
        }
    }
}
