/**
 * Created by Benny Pena on 12/10/2016.
 */
public class MainClient {
    public static void main(String[]args){
        try {
            //Crawler crawler = new Crawler("https://www.oldwestbury.edu/","art");
            //Indexer indexer = new Indexer(crawler);
            Indexer indexer = new Indexer();
            indexer.index();
            Ranker ranker = new Ranker(indexer.getKeywordUrls());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
