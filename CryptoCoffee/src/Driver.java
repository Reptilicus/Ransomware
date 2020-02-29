import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Driver {
	public static void main(String args[]) {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
