package parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import generated.ComputerStore;
import generated.Tcountry;

public class DOMParser {
	private static boolean logEnabled = false;

	public static void log(Object o) {
		if (logEnabled) {
			System.out.println(o);
		}
	}

	private ComputerStore parseComputerStore(Node node) {
		
		ComputerStore ComputerStore = new ComputerStore();
		NodeList nodes = node.getChildNodes();
	
		for (int i = 0; i < nodes.getLength(); i++) {
			
			Node item = nodes.item(i);
//			System.out.println(item.getLocalName());
			if ("company".equals(item.getLocalName()))
				ComputerStore.getCompany().add(parseCompany(item));
		}
		return ComputerStore;
	}


	private Company parseCompany(Node node) {
		Company oItem = new Company();
	   
	    if (node.hasAttributes()) {
	      NamedNodeMap attrs = node.getAttributes();
	      Node item = attrs.getNamedItem("id");
	      log(item.getLocalName() + " = " + item.getTextContent());
	      oItem.setId(Integer.parseInt(item.getTextContent()));
			

//	     System.out.println(item.getLocalName() + " = " + item.getTextContent());
	      
	    }
	    NodeList nodes = node.getChildNodes();
	    for (int i = 0; i < nodes.getLength(); i++) {
	      Node item = nodes.item(i);
	      log(item.getLocalName());
	     if ("name".equals(item.getLocalName())) {
	        log(item.getLocalName() + " = " + item.getTextContent());
		    oItem.setName(item.getTextContent());
	      }else if ("country".equals(item.getLocalName())) {
		        log(item.getLocalName() + " = " + item.getTextContent());
			    oItem.setCountry(Tcountry.fromValue(item.getTextContent())); 
		  }
	      else if ("email".equals(item.getLocalName())) {
		        log(item.getLocalName() + " = " + item.getTextContent());
			    oItem.setEmail(item.getTextContent());
	      }
	    }
	    System.out.println(oItem);
        return oItem;
	    
	  }
	
	public List<ComputerStore> parse(InputStream in) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		
		// make parser validating
		dbf.setFeature("http://xml.org/sax/features/validation", true);
		dbf.setFeature("http://apache.org/xml/features/validation/schema", true);

		DocumentBuilder db = dbf.newDocumentBuilder();
		db.setErrorHandler(new DefaultHandler() {
			@Override
			public void error(SAXParseException e) throws SAXException {
				throw e; // <-- throw exception if XML document is NOT valid
			}
		});
		

		Document root = db.parse(in);
		
		List<ComputerStore> ComputerStores = new ArrayList<>();
           

//		Element e = root.getDocumentElement();

		NodeList xmlComputerStoress = root.getElementsByTagNameNS("*", "computerStore");		
		for (int i = 0; i < xmlComputerStoress.getLength(); i++) {
//			System.out.println(xmlComputerStoress.item(i));
			ComputerStores.add(parseComputerStore(xmlComputerStoress.item(i)));
			
		}
		return ComputerStores;
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		DOMParser domParser = new DOMParser();
		InputStream in = new FileInputStream("company.xml");
		List<ComputerStore> ComputerStores = domParser.parse(in);
//		System.out.println(ComputerStores);
        
	}
}
