package pt.ulisboa.tecnico.sdis.kerby;

import java.security.Key;

/**
 * Immutable class that represents a Kerberos session key with accompanying
 * nounce.
 * 
 * @author Miguel Pardal
 *
 */
public class SessionKey {

	private Key keyXY;
	private long nounce;

	/**
	 * @param keyXY
	 * @param nounce
	 */
	public SessionKey(Key keyXY, long nounce) {
		super();
		this.keyXY = keyXY;
		this.nounce = nounce;
	}

	/**
	 * @return the keyXY
	 */
	public Key getKeyXY() {
		return keyXY;
	}

	/**
	 * @return the nounce
	 */
	public long getNounce() {
		return nounce;
	}

}
