import java.io.IOException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.ErrorHandler;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLvalidation_DTD {
    static String xmlFile;
    static boolean validationCheck = false;
    private boolean isValid = true;

    public class SimpleErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) throws SAXException {
            System.out.println(e.getMessage());
        }

        public void error(SAXParseException e) throws SAXException {
            isValid = false;
            System.out.println(e.getMessage());
        }

        public void fatalError(SAXParseException e) throws SAXException {
            isValid = false;
            System.out.println(e.getMessage());
        }
    }

    public void validate(String xmlFile) {
        isValid = true;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setValidating(validationCheck);

            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new SimpleErrorHandler());

            Document doc = builder.parse(xmlFile);

            if (validationCheck) {
                if (!isValid) {
                    System.out.println("DTD파일이 유효하지 않습니다.");
                }

            }

        } catch (

        ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setValidate(boolean validation) {
        validationCheck = validation;

    }
}
