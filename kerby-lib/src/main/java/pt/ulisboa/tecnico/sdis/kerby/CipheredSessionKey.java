package pt.ulisboa.tecnico.sdis.kerby;

import java.security.Key;
import java.util.Arrays;

/**
 * Immutable class that represents a ciphered Kerberos session key.
 * 
 * @author Miguel Pardal
 *
 */
public class CipheredSessionKey {

	private byte[] cipherData;

	/**
	 * @param cipherData
	 */
	public CipheredSessionKey(byte[] cipherData) {
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
		return "CipheredSessionKey [cipherData=" + Arrays.toString(cipherData) + "]";
	}

	public SessionKey decipher(Key key) throws KerbyException {
		return null;
		// TODO
	}

}
