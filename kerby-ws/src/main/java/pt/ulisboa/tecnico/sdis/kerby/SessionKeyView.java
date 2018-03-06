package pt.ulisboa.tecnico.sdis.kerby;

import java.io.Serializable;
import java.security.Key;

public class SessionKeyView implements Serializable {

	private static final long serialVersionUID = 1L;

	private Key keyXY;
	private long nounce;

	public Key getKeyXY() {
		return keyXY;
	}

	public void setKeyXY(Key keyXY) {
		this.keyXY = keyXY;
	}

	public long getNounce() {
		return nounce;
	}

	public void setNounce(long nounce) {
		this.nounce = nounce;
	}

}
