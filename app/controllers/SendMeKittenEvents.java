package controllers;

import play.mvc.Results;

public class SendMeKittenEvents {

    public final String id;
    public final Results.Chunks.Out<String> output;

    public SendMeKittenEvents(String id, Results.Chunks.Out<String> output) {
        this.id = id;
        this.output = output;
    }
}
