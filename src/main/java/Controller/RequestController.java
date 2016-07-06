package Controller;

import Service.DataDictionary;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Created by iam on 7/6/16.
 */
public class RequestController extends AbstractVerticle{

    public static void main(String[] args) {

        DataDictionary.loadDictionary();
        System.out.println("Loading Dictionary Completed and Not Deploying New Verticle");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RequestController());

    }

    @Override
    public void start() throws Exception {

    }
}
