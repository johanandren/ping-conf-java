package controllers;

import akka.actor.Actor;
import akka.actor.UntypedActor;

public class CatAuction extends UntypedActor {

    private Bid highestBid = new Bid("nobody", 0);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Bid && ((Bid) message).getAmount() > highestBid.getAmount()) {
            highestBid = (Bid) message;
        } else if (message == Messages.WHO_IS_WINNING) {
            getSender().tell(highestBid, getSelf());
        }
    }
}
