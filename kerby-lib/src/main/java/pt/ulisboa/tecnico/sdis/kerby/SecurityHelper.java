package pt.ulisboa.tecnico.sdis.kerby;

import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.viewToXMLBytes;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.xmlBytesToView;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SecurityHelper {

	public static final String CIPHER_ALGO = "AES";
	public static final int CIPHER_KEY_SIZE = 128;

	// keys ------------------------------------------------------------------

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

	// ciphers ---------------------------------------------------------------

	private static Cipher initCipher(int opmode, Key key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
		cipher.init(opmode, key);
		return cipher;
	}

	public static Cipher initCipher(Key key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		return initCipher(Cipher.ENCRYPT_MODE, key);
	}

	public static Cipher initDecipher(Key key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		return initCipher(Cipher.DECRYPT_MODE, key);
	}

	// sealed views ----------------------------------------------------------

	public static <V> SealedView seal(Class<V> viewClass, V view, Key key) throws KerbyException {
		byte[] plainBytes = null;
		try {
			plainBytes = viewToXMLBytes(viewClass, view);
		} catch (Exception e) {
			throw new KerbyException("Exception while serializing view!", e);
		}

		try {
			Cipher cipher = initCipher(key);
			byte[] cipherBytes = cipher.doFinal(plainBytes);

			SealedView sealedView = new SealedView();
			sealedView.setData(cipherBytes);
			return sealedView;

		} catch (Exception e) {
			throw new KerbyException("Exception while sealing ticket!", e);
		}
	}

	public static <V> V unseal(Class<V> viewClass, SealedView sealedView, Key key) throws KerbyException {
		byte[] newPlainBytes = null;
		try {
			Cipher decipher = initDecipher(key);
			newPlainBytes = decipher.doFinal(sealedView.getData());
		} catch (Exception e) {
			throw new KerbyException("Exception while deciphering ticket!", e);
		}

		try {
			return xmlBytesToView(viewClass, newPlainBytes);
		} catch (Exception e) {
			throw new KerbyException("Exception while deserializing ticket!", e);
		}
	}

}
