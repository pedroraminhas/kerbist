package pt.ulisboa.tecnico.sdis.kerby;

import java.security.Key;
import java.util.Arrays;

public class CipheredTicket {

	private byte[] cipherData;

	/**
	 * @param cipherData
	 */
	public CipheredTicket(byte[] cipherData) {
		super();
		this.cipherData = cipherData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CipheredTicket [cipherData=" + Arrays.toString(cipherData) + "]";
	}

	
	public Ticket decipher(Key key) throws KerbyException {
		return null;
		// TODO
	}

}
