package pt.ulisboa.tecnico.sdis.kerby;

import java.security.Key;
import java.time.Instant;

public class Authenticator {

	private String x;
	private java.time.Instant timeRequest;

	/**
	 * @param clientId
	 * @param timeRequest
	 */
	public Authenticator(String clientId, Instant timeRequest) {
		super();
		this.x = clientId;
		this.timeRequest = timeRequest;
	}

	/**
	 * @return the identifier of x
	 */
	public String getX() {
		return x;
	}

	/**
	 * @return the timeRequest
	 */
	public java.time.Instant getTimeRequest() {
		return timeRequest;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Authenticator [clientId=" + x + ", timeRequest=" + timeRequest + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((timeRequest == null) ? 0 : timeRequest.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Authenticator other = (Authenticator) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (timeRequest == null) {
			if (other.timeRequest != null)
				return false;
		} else if (!timeRequest.equals(other.timeRequest))
			return false;
		return true;
	}

	
	public CipheredAuthenticator cipher(Key key) throws KerbyException {
		return null;
		// TODO
	}

	
}
