package cinderella;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class MedParser {
    private static final Logger logger = LoggerFactory.getLogger(MedParser.class);

    private static final Logger resultLogger = LoggerFactory.getLogger("link.visitor");

    private static final String MED_BASE_URL = "http://www.ngmu.ru";

    private static List<String> extractLinksFromGroupPage() throws IOException {
        Document groupsDomDocument = Jsoup.connect(MED_BASE_URL + "/groups/").userAgent("Chrome").timeout(5000).get();

        ArrayList<Element> twoCoursesLinks = new ArrayList<>();
        twoCoursesLinks.addAll(groupsDomDocument.select("tbody tr td:nth-child(2) a"));
        twoCoursesLinks.addAll(groupsDomDocument.select("tbody tr td:nth-child(3) a"));

        List<String> links = twoCoursesLinks.stream().map(elem -> elem.attr("href").trim()).collect(Collectors.toList());
        logger.debug("Extracted {} links", links.size());
        return links;
    }

    private static void visitLink(String relativeLink) {
        Document groupMembersDomDocument;
        try {
            groupMembersDomDocument = Jsoup.connect(MED_BASE_URL + relativeLink).userAgent("Chrome").timeout(5000).get();
        } catch (IOException e) {
            logger.error("Err", e);
            return;
        }

        int rowsAmount = groupMembersDomDocument.select("tbody tr").size();

        Elements linkToPersonElements = groupMembersDomDocument.select("tbody a");
        if (linkToPersonElements.size() != rowsAmount) {
            logger.warn("Link {} amount of links is {} and amount of rows {}", relativeLink, rowsAmount, linkToPersonElements.size());
        }

        Predicate<Element> cinderellaNameMatcher = personElem -> {
            String personFullName = personElem.text();

            String name = personFullName.split(" ")[1];
            return name.equals("Александра");
        };

        linkToPersonElements.stream().filter(cinderellaNameMatcher).forEach(element -> {
            resultLogger.info("{} {}{}", element.text(), MED_BASE_URL, element.attr("href"));
        });
    }

    public static void parse() throws IOException {
        List<String> links = extractLinksFromGroupPage();

        links.stream().forEach(MedParser::visitLink);
    }
}
