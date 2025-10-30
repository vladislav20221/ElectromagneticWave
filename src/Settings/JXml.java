package Settings;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Работа с файлом конфигурации.
 * @author Vladislav
 */
public class JXml {
    
    /**
    public static void main ( String ... args ) {        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            File file = new File ("src\\Settings\\Config.xml");
            Document doc = builder.parse( file );
            
            Element root = doc.getDocumentElement();
            NodeList children = root.getChildNodes();
            for ( int i = 0; i < children.getLength(); i++ ) {
                Node child = children.item(i);
                if ( child instanceof Element ) {
                    Element childElement = (Element) child;
                    Text textNode = (Text) childElement.getFirstChild();
                    String text = textNode.getData().trim();                    
                    System.out.println ( text );
                }                
            }                        
        } catch ( ParserConfigurationException | SAXException | IOException ex ) {
            Logger.getLogger(JXml.class.getName()).log(Level.SEVERE, null, ex);
        }                
    }
    **/
}
