import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class PubKey {

	private static PublicKey pub;
	
	public static java.security.PublicKey generate(String type, Map<String, String> data) throws IOException {
		
		try {
			String mod = data.get("ds:Modulus");
			String exp = data.get("ds:Exponent");
			
			byte[] tmp_mod = Base64.decodeBase64(mod);
			byte[] tmp_exp = Base64.decodeBase64(exp);
			
			BigInteger modulus  = new BigInteger( Hex.encodeHexString( tmp_mod ), 16 );
	        BigInteger exponent = new BigInteger( Hex.encodeHexString( tmp_exp ), 16 );
	
			RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
			KeyFactory factory = KeyFactory.getInstance( type );
			pub = factory.generatePublic(spec);
			
			Xades.fw.write("\n" + pub); 
		
		} catch (Exception e ) {
			System.out.println(4);
			Xades.fw.close();
			System.exit(4);
		}
		
		return pub;
		
	}
	
}