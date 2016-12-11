import java.io.IOException;

/**
 * Created by Benny Pena on 12/10/2016.
 */
public class MainClient {
    public static void main(String[]args){
        try {
            Crawler crawler = new Crawler("https://www.youtube.com/","art");
            Indexer indexer = new Indexer(crawler);
            Ranker ranker = new Ranker(indexer.getKeywordUrls());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
