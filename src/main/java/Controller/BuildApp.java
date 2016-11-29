package Controller;

import Service.DataDictionary;
import io.vertx.core.Vertx;

/**
 * Created by iam on 11/29/16.
 */
public class BuildApp {
    public static void main(String[] args) {

        DataDictionary.loadDictionary();
        DataDictionary.loadNegation();
        DataDictionary.loadStopWords();
        if (DataDictionary.wordDictionary.size()==0&&DataDictionary.negationWord.size()==0&&DataDictionary.wordsTobeIgnored.size()==0){
            System.out.println("Please View the Error Log and Restart Again");
            System.exit(0);
        }
        System.out.println("Deploying Verticle");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RequestController());

    }
}

