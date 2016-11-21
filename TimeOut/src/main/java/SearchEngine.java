import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

import javafx.beans.binding.MapExpression;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by nathanliu on 17/09/2016.
 */
public class SearchEngine {

    private ArrayList<String> visitedSites;
    private LinkedList<String> queue;
    private PrintWriter writer;
    private int Stopper = 7200000;

    public SearchEngine(String init_url, int stopper) throws IOException {

        this.visitedSites = new ArrayList<>();
        this.queue = new LinkedList<>();
        this.Stopper = stopper;
        this.writer = new PrintWriter(System.getProperty("user.dir") + "/output.txt");

        Document doc = Jsoup.connect(init_url).get();
        AddAll(doc.getElementsByAttribute("href"));

    }

    public void Learn() {

        int counter = 0;
        while (queue.size() >= 0 && ++counter < Stopper) {
            try {
                Iterate(queue.getFirst());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                visitedSites.add(queue.remove(0));
            }
        }

        writer.close();
    }

    private void Iterate(String visiting_url) throws IOException {

        if (!hasVisited(visitedSites, visiting_url)) {

            visiting_url = RefineURL(visiting_url);

            if (visiting_url.contains("www.timeout.com/london")) {
                Document doc = Jsoup.connect(visiting_url).get();
                AddAll(doc.getElementsByAttribute("href"));

                try {
                    List<Element> articles = doc.getElementsByTag("article");

                    for (Element article : articles) {

                        if (article.attr("class").equals("listing")) {

                            ExtractData(article, visiting_url);

                        }
                    }

                } catch (Exception e)  {
                    e.printStackTrace();
                }
            }
        }
    }

    public void ExtractData(Element element, String url) throws Exception {

        HashMap<String, String> dict = new HashMap<>();

        List<Element> titles = element.getElementsByClass("listing_page_title");
        dict.put("Name: " + titles.get(0).text(), "dummy");
        dict.put("Category: " + GetCategoryFromURL(url), "dummy");

        Element d = element.getElementById("tab___content_2");
        List<Element> details = d.getElementsByClass("listing_details");

        List<Element> innerDetails = details.get(0).getElementsByTag("tr");
        innerDetails.remove(innerDetails.size() - 1);

        String telephone = "tel: N/A";
        String website = "";

        for (Element t : innerDetails) {

            List<Element> values = t.getAllElements();

            try {
                List<Element> contactDetails = values.get(0).getElementsByTag("a");
                website = contactDetails.get(0).attr("href");
                telephone = contactDetails.get(1).attr("href");
            } catch (Exception e) {
                dict.put(values.get(0).text(), values.get(1).text());
            }

        }

        List<Element> precursor = element.getElementsByClass("review__article");
        dict.put("Description: " + precursor.get(0).getElementsByTag("p").get(0).text(), "dummy");

        String output = "{\n";

        for (Map.Entry<String, String> pair : dict.entrySet()) {
            output = output + pair.getKey() + "\n";
        }

        output = output + telephone + "\n" + "Website: " + website + "\n" + "}";

        writer.println(output);
    }

    private String GetCategoryFromURL(String url) {

        String retString = "";

        for (int i = url.length() - 1 ; i > 0; i--) {

            if (url.charAt(i) == '/') {

                for (int j = i - 1; j > 0 ; j--) {

                    if (url.charAt(j) == '/') {
                        break;
                    }

                    retString = url.charAt(j) + retString;

                }

                break;

            }

        }

        return retString;

    }

    private String RefineURL(String url) {
        if (!url.substring(0, 4).equals("http")) {
            return "http://www.timeout.com" + url;
        }

        return url;

    }

    private boolean hasVisited(List<String> list1, String url) {

        for (String item1 : list1) {

            if (item1.equals(url)) {
                return true;
            }

        }

        return false;

    }

    private void AddAll(List<Element> list) {

        for (Element s : list) {

            queue.add(s.attr("href"));

        }

    }

}
