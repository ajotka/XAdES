import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parse {

	static Map<String, String> data = new HashMap<String, String>();
	private static boolean isXades;
	public static Document doc;
	
  public static Map<String, String> parseXML(File XmlFile) throws IOException {
    try {
    	    	
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//	dbFactory.setIgnoringComments(true);
//	dbFactory.setIgnoringElementContentWhitespace(false);
//	dbFactory.setValidating(false);
	dbFactory.setNamespaceAware(true);
	doc = dbFactory.newDocumentBuilder().parse(XmlFile);
	
	if ( !StructureVerifier.checkStructure( Xades.path ) ) {
		System.out.println("1");
		Xades.fw.close();
		System.exit(1);
	}
	
	//normalize
	doc.getDocumentElement().normalize();

		isXades = false;
			NodeList nodeList = doc.getElementsByTagName("*");
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node node = nodeList.item(i);
		        if (node.getNodeType() == Node.ELEMENT_NODE) {
		            if( node.getNodeName().contains("xades") ){
		            	isXades = true;
		            	break;
		            }
		        }
		    }
		 if ( isXades == true ) {

			String[] Enveloped = {"ds:Transform"};
			getMainMarker(doc, "ds:Signature", Enveloped );
			if(data.containsValue("http://www.w3.org/2000/09/xmldsig#enveloped-signature")) {
				String[] SignatureList = {"ds:DigestValue", "ds:DigestValueSP", "ds:DigestValueC","ds:SignatureValue","ds:Modulus","ds:Exponent","ds:X509Certificate"};
				String[] Algorithms = {"ds:CanonicalizationMethod", "ds:SignatureMethod", "ds:Transform", "ds:DigestMethod"};
				getMainMarker(doc, "ds:Signature", SignatureList );
				getMainMarker(doc, "ds:Signature", Algorithms );
				
				Canonicalize.start( XmlFile, data );
				
			} else {
				System.out.println("1");
				Xades.fw.close();
				System.exit(1);
			}
			
		 } else {
         	System.out.println("1");
         	Xades.fw.close();
         	System.exit(1);
         }

    } catch (Exception e) {
    	System.out.println("5");
    	Xades.fw.close();
    	System.exit(5);
    }
	return data;
    
  }

 /* Wchodzê do "dzia³u" xmla czyli rodzica innych znaczników */
  public static void getMainMarker(Document doc, String MainMarker, String[] MarkersList) throws IOException{
		NodeList nList = doc.getElementsByTagName( MainMarker );
	
		for (int temp = 0; temp < nList.getLength(); temp++) {
			
			Node nNode = nList.item(temp);
	
			Xades.fw.write("\nCurrent Element: " + nNode.getNodeName() + "\n");
	
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	
				Element eElement = (Element) nNode;
	
				int size = MarkersList.length;
		        for (int i=0; i<size; i++) {
		        	getMarker(eElement, MarkersList[i]);
		        }
	
			}
		}
  }  
  
  /* Pobieram znaczniki z xmla */
  public static Map<String, String> getMarker(Element eElement, String marker) throws IOException{
  	try {
  		if (marker == "ds:DigestValueSP") {
  			data.put(marker, eElement.getElementsByTagName( "ds:DigestValue" ).item(1).getTextContent());
  		} else if (marker == "ds:DigestValueC") {
  			data.put(marker, eElement.getElementsByTagName( "ds:DigestValue" ).item(2).getTextContent());
  		} else {
	  		if ( eElement.getElementsByTagName( marker ).item(0).getTextContent() != "" ) {
	  			String value = eElement.getElementsByTagName( marker ).item(0).getTextContent();
	  			Xades.fw.write("\n" + marker + " : " + value);
	  			data.put(marker, value);
	  			
	  		} else {
	  			Xades.fw.write("\n" + marker + " : " + eElement.getElementsByTagName( marker ).item(0).getAttributes().getNamedItem("Algorithm").getNodeValue());
	  		
	  			data.put(marker, eElement.getElementsByTagName( marker ).item(0).getAttributes().getNamedItem("Algorithm").getNodeValue());
	  		
	  		}
  		}
  		
  		try {
  			if ( eElement.getElementsByTagName( "ds:DigestValue" ).item(3).getTextContent() != null ) {
  		  		System.out.println("1");
  		  		Xades.fw.close();
  		  		System.exit(1);
  			}
  		} catch (Exception e) {
  			
  		}
  		
  	} catch (Exception e) {
  		System.out.println("1");
  		Xades.fw.close();
  		System.exit(1);
  	}
  	return data;
  }
  
}