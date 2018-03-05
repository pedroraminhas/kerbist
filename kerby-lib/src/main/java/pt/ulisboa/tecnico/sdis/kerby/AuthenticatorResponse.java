package pt.ulisboa.tecnico.sdis.kerby;

import java.time.Instant;

/**
 * Immutable class that represents a Kerberos authenticator response.
 * 
 * @author Miguel Pardal
 *
 */
public class AuthenticatorResponse {

	private java.time.Instant timeRequest;

	/**
	 * @param timeRequest
	 */
	public AuthenticatorResponse(Instant timeRequest) {
		super();
		this.timeRequest = timeRequest;
	}

	/**
	 * @return the timeRequest
	 */
	public java.time.Instant getTimeRequest() {
		return timeRequest;
	}

}
