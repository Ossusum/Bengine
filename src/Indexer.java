import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Indexes the Url Links from a crawler/ URL array for ranking
 * <p>
 * Created by Benny Pena on 12/10/2016.
 */
public class Indexer {

    private ArrayList<String> keywordUrls;
    private HashMap<String, ArrayList<String>> hashmap;

    public Indexer() {
        this.keywordUrls = new ArrayList<>();
        this.hashmap = new HashMap<>();
    }

    public void index() {
        readUrls();
        getAllWords();
        writeToFile();
        System.out.print("DONE!!");
    }


    private void getAllWords() {
        Document doc;
        Connection connection;
        for (int i = 0; i < keywordUrls.size(); ++i) {
            connection = Jsoup.connect(keywordUrls.get(i));
            System.out.println("Indexing: " + keywordUrls.get(i));
            try {
                doc = connection.get();
            } catch (Exception e) {
                doc = null;
            }
            if (doc != null) {
                String[] documentWords = doc.text().split(" ");
                for (int j = 0; j < documentWords.length; ++j) {
                    if (!hashmap.containsKey(documentWords[j])) {
                        hashmap.put(documentWords[j], new ArrayList<String>());
                    }
                    ArrayList<String> urls = hashmap.get(documentWords[j]);
                    urls.add(keywordUrls.get(i));


                }
            }
        }
    }


    private void readUrls() {
        File readFile = new File("CrawlerURLS.txt");
        try {
            FileReader fReader = new FileReader(readFile);
            BufferedReader bReader = new BufferedReader(fReader);
            String line = "";
            while ((line = bReader.readLine()) != null) {
                keywordUrls.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile() {
        File hashTable = new File("Hash.txt");
        try {
            FileWriter fWriter = new FileWriter(hashTable);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            for (int i = 0; i < hashmap.size(); ++i) {
                bWriter.write(hashmap.entrySet().toArray()[i].toString());
                bWriter.newLine();
            }
            bWriter.close();
            fWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getKeywordUrls() {
        return keywordUrls;
    }


}
