import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**Retrieves the URL links from all links in the associated domain
 *
 * Created by Benny Pena on 12/10/2016.
 */
public class Crawler {
    private File savedFile;     // File to save the urls of the domain
    private ArrayList<String> urlsTransversed;
    private String rootURL;
    private java.sql.Connection connection = null;

    public Crawler(String rootToCrawl) throws IOException {
        savedFile = new File("CrawlerURLS.txt");
        urlsTransversed = new ArrayList<>();
        rootURL = getRoot(rootToCrawl);
        crawl(rootToCrawl);
    }

    private String getRoot(String rootToCrawl) {
        return rootToCrawl.replace("http://www.","").replace("https://www.","");
    }

    public void crawl(String url) throws IOException {
        System.out.println("Crawling Url: " + url);

        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        }catch (IOException e){
            doc = null;
        }
        urlsTransversed.add(url);
        if (doc != null) {
            writeToFile(savedFile, url);
            //get all links and recursively call the processPage method
            Elements questions = doc.select("a[href]");
            for (int i = 0; i < questions.size(); ++i) {
                if (questions.get(i).attr("href").contains(rootURL) &&
                        checkURL(questions.get(i).attr("abs:href")))
                    crawl(questions.get(i).attr("abs:href"));
            }
        }

    }


    private boolean checkURL(String url){
        if (!urlsTransversed.contains(url))
            if (url.contains("http") || url.contains("https"))
                if (!url.contains(".pdf") && !url.contains("?") && !url.contains("event-calendar") )
                    return true;
        return false;
    }
    private void writeToFile(File savedFile, String url) {
        try {
            FileWriter fWriter = new FileWriter(savedFile, true);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            bWriter.write(url);
            bWriter.newLine();
            bWriter.close();
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (connection != null || !connection.isClosed()) {
            connection.close();
        }
    }
}
