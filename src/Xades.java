import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Xades {

	public static File out;
	public static FileWriter fw;
	public static File fXmlFile;
	public static String path;

	public static void main(String[] args) throws IOException {
		try {
			
	        if(args.length > 0) {
	            fXmlFile = new File( args[0] );
	            path = args[0];
	        } else {
				System.out.println("5");
				Xades.fw.close();
				System.exit(5);
	        }
			
			out = new File("log.txt");
			fw = new FileWriter( out );
			
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	        Date today = Calendar.getInstance().getTime();
	        String reportDate = df.format(today);
			fw.write("=========================\n" + "Verification time: " + reportDate + "\n");
			
			Map<String, String> data = new HashMap<String, String>();
			
			fw.write("\n----------------------------------------\nXML PARSE\n----------------------------------------");
			data = Parse.parseXML(fXmlFile);
			fw.write("\n----------------------------------------\nCHECK DIGEST\n----------------------------------------");
			Digest.sha(data);
			fw.write("\n----------------------------------------\nGENERATE PUBLIC KEY\n----------------------------------------");
			PublicKey key = PubKey.generate("RSA", data);
			fw.write("\n----------------------------------------\nCHECK SIGNATURE\n----------------------------------------");
			X509.read(data, key);
			
			fw.write("\nEND\n");
			fw.close();
			
		} catch(Exception e) {
			System.out.println("5");
			fw.close();
			System.exit(5);
		}

	}

}
