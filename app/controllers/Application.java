package controllers;

import play.*;
import play.libs.F;
import play.mvc.*;

import views.html.*;
import play.libs.WS;
import play.mvc.Result;

import static play.libs.F.Function;
import static play.libs.F.Promise;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.nio.CharBuffer;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static F.Promise<Result> kittenProfile(Long kittenId) {
        return WS.url("http://kittenapi.com/kitten/" + kittenId).get()
                // if i had a web response I would
                .map(new Function<WS.Response, Result>() {
                    @Override
                    public Result apply(WS.Response response) {
                        if (response.getStatus() == OK) {
                            Kitten kitten = readKitten(response.asJson());
                            return ok(views.html.kittenDetails.render(kitten));
                        } else {
                            throw new RuntimeException("Kitten server error");
                        }
                    }
                });
    }

    public static Kitten readKitten(JsonNode json) {
        return new Kitten(json.get("name").asText());
    }

}

