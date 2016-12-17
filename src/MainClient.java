import java.util.Scanner;

/**
 * Created by Benny Pena on 12/10/2016.
 */
public class MainClient {
    public static void main(String[]args){
        try {
//            Crawler crawler = new Crawler("https://www.oldwestbury.edu/");
//            Crawler crawler2 = new Crawler("https://www.youtube.com/");
            Indexer indexer = new Indexer();
            indexer.index();
            Ranker ranker = new Ranker(indexer.getHashmap());
            System.out.println("\nKeyword to search?");
            Scanner scan = new Scanner(System.in);
            String keyword = scan.next();
            ranker.rankBasedBy(keyword);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
