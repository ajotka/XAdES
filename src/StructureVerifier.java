import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class StructureVerifier {
	
	public static boolean checkStructure(String path) throws ParserConfigurationException, TransformerException, SAXException, IOException{

		Source xmlFile = new StreamSource(new File(path));
		try {
			File schemaFile = new File("xades.xsd");
			
            String signature = XMLToString.getStringFromFile(path);
            signature = signature.substring(signature.indexOf("<ds:Signature"),
            			signature.indexOf("</ds:Signature>")+15);
            
            File f = new File("tmp.xml");
            FileOutputStream out = new FileOutputStream("tmp.xml");
            out.write(signature.getBytes());
            out.close();
            xmlFile = new StreamSource(f);          
            
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
			Xades.fw.write( "\n" + xmlFile.getSystemId() + " is valid" );
			return true;
			
		} catch (SAXException e) {
			Xades.fw.write( "\n" + xmlFile.getSystemId() + " is NOT valid.\nThe reason:" + e);
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}

