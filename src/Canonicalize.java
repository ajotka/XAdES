import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
 
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import Enums.Algorithms;
 
public class Canonicalize
{

	static byte[] canonicalize(byte[] xmlBytes, Map<String, String> data) throws CanonicalizationException, ParserConfigurationException, IOException, SAXException, InvalidCanonicalizerException{
		
		Canonicalizer c14n = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
		
		for(Algorithms Canonicalization: Algorithms.values()) {
		    if ( Canonicalization.getURL().equals( data.get("ds:CanonicalizationMethod") ) )
		    	c14n = Canonicalizer.getInstance( Canonicalization.getValue() );	
		}

		byte[] canonXmlBytes = c14n.canonicalize(xmlBytes);
        return canonXmlBytes;
        //String canonXmlString = new String(canonXmlBytes);
    }
   
    public static void start(File xades, Map<String, String> data) throws Exception{

        org.apache.xml.security.Init.init();
 
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	dbFactory.setIgnoringComments(true);
    	dbFactory.setIgnoringElementContentWhitespace(false);
    	dbFactory.setValidating(false);
    	dbFactory.setNamespaceAware(true);
    	Document doc1 = dbFactory.newDocumentBuilder().parse( xades );
        //cut file to only "tosign" file

    	Element element = (Element) doc1.getElementsByTagName("ds:Signature").item(0);
    	        element.getParentNode().removeChild(element);
    	        doc1.normalize();
   	
    	
        byte[] fileBytes = documentToByte(doc1);
        byte[] canonFileBytes = canonicalize(fileBytes, data);
        
        FileOutputStream fos = new FileOutputStream("podpisywany.xml");
        fos.write(canonFileBytes);
        fos.close();
        
        //show "tosign" file
        //System.out.println(new String(canonFileBytes));

    }
    public static byte[] documentToByte(Document document)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.xml.security.utils.XMLUtils.outputDOM(document, baos, true);
        return baos.toByteArray();
    } 
 
}