package pt.ulisboa.tecnico.sdis.kerby;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class SecurityHelper {

	public static final String CIPHER_ALGO = "AES";
	public static final int CIPHER_KEY_SIZE = 128;

	public static Key generateKey() throws NoSuchAlgorithmException {
		return generateKey(CIPHER_ALGO, CIPHER_KEY_SIZE);
	}

	private static Key generateKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
		keyGen.init(keySize);
		Key key = keyGen.generateKey();
		return key;
	}

	public static Key recodeKey(byte[] encodedKey) {
		return new SecretKeySpec(encodedKey, CIPHER_ALGO);
	}

}
