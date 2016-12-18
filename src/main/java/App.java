import cinderella.MedParser;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {
        initLog4j();

        MedParser.parseGroupsPage();

        System.out.println("OK");
    }

    private static void initLog4j() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // then we have to create document-loader:
        DocumentBuilder loader = factory.newDocumentBuilder();

        loader.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId)
                    throws SAXException, IOException {
                // Check for dtd ref
                if (systemId.endsWith("org/apache/log4j/xml/log4j.dtd")) {
                    // return the dtd from classpath
                    return new InputSource(getClass().getClassLoader()
                            .getResourceAsStream(
                                    "org/apache/log4j/xml/log4j.dtd"));
                }

                // Resume normal flow
                return null;
            }
        });

        // loading a DOM-tree...
        Document document = loader.parse(App.class.getResourceAsStream("/log4j.xml"));
        DOMConfigurator.configure(document.getDocumentElement());
    }

}
