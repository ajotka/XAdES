import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

public class XMLToString {
	public static void main(String [] args){}
	
	
	public static String getStringFromFile(String path) throws SAXException, IOException, ParserConfigurationException, TransformerException{
	
	File fXmlFile 		= new File(path);
	byte[] fileBytes    = new byte[(int) fXmlFile.length()];
	
	FileInputStream fis = null;
	try {
		fis = new FileInputStream( fXmlFile );
		fis.read(fileBytes);
		fis.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	String output = new String(fileBytes,StandardCharsets.UTF_8); 
	
	return output;
	}
	
}
