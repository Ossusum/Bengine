import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Indexes the Url Links from a crawler/ URL array for ranking
 *
 * Created by Benny Pena on 12/10/2016.
 */
public class Indexer {

    private ArrayList<String> keywordUrls;
    private ArrayList<String> allwords;
    private HashMap<String,ArrayList<String>> hashmap;

    public Indexer() {
        this.keywordUrls = new ArrayList<>();
        this.allwords = new ArrayList<>();
        this.hashmap = new HashMap<>();
    }

    public void index(){
        readUrls();
        getAllWords();
        System.out.println(allwords.toString());
        insertToHash();
        System.out.println(hashmap.toString());
    }

    private void insertToHash() {
        System.out.println("Total Words To Index: " + allwords.size());
        for (int i = 0; i < allwords.size();++i){
            System.out.println("Word Indexing now: " + i);
            String keyword = allwords.get(i);
            Document doc = null;
            Connection connection = null;
            ArrayList<String> related = new ArrayList<>();
            for(int j = 0; j < keywordUrls.size();++j){
                connection = Jsoup.connect(keywordUrls.get(j));
                try {
                    doc = connection.get();

                } catch (Exception e) {
                    doc = null;
                }
                if (doc != null){
                    if(doc.text().contains(keyword)){
                        related.add(keywordUrls.get(j));
                    }
                }
            }
            hashmap.put(keyword,related);
            System.out.println("Finished indexing: " + i);
        }
    }

    private void getAllWords(){
        Document doc = null;
        Connection connection = null;
        for (int i = 0; i < keywordUrls.size(); ++i){
            connection = Jsoup.connect(keywordUrls.get(i));
            System.out.println("Indexing: "+keywordUrls.get(i));
            try {
                doc = connection.get();
                System.out.println("Got the Document!");
            } catch (Exception e) {
                doc = null;
            }
            if (doc != null){
                String[] documentWords = doc.text().split(" ");
                for (int j = 0; j < documentWords.length; ++j){
                    if (!allwords.contains(documentWords[j])){
                        allwords.add(documentWords[j]);
                    }
                }
            }
        }
    }


    private void readUrls(){
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
    }

    public ArrayList<String> getKeywordUrls() {
        return keywordUrls;
    }


}
