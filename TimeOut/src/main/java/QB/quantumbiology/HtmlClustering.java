package QB.quantumbiology;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class HtmlClustering {

    public static List<Element> getClusterFromUrl(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();
        return getClusterFromElement(doc);

    }

    public static List<Element> getClusterFromElement(Element doc) {

        List<Element> output = new LinkedList<>();
        output.add(doc);
        List<Element> nodes = doc.children();

        while(nodes.size() > 0) {

            nodes = updateTheCluster(nodes,output);

        }

        return output;

    }

    private static List<Element> updateTheCluster(List<Element> nodes, List<Element> output) {

        List<Element> nextLayerNodes = new LinkedList<>();

        for (Element node : nodes) {

            output.add(node);
            nextLayerNodes.addAll(node.children());

        }

        return nextLayerNodes;

    }

}
