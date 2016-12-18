package cinderella;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class MedParser {
    private static final Logger logger = Logger.getLogger(MedParser.class);

    private static final String MED_BASE_URL = "http://www.ngmu.ru";

    private static List<String> extractLinksFromGroupPage() throws IOException {
        Document groupDomDocument = Jsoup.connect(MED_BASE_URL + "/groups/").userAgent("Chrome").timeout(5000).get();

        ArrayList<Element> twoCoursesLinks = new ArrayList<>();
        twoCoursesLinks.addAll(groupDomDocument.select("tbody tr td:nth-child(2) a"));
        twoCoursesLinks.addAll(groupDomDocument.select("tbody tr td:nth-child(3) a"));

        List<String> links = twoCoursesLinks.stream().map(elem -> elem.attr("href").trim()).collect(Collectors.toList());
        return links;
    }

    public static void parse() throws IOException {
        List<String> links = extractLinksFromGroupPage();
    }
}
