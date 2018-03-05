package pt.ulisboa.tecnico.sdis.kerby;

import java.util.Arrays;
import java.security.Key;

/**
 * Immutable class that represents a ciphered Kerby authenticator.
 * 
 * @author Miguel
 *
 */
public class CipheredAuthenticator {

	private byte[] cipherData;

	/**
	 * @param cipherData
	 */
	public CipheredAuthenticator(byte[] cipherData) {
		super();
		this.cipherData = cipherData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CipheredAuthenticator [cipherData=" + Arrays.toString(cipherData) + "]";
	}

	
	public Authenticator decipher(Key key) throws KerbyException {
		return null;
		// TODO
	}
	
}
