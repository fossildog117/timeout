import QB.quantumbiology.HtmlClustering;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeathToEikon {

    public static void main(String[] args) {

        Logger logger = Logger.getLogger ("");
        logger.setLevel (Level.OFF);

        try {

            SearchEngine searchEngine = new SearchEngine("http://www.timeout.com/london/", 1000000);
            searchEngine.Learn();

        } catch (Exception e) {
            //e.printStackTrace();
        }

        Assistant.PrintList();

    }

}