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
    }
}
