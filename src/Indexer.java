import java.io.*;
import java.util.ArrayList;

/**
 * Indexes the Url Links from a crawler/ URL array for ranking
 *
 * Created by Benny Pena on 12/10/2016.
 */
public class Indexer {
    private Crawler crawler;
    private ArrayList<String> keywordUrls;

    public Indexer(Crawler crawler) {
        this.crawler = crawler;
        keywordUrls = new ArrayList<>();

    }
    //will index the array of URLs indexed
    public void index() {

    }
    private ArrayList<String> readUrls(){
        File readFile = new File("CrawlerURLS.txt");
        try {
            FileReader fReader = new FileReader(readFile);
            BufferedReader bReader = new BufferedReader(fReader);
            String line = "";
            while((line = bReader.readLine()) != null ){
                keywordUrls.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getKeywordUrls() {
        return keywordUrls;
    }


}
