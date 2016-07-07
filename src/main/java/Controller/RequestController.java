package Controller;

import Service.Crawler;
import Service.DataDictionary;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.List;

/**
 * Created by iam on 7/6/16.
 */
public class RequestController extends AbstractVerticle{

    public static void main(String[] args) {

        DataDictionary.loadDictionary();
        if (DataDictionary.wordDictionary.size()==0){
            System.out.println("Please View the Error Log and Restart Again");
            System.exit(0);
        }
        System.out.println("Deploying Verticle");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RequestController());

    }

    @Override
    public void start() throws Exception {
        System.out.println("Verticle Deployed Successfully.");

        Router router = Router.router(vertx);
        vertx.createHttpServer().requestHandler(router::accept).listen(9000);

        router.route("/").handler(rtx->{
           rtx.response().setChunked(true);
           rtx.response().sendFile("webroot/index.html");
        });

        router.route("/Js/*").handler(StaticHandler.create("webroot/Js"));
        router.route("/Css/*").handler(StaticHandler.create("webroot/Css"));
        router.route("/images/*").handler(StaticHandler.create("webroot/images"));


        router.route("/fetchNews").handler(rtx->{
           vertx.executeBlocking(objectFuture -> {

               int numberOfNews = Integer.parseInt(rtx.request().getParam("noOfNews"));
               String newsType = rtx.request().getParam("newsType");

           },res->{

           });
        });


    }
}
