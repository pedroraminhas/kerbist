package pt.ulisboa.tecnico.sdis.kerby;

import java.security.Key;
import java.util.Arrays;

/**
 * Immutable class that represents a ciphered Kerbereos authenticator response.
 * 
 * @author Miguel Pardal
 *
 */
public class CipheredAuthenticatorResponse {

	private byte[] cipherData;

	/**
	 * @param cipherData
	 */
	public CipheredAuthenticatorResponse(byte[] cipherData) {
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
		return "CipheredAuthenticatorResponse [cipherData=" + Arrays.toString(cipherData) + "]";
	}

	public AuthenticatorResponse decipher(Key key) throws KerbyException {
		return null;
		// TODO
	}

}
