import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class X509 {
	
	private static byte[] sig;
	private static byte[] cert;
	private static byte[] sigValbytes;
	private static Cipher cipher;

	public static void read(Map<String, String> data, PublicKey key) throws Exception {
		
		try {
			String certificate = data.get("ds:X509Certificate");
			String signature = data.get("ds:SignatureValue");
			
			cert = Base64.decodeBase64(certificate);
			sigValbytes = Base64.decodeBase64(signature);
			
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream in = new ByteArrayInputStream(cert);
		    java.security.cert.Certificate c = cf.generateCertificate(in);
		    in.close();
	
		    X509Certificate t = (X509Certificate) c;
		    Xades.fw.write("\nVersion " + t.getVersion());
		    Xades.fw.write("\nSerial number " + t.getSerialNumber().toString(16));
		    Xades.fw.write("\nSubject " + t.getSubjectDN());
		    Xades.fw.write("\nIssuer " + t.getIssuerDN());
		    Xades.fw.write("\nBefore " + t.getNotBefore());
		    Xades.fw.write("\nAfter " + t.getNotAfter());
		    Xades.fw.write("\nAlgorithm " + t.getSigAlgName());
			
		    sig = t.getSignature();
			
		    //weryfikacja klucza publicznego
		    PublicKey pk = t.getPublicKey();
	
		    int tmp = 0;
		    byte[] pkenc = pk.getEncoded();
		    byte[] pkcmp = key.getEncoded();
		    for (int i = 0; i < pkenc.length; i++) {
		      if(pkenc[i] == pkcmp[i]) {
		    	  tmp++;
		      }
		    }	    
		    if ( tmp == pkenc.length ) {
		    	Xades.fw.write("\nKlucze publiczne sie zgadzaja\n");
		    	
		    	Signature signature1 = Signature.getInstance("SHA1withRSA");
		    	try {
			    	signature1.initVerify( pk );
			    	signature1.initVerify( key );
		    	} catch (Exception e) {
			    	System.out.println( "4" );
			    	Xades.fw.close();
			    	System.exit(4);
		    	}
		    	
		    } else {
		    	System.out.println( "4" );
		    	Xades.fw.close();
		    	System.exit(4);
		    }

		    //Podpis - signature
		    
		    cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    cipher.init(Cipher.DECRYPT_MODE, key);
		    sig = cipher.doFinal(sig);
		    
		    
		} catch (Exception e) {
			System.out.println("4");
			Xades.fw.close();
			System.exit(4);
		}
		
		try{
		    //zdekryptowany SignatureValue
	        byte[] decryptedsigVal = cipher.doFinal(sigValbytes);
	        
			String digest = (String) data.get("ds:DigestValueC");
			
	        String cert = data.get("ds:X509Certificate");
	        byte[] wynik = Base64.decodeBase64( cert );
	        byte[] newDigest = Digest.newByteDigest( wynik , data);
	        Digest.comparing( newDigest, digest );
	        
	        Xades.fw.write("\nSignature:\n" + new BigInteger(decryptedsigVal).toString(16));
	        String asn = new BigInteger(decryptedsigVal).toString(16);
	        asn = asn.substring(asn.length()-39);
	        Xades.fw.write("\n" + asn );
	        Xades.fw.write("\n" + new BigInteger(Digest.tocompare).toString(16) );
		    
	        String tocompare = new BigInteger(Digest.tocompare).toString(16);
	
		    if ( asn.equals( tocompare ) ) {
		    	System.out.println("0");
		    } else {
		    	System.out.println("3");
		    	Xades.fw.close();
		    	System.exit(3);
		    }
		} catch (Exception e) {
			System.out.println("3");
			Xades.fw.close();
			System.exit(3);
		}
	}
}
