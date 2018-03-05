package pt.ulisboa.tecnico.sdis.kerby;

import java.security.Key;
import java.util.Arrays;

/**
 * Immutable class that represents a ciphered Kerberos authenticator.
 * 
 * @author Miguel Pardal
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

	/*
	 * (non-Javadoc)
	 * 
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
