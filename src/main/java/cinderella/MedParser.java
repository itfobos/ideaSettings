package cinderella;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class MedParser {
    private static final Logger logger = Logger.getLogger(MedParser.class);
    private static final String MED_URL = "http://www.ngmu.ru";

    public static void parseGroupsPage() throws IOException {
        Document document = Jsoup.connect(MED_URL + "/groups/").userAgent("Chrome").timeout(5000).get();
        logger.debug("Ok");
    }
}
