import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
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

    public Crawler(String rootToCrawl, String keyword) throws IOException {
        savedFile = new File("CrawlerURLS.txt");
        urlsTransversed = new ArrayList<>();
        rootURL = getRoot(rootToCrawl);
        crawl(rootToCrawl);
        //crawlDataBase(rootToCrawl);
    }

    private String getRoot(String rootToCrawl) {
        return rootToCrawl.replace("http://www.","").replace("https://www.","");
//        return rootToCrawl;
    }

    public void crawl(String url) throws IOException {
        System.out.println("Crawling Url: " + url);

        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        }catch (IOException e){
            doc = null;
        }
//        Document doc = conn.get();
        urlsTransversed.add(url);
//        checkDatabase(url);
        if (doc != null) {

            //if (doc.text().toLowerCase().contains(keyword.toLowerCase())) {
                writeToFile(savedFile, url);
            //}

            //get all links and recursively call the processPage method
            Elements questions = doc.select("a[href]");
            for (int i = 0; i < questions.size(); ++i) {
                if (questions.get(i).attr("href").contains(rootURL) &&
                        checkURL(questions.get(i).attr("abs:href")))
                    crawl(questions.get(i).attr("abs:href"));
            }
        }

    }

    private void crawlDataBase(String url){
        try{
            String dbPath = "jdbc:mysql://localhost:3306/bengine?autoReconnect=true&useSSL=false";
            connection = DriverManager.getConnection(dbPath,"root", "");
            System.out.println("Crawling Url: " + url);

            String sql = "select * from crawler where URL = '"+url+"'";
            //runSql2("TRUNCATE crawler;");
            ResultSet rs = runSql(sql);
            if(rs.next()){

            }else{
                //store the URL to database to avoid parsing again
                sql = "INSERT INTO `crawler` (`URL`) VALUES (?);";
                PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, url);
                stmt.execute();

                //get useful information
                Document doc = Jsoup.connect(url).get();


                //get all links and recursively call the processPage method
                Elements questions = doc.select("a[href]");
                for(int i = 0; i < questions.size(); ++i){
                    if(questions.get(i).attr("href").contains(rootURL) &&
                            checkURL(questions.get(i).attr("abs:href")))
                        crawlDataBase(questions.get(i).attr("abs:href"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ResultSet runSql(String sql) throws SQLException {
        Statement sta = connection.createStatement();
        return sta.executeQuery(sql);
    }

    public boolean runSql2(String sql) throws SQLException {
        Statement sta = connection.createStatement();
        return sta.execute(sql);
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
