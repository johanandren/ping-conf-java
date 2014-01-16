package controllers;

import static akka.pattern.Patterns.ask;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.util.Timeout;
import play.libs.Akka;
import play.libs.F.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import static akka.pattern.Patterns.ask;


import java.util.concurrent.TimeUnit;

public class CatAuctionController extends Controller {

    static ActorRef catnipAuction = Akka.system().actorOf(Props.create(CatAuction.class));

    public static Result placeCatnipBid(String catName, int amount) {
        catnipAuction.tell(new Bid(catName, amount), ActorRef.noSender());
        return ok("bid placed");
    }

    static final Timeout timeout = Timeout.apply(5, TimeUnit.SECONDS);

    public static Promise<Result> highestBidForCatnip() {
        Promise<Object> eventualBid = Promise.wrap(ask(catnipAuction, Messages.WHO_IS_WINNING, timeout));

        return eventualBid.map(new Function<Object, Result>() {
            @Override
            public Result apply(Object message) throws Throwable {
                Bid bid = (Bid) message;
                return ok(Json.toJson(bid));
            }
        });
    }

}