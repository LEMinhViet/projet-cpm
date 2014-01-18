package support;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class CoffeeToLog {
	private HashMap<String, String> UserIP = new HashMap<String, String>();
	
	/**
	 * Transsformer le fichier Coffee au fichier Log
	 * @param nom
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void transformerCoffeeLog(String nom) throws ParserConfigurationException, SAXException, IOException {
		// Lire le fichier xml
		FileWriter fw = new FileWriter(new File(nom.replace("xml", "txt")));
		BufferedWriter bw = new BufferedWriter(fw);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(nom));
		
		document.getDocumentElement().normalize();
		// Retirer tous les record dans le fichier xml
		NodeList recordList = document.getDocumentElement().getElementsByTagName("record");
		
		for (int  i = 0; i < recordList.getLength(); i++) {
			Node record = recordList.item(i);
			
			// Supprimer tous les Nodes null
			removeWhitespaceNodes((Element)record);
			
			// Message
			Node message = record.getFirstChild();;
			
			if (message.getChildNodes().getLength() > 0) {
				// Message Header
				Node messageHeader = message.getFirstChild();
				NamedNodeMap messageHeaderAttributes = messageHeader.getAttributes();
				
				String sLog = "";
				/* On sauver les 4 types de record : 
				 * SessionType : information sur les sessions
				 * ConnectionType : information sur les connections des utilisateurs
				 * DisconnectionType : information sur les disconnections des utilisateurs
				 * GenericType : information sur les actions des utilisateurs dans les sessions
				 *
				 *       IP      UTILISATEUR         DATE                     OUTIL                 -                CONTENUE (TEXT)          ETAPE       OUTIL               TYPE
				 * 132.227.113.204 SERVER - [11/12/2012:15:52:52 +0200] "MLThreadedChatServerMessage L'homophobie est un délit puni par la loi" 2 - "mlThreadedChatTool" "GenericType"
				 */
				if (messageHeaderAttributes.getNamedItem("messageType").getNodeValue().equals("ConnectionType")) {
					getConnectionType(message, messageHeaderAttributes);
				} else if (messageHeaderAttributes.getNamedItem("messageType").getNodeValue().equals("GenericType")) {
					sLog = getGenericType(message, messageHeaderAttributes);
					fw.write(sLog + "\n");
				} else {
					continue;
				}
				/*
				 * On a besoin seulement des actions des utilisateur => pour les autres types, on laisse tomber 
				 * Mais on utilise le ConnectionType pour garder l'ensemble de IP et Utilisateur
				 */
			}
			
		}
		
		bw.close();
	}	
	
	/**
	 * Pour les records sessionType, on cherche seulement le temps, le text de sessionInfo
	 * @param message : le premier sous-noeuds du record
	 * @param messageHeaderAttributes : le dernier sous-noeuds de record 
	 * @return : une ligne de log
	 */
	public String getSessionType(Node message, NamedNodeMap messageHeaderAttributes) {
		StringBuilder sbLog = new StringBuilder();
		sbLog.append("- - -");
		
		long millis = Long.parseLong(messageHeaderAttributes.getNamedItem("millis").getNodeValue());
		sbLog.append(" [" + (new CPMCalendar(millis)).getDateString() + "]");
		
		sbLog.append(" \"" + messageHeaderAttributes.getNamedItem("SessionInfo").getNodeValue() + "\"");
		sbLog.append(" - - \"-\" \"SessionType\"");
		
		return sbLog.toString();
	}
	
	/**
	 * Pour  les records ConnectionType, on cherche le IP et le nom d'utilisateur et le temps
	 * @param message : le premier sous-noeuds du record
	 * @param messageHeaderAttributes : le dernier sous-noeuds de record 
	 * @return : une ligne de log
	 */
	public String getConnectionType(Node message, NamedNodeMap messageHeaderAttributes) {
		StringBuilder sbLog = new StringBuilder();
		
		String ip = messageHeaderAttributes.getNamedItem("From").getNodeValue().split("/")[2];
		ip = ip.split(":")[0];
		sbLog.append(ip);
		
		String nom = messageHeaderAttributes.getNamedItem("UserName").getNodeValue().replace(" ", "_"); 
		sbLog.append(" " + nom);
		sbLog.append(" -");
		
		if (!UserIP.containsKey(nom)) 	UserIP.put(nom, ip); 
		
		long millis = Long.parseLong(messageHeaderAttributes.getNamedItem("millis").getNodeValue());
		sbLog.append(" [" + (new CPMCalendar(millis)).getDateString() + "]");
		
		sbLog.append(" \"-\"");
		sbLog.append(" - - \"-\" \"ConnectionType\"");
		
		return sbLog.toString();
	}
	
	/**
	 * Pour  les records DisconnectionType, on cherche le IP et le nom d'utilisateur et le temps
	 * @param message : le premier sous-noeuds du record
	 * @param messageHeaderAttributes : le dernier sous-noeuds de record 
	 * @return : une ligne de log
	 */
	public String getDisconnectionType(Node message, NamedNodeMap messageHeaderAttributes) {
		StringBuilder sbLog = new StringBuilder();
		
		String ip = messageHeaderAttributes.getNamedItem("From").getNodeValue().split("/")[2];
		ip = ip.split(":")[0];
		sbLog.append(ip);
		
		sbLog.append(" " + messageHeaderAttributes.getNamedItem("UserName").getNodeValue().replace(" ", "_"));
		sbLog.append(" -");
		
		long millis = Long.parseLong(messageHeaderAttributes.getNamedItem("millis").getNodeValue());
		sbLog.append(" [" + (new CPMCalendar(millis)).getDateString() + "]");
		
		sbLog.append(" \"-\"");
		sbLog.append(" - - \"-\" \"DisconnectionType\"");
		
		return sbLog.toString();
	}
	
	/**
	 * Pour les records GenericType, on cherche le IP et le nom d'utilisateur, le temps, le nom d'outil :
	 * - positionometerTool : le contenu du "vote" dans PositionometerMessage
	 * - mlThreadedChatTool : le text dans MLThreadedChatServerMessage
	 * - notetool : le text dans NoteMessage
	 * - graphicalTool : le type d'outil graphique utilise et le text (si il y a) dans graphicalToolMessage
	 * @param message : le premier sous-noeuds du record
	 * @param messageHeaderAttributes : le dernier sous-noeuds de record 
	 * @return : une ligne de log
	 */
	public String getGenericType(Node message, NamedNodeMap messageHeaderAttributes) {
		StringBuilder sbLog = new StringBuilder();
		
		// Obtenir le IP
		String ip = messageHeaderAttributes.getNamedItem("From").getNodeValue().split("/")[2];
		ip = ip.split(":")[0];
		sbLog.append(ip);
		
		// Obtenir le nom 
		sbLog.append(" " + messageHeaderAttributes.getNamedItem("UserName").getNodeValue().replace(" ", "_"));		
		sbLog.append(" -");
		
		// Obtenir le temps
		long millis = Long.parseLong(messageHeaderAttributes.getNamedItem("millis").getNodeValue());
		sbLog.append(" [" + (new CPMCalendar(millis)).getDateString() + "]");
		
		// totale : 725 - mlThreadedChatTool : 56 - positionometerTool : 410 - graphicalTool : 259
		Node toolMessageContent = message.getLastChild().getFirstChild();
		
		/**
		 * Pour le positionometerTool
		 */
		if (messageHeaderAttributes.getNamedItem("ToolName").getNodeValue().equals("positionometerTool")) {
			NamedNodeMap toolMessageAttributes = toolMessageContent.getAttributes();
			if ("PositionometerMessage".equals(toolMessageContent.getNodeName())) {
				sbLog.append(" \"" + toolMessageContent.getNodeName() + 
						     " " + toolMessageAttributes.getNamedItem("vote").getNodeValue() + "\"");
			} else {
				sbLog.append(" \"" + toolMessageContent.getNodeName() + "\"");
			}			
		} 
		
		/**
		 * Pour le mlThreadedChatTool
		 */
		else if (messageHeaderAttributes.getNamedItem("ToolName").getNodeValue().equals("mlThreadedChatTool")) {
			NamedNodeMap toolMessageAttributes = toolMessageContent.getAttributes();
			
			if (toolMessageAttributes.getNamedItem("text") != null) {
				// On remplace le nom SERVER par le vrai nom d'utilisateur dans les MLThreadedChatServerMessage
				String vraiNom = toolMessageAttributes.getNamedItem("from").getNodeValue().replace(" ", "_");
				
				if (!vraiNom.equals("SERVER") && !vraiNom.equals("Enseignant")) {
					sbLog.setLength(0);
					sbLog.append(UserIP.get(vraiNom) + " " + vraiNom + " - [" + (new CPMCalendar(millis)).getDateString() + "]");
				}
				
				// Ajouter le symbole * pour indiquer que les text apres sont les texts des utilisateurs
				sbLog.append(" \"" + toolMessageContent.getNodeName() +
					     " *" + toolMessageAttributes.getNamedItem("text").getNodeValue().replace(" ", "_") + "\"");
			} else {
				sbLog.append(" \"" + toolMessageContent.getNodeName() + "\"");
			}
		} 
		
		/** Il n' y pas cet outil dans cette version du fichier COFFEE log
		else if (messageHeaderAttributes.getNamedItem("ToolName").getNodeValue().equals("notetool")) {
			if ("NoteMessage".equals(toolMessageContent.getNodeName())) {
				NamedNodeMap toolMessageAttributes = toolMessageContent.getAttributes();
				sbLog.append(" \"" + toolMessageContent.getNodeName() +
						     " " + toolMessageAttributes.getNamedItem("text").getNodeValue() + "\"");
			} else {
				sbLog.append(" \"" + toolMessageContent.getNodeName() + "\"");
			}	
		} */ 
		
		/**
		 * Pour le graphicalTool
		 */
		else if (messageHeaderAttributes.getNamedItem("ToolName").getNodeValue().equals("graphicalTool")) {
			if ("graphicalToolMessage".equals(toolMessageContent.getNodeName())) {
				NamedNodeMap toolMessageAttributes = toolMessageContent.getAttributes();
				sbLog.append(" \"" + toolMessageContent.getNodeName() +
						     " " + toolMessageAttributes.getNamedItem("typeID").getNodeValue());
				if (toolMessageAttributes.getNamedItem("text") != null) {
					sbLog.append(" " + toolMessageAttributes.getNamedItem("text").getNodeValue().replace(" ", "_"));
				}
				sbLog.append("\"");
			} else {
				sbLog.append(" \"" + toolMessageContent.getNodeName() + "\"");
			}	
		}
		
		sbLog.append(" " + messageHeaderAttributes.getNamedItem("StepNumber").getNodeValue());
		sbLog.append(" -");
		sbLog.append(" \"" + messageHeaderAttributes.getNamedItem("ToolName").getNodeValue() + "\"");
		sbLog.append(" \"GenericType\"");
		
		return sbLog.toString();
	}
	
	/**
	 * Supprimer les sous-noeuds null des noeuds "record" dans le fichier xml
	 * @param e : le noeud null qui n'a pas le text
	 */
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
