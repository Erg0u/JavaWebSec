package run.ergou.javawebsec.controller;

import com.example.javasec.entity.User;
import org.apache.commons.digester3.Digester;
import org.apache.commons.io.IOUtils;
import org.dom4j.io.SAXReader;
import org.jdom2.input.SAXBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/xxe")
public class XXE {
    @PostMapping("/xmlReader/vulnerable")
    public String xmlReaderVulnerable(HttpServletRequest req) throws Exception {
        String body = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.parse(new InputSource(new StringReader(body)));
        return "XMLReader xxe";
    }

    @PostMapping("/xmlReader/secure")
    public String xmlReaderSecure(HttpServletRequest req) throws Exception {
        String body = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();

        xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        xmlReader.parse(new InputSource(new StringReader(body)));
        return "security";
    }

    @PostMapping("/SAXParser/vulnerable")
    public String SAXParserVulnerable(HttpServletRequest req) throws Exception {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(req.getInputStream(), new DefaultHandler());
        return "SAXParser xxe";
    }

    @PostMapping("/SAXParser/secure")
    public String SAXParserSecure(HttpServletRequest req) throws Exception {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(req.getInputStream(), new DefaultHandler());
        return "security";
    }

    @PostMapping("/SAXBuilder/vulnerable")
    public String SAXBuilderVulnerable(HttpServletRequest req) throws Exception {
        SAXBuilder saxBuilder = new SAXBuilder();
        saxBuilder.build(req.getInputStream());
        return "jdom2-SAXBuilder xxe";
    }

    @PostMapping("/SAXBuilder/secure")
    public String SAXBuilderSecure(HttpServletRequest req) throws Exception {
        SAXBuilder saxBuilder = new SAXBuilder();
        saxBuilder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        saxBuilder.setFeature("http://xml.org/sax/features/external-general-entities", false);
        saxBuilder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        saxBuilder.build(req.getInputStream());
        return "security";
    }

    @PostMapping("/SAXReader/vulnerable")
    public String SAXReaderVulnerable(HttpServletRequest req) throws Exception {
        SAXReader saxReader = new SAXReader();
        saxReader.read(req.getInputStream());
        return "dom4j-SAXReader xxe";
    }

    @PostMapping("/SAXReader/secure")
    public String SAXReaderSecure(HttpServletRequest req) throws Exception {
        SAXReader saxReader = new SAXReader();
        saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        saxReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
        saxReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        saxReader.read(req.getInputStream());
        return "security";
    }

    @PostMapping("/Digester/vulnerable")
    public String DigesterVulnerable(HttpServletRequest req) throws Exception {
        Digester digester = new Digester();
        digester.parse(req.getInputStream());
        return "commons-digester3-Digester xxe";
    }

    @PostMapping("/Digester/secure")
    public String DigesterSecure(HttpServletRequest req) throws Exception {
        Digester digester = new Digester();
        digester.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        digester.parse(req.getInputStream());
        return "security";
    }

    @PostMapping("/DocumentBuilder/vulnerable")
    public String DocumentBuilderVulnerable(HttpServletRequest req) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(req.getInputStream());

        return "DocumentBuilder xxe";
    }

    @PostMapping("/DocumentBuilder/secure")
    public String DocumentBuilderSecure(HttpServletRequest req) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.parse(req.getInputStream());
        return "security";
    }

    @PostMapping("/XMLInputFactory/vulnerable")
    public String XMLStreamReaderVulnerable(HttpServletRequest req) throws Exception {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(req.getInputStream());
        while (xmlStreamReader.hasNext()) {
            int point = xmlStreamReader.next();
            switch (point) {
                case XMLStreamReader.START_ELEMENT: {
                    String elemName = xmlStreamReader.getName().toString();
                }
                case XMLStreamReader.END_DOCUMENT: {

                }
            }
        }
        return "XMLInputFactory-xxe";
    }

    @PostMapping("/XMLInputFactory/secure")
    public String XMLStreamReaderSecure(HttpServletRequest req) throws Exception {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(req.getInputStream());
        while (xmlStreamReader.hasNext()) {
            int point = xmlStreamReader.next();
            switch (point) {
                case XMLStreamReader.START_ELEMENT: {
                    String elemName = xmlStreamReader.getName().toString();
                }
                case XMLStreamReader.END_DOCUMENT: {

                }
            }
        }
        return "security";
    }

    @PostMapping("/Validator/vulnerable")
    public String ValidatorVulnerable(HttpServletRequest req) throws Exception {
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = schemaFactory.newSchema();
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(req.getInputStream()));
        return "Validator xxe";
    }

    @PostMapping("/Validator/secure")
    public String ValidatorSecure(HttpServletRequest req) throws Exception {
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = schemaFactory.newSchema();
        Validator validator = schema.newValidator();
        validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        validator.validate(new StreamSource(req.getInputStream()));
        return "security";
    }

    @PostMapping("/Unmarshaller/vulnerable")
    public String UnmarshallerVulnerable(HttpServletRequest req) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.unmarshal(req.getInputStream());
        return "Unmarshaller xxe";
    }

    @PostMapping("/Unmarshaller/secure")
    public String UnmarshallerSecure(HttpServletRequest req) throws Exception {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        saxParserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        SAXSource xmlSource = new SAXSource(saxParserFactory.newSAXParser().getXMLReader(),
                new InputSource(req.getInputStream()));

        JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.unmarshal(xmlSource);
        return "security";
    }

    @PostMapping("/XPath/vulnerable")
    public String XPathExpressionVulnerable(HttpServletRequest req) throws Exception {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.evaluate("/", new InputSource(req.getInputStream()));
        return "XPath xxe";
    }

    @PostMapping("/XPath/secure")
    public String XPathExpressionSecure(HttpServletRequest req) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.evaluate("/", documentBuilder.parse(req.getInputStream()));
        return "security";
    }
}
