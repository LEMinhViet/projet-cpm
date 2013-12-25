package filtrer;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

public class CoffeeLog {
	
	public CoffeeLog() {
		
	}
	
	public void transformerCoffeeLog(String nom) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(nom));
		
		document.getDocumentElement().normalize();
		 
		NodeList recordList = document.getDocumentElement().getElementsByTagName("record");
		
		for (int  i = 0; i < recordList.getLength(); i++) {
			Node record = recordList.item(i);
			removeWhitespaceNodes((Element)record);
			
			// Chercher les records avec le nom de l'utilisateur
			Node message = record.getChildNodes().item(0);
			
			if (message.getChildNodes().getLength() > 0) {
				
				if (!((Element)message.getChildNodes().item(0)).getAttribute("UserName").isEmpty()) {

				}
			}
			
		}
	}	
	
	public static void removeWhitespaceNodes(Element e) {
		NodeList children = e.getChildNodes();
		for (int i = children.getLength() - 1; i >= 0; i--) {
			Node child = children.item(i);
			if (child instanceof Text && ((Text) child).getData().trim().length() == 0) {
				e.removeChild(child);
			}
			else if (child instanceof Element) {
				removeWhitespaceNodes((Element) child);
			}
		}
	}
}
