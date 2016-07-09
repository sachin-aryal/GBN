package Service;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by anons on 7/6/16.
 */


public class Parser {
    public void getDataFromFile(String file){

        String projectDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
        String dataDirectory = projectDirectory+file;

        Path inPath = Paths.get(dataDirectory);
        Path outPath = Paths.get(projectDirectory+"/resources/sortedposnegword.txt");

        Map<String,Double> itemMap = new TreeMap();
        try (BufferedReader reader = Files.newBufferedReader(inPath)) {
            reader.lines()
                    .distinct()
                    .map(line->Arrays.asList(line.split(",")))
                    .collect(Collectors.toList())
                    .forEach(item->{
                        String[] eachItem =  item.toString().split(",");

                        String key = eachItem[4].split("#")[0].trim();
                        Double sentimentValue = (Double.parseDouble(eachItem[2].trim())-Double.parseDouble(eachItem[3].trim()));

                        if (itemMap.containsKey(key)){
                            Double oldSentimentValue = itemMap.get(key);
                            if (Math.abs(oldSentimentValue)<Math.abs(sentimentValue)){
                                itemMap.put(key, sentimentValue);
                            }
                        }else {
                            itemMap.put(key,sentimentValue);
                        }
                    });

            /*Set set = itemMap.entrySet();
            Iterator it = set.iterator();

            while(it.hasNext()) {
                Map.Entry me = (Map.Entry)it.next();
                System.out.println(me.getKey() + ","+me.getValue());
            }*/

            BufferedWriter writer = Files.newBufferedWriter(outPath);
            itemMap.forEach((key,value)->{
                try {
                    writer.append(key).append(',').append(value.toString()).append('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Parser pa = new Parser();
        pa.getDataFromFile("/resources/sentiword.txt");
    }
}
