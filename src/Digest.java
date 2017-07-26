import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.codec.binary.Base64;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Node;

import Enums.Algorithms;

public class Digest {

	private static FileInputStream fis;
	private static byte[] result;
	private static Node todigest;
	public static byte[] tocompare;

	public static void sha(Map<String, String> data) throws Exception {

		String digest = (String) data.get("ds:DigestValue");
		
        byte[] newDigest = newDigest(new File("podpisywany.xml"), data);
        comparing(newDigest, digest);
		
		digest = (String) data.get("ds:DigestValueSP");
        
		todigest = Parse.doc.getElementsByTagName("xades:SignedProperties").item(0);
		result = serialize( todigest );
        newDigest = newByteDigest( result , data);
        comparing(newDigest, digest);
        
//		digest = (String) data.get("ds:DigestValueC");
//		
//        String cert = data.get("ds:X509Certificate");
//        byte[] wynik = Base64.decodeBase64( cert );
//        newDigest = newByteDigest( wynik , data);
//        comparing( newDigest );
        
		todigest = Parse.doc.getElementsByTagName("ds:SignedInfo").item(0);
		result = serialize( todigest );
        newDigest = newByteDigest( result , data);
        tocompare = newDigest;
	}	
	
    public static byte[] newDigest(File file, Map<String, String> data) throws Exception {

        MessageDigest md =  MessageDigest.getInstance("SHA-1");
		for(Algorithms DigestMethod: Algorithms.values()) {
		    if ( DigestMethod.getURL().equals( data.get("ds:DigestMethod") ) )
		    	MessageDigest.getInstance( DigestMethod.getValue() );
		}
        fis = new FileInputStream(file);
        byte[] dataBytes = new byte[1024];

        int nread = 0;

        while ((nread = fis.read(dataBytes)) != -1) {
          md.update(dataBytes, 0, nread);
        };

        byte[] mdbytes = md.digest();

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
        	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        Xades.fw.write("\nDigest(hex):\t" + sb.toString());
        return mdbytes;
      }
    
    public static byte[] newByteDigest(byte[] text, Map<String, String> data) throws Exception {

        MessageDigest md =  MessageDigest.getInstance("SHA-1");
		for(Algorithms DigestMethod: Algorithms.values()) {
		    if ( DigestMethod.getURL().equals( data.get("ds:DigestMethod") ) )
		    	MessageDigest.getInstance( DigestMethod.getValue() );
		}
        byte[] dataBytes = text;

          md.update(dataBytes);

        byte[] mdbytes = md.digest();

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
        	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        Xades.fw.write("\nDigest(hex):\t" + sb.toString());
        return mdbytes;
      }
    
    public static byte[] serialize(Node todigest) throws TransformerFactoryConfigurationError, TransformerException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLUtils.outputDOM( todigest, baos);
		byte[] result = baos.toByteArray();

		return result; 
    }
    
    public static void comparing (byte[] newDigest, String digest) throws IOException {
    	String wynik = new String(Base64.encodeBase64(newDigest));
        Xades.fw.write("\nDigest(base):\t"+ wynik);
        Xades.fw.write("\nDigest(cmp):\t"+ digest);
		
		if( digest.equals(wynik)) {
			Xades.fw.write( "\nSkroty pasuja" );
		} else {
			Xades.fw.write( "\nBledne skroty" );
			System.out.println( "2" );
			Xades.fw.close();
			System.exit(2);
		}
    }
    
}
