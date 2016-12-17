import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Benny Pena on 12/10/2016.
 */
public class Ranker {
    private HashMap<String, ArrayList<String>> hashmap;
    public Ranker(HashMap<String, ArrayList<String>> hashmap) {
        this.hashmap = hashmap;
    }
    public void rankBasedBy(String keyword){
        if (hashmap.containsKey(keyword)) {
            ArrayList<String> rankedUrls = hashmap.get(keyword);
            printResults(keyword,rankedUrls);
        }else{
            System.out.println("Could not find anything");
        }

    }

    private void printResults(String keyword, ArrayList<String> rankedUrls) {
        System.out.println("\n Searching for: " + keyword + "\n");
        for (int i = 0; i<rankedUrls.size();++i){
           System.out.println(rankedUrls.get(i));
        }
    }
}
