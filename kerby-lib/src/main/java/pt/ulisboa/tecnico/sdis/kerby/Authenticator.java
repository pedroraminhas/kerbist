package pt.ulisboa.tecnico.sdis.kerby;

import java.security.Key;
import java.time.Instant;

public class Authenticator {

	private String clientId;
	private java.time.Instant timeRequest;

	/**
	 * @param clientId
	 * @param timeRequest
	 */
	public Authenticator(String clientId, Instant timeRequest) {
		super();
		this.clientId = clientId;
		this.timeRequest = timeRequest;
	}

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
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
		return "Authenticator [clientId=" + clientId + ", timeRequest=" + timeRequest + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
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
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (timeRequest == null) {
			if (other.timeRequest != null)
				return false;
		} else if (!timeRequest.equals(other.timeRequest))
			return false;
		return true;
	}
	
}
