package Controller;

import Service.DataDictionary;
import Service.InitiateOperation;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Created by iam on 7/6/16.
 */
public class RequestController extends AbstractVerticle{

    @Override
    public void start() throws Exception {
        System.out.println("Verticle Deployed Successfully.");

        Router router = Router.router(vertx);
        vertx.createHttpServer().requestHandler(router::accept).listen(9000);
        System.out.println(" .----------------.  .----------------.  .-----------------.\n" +
                "| .--------------. || .--------------. || .--------------. |\n" +
                "| |    ______    | || |   ______     | || | ____  _____  | |\n" +
                "| |  .' ___  |   | || |  |_   _ \\    | || ||_   \\|_   _| | |\n" +
                "| | / .'   \\_|   | || |    | |_) |   | || |  |   \\ | |   | |\n" +
                "| | | |    ____  | || |    |  __'.   | || |  | |\\ \\| |   | |\n" +
                "| | \\ `.___]  _| | || |   _| |__) |  | || | _| |_\\   |_  | |\n" +
                "| |  `._____.'   | || |  |_______/   | || ||_____|\\____| | |\n" +
                "| |              | || |              | || |              | |\n" +
                "| '--------------' || '--------------' || '--------------' |\n" +
                " '----------------'  '----------------'  '----------------' \n");
        System.out.println("Server running on: http://localhost:9000/");
        router.route("/").handler(rtx->{
           rtx.response().setChunked(true);
           rtx.response().sendFile("webroot/index.html");
        });

        router.route("/Js/*").handler(StaticHandler.create("webroot/Js"));
        router.route("/Css/*").handler(StaticHandler.create("webroot/Css"));
        router.route("/images/*").handler(StaticHandler.create("webroot/images"));


        router.route("/fetchNews/:noOfNews/:newsType/:newsSource/*").handler(rtx->{
           vertx.executeBlocking(objectFuture -> {
               String newsSource = rtx.request().getParam("newsSource");
               int numberOfNews = Integer.parseInt(rtx.request().getParam("noOfNews"));
               String newsType = rtx.request().getParam("newsType");
               InitiateOperation initiateOperation = new InitiateOperation(numberOfNews,newsType,newsSource);
               objectFuture.complete(initiateOperation.getNewsData());

           },res->{
               rtx.response().setChunked(true);
               rtx.response().putHeader("content-type", "application/json; charset=utf-8");
               rtx.response().write(Json.encodePrettily(res.result()));
               System.out.println("Response Written Successfully. Now Closing Respone");
               rtx.response().end();
           });
        });


    }
}
