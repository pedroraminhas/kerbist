package pt.ulisboa.tecnico.sdis.kerby;

import java.security.Key;
import java.time.Instant;

public class Ticket {

	private String clientId;
	private String ServerId;
	private java.time.Instant time1;
	private java.time.Instant time2;
	private Key keyXY;
	
	
	/**
	 * @param clientId
	 * @param serverId
	 * @param time1
	 * @param time2
	 * @param keyXY
	 */
	public Ticket(String clientId, String serverId, Instant time1, Instant time2, Key keyXY) {
		super();
		this.clientId = clientId;
		ServerId = serverId;
		this.time1 = time1;
		this.time2 = time2;
		this.keyXY = keyXY;
	}


	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}


	/**
	 * @return the serverId
	 */
	public String getServerId() {
		return ServerId;
	}


	/**
	 * @return the time1
	 */
	public java.time.Instant getTime1() {
		return time1;
	}


	/**
	 * @return the time2
	 */
	public java.time.Instant getTime2() {
		return time2;
	}


	/**
	 * @return the keyXY
	 */
	public Key getKeyXY() {
		return keyXY;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Ticket [clientId=" + clientId + ", ServerId=" + ServerId + ", time1=" + time1 + ", time2=" + time2
				+ ", keyXY=" + keyXY + "]";
	}


	//TODO hashCode and equals
	
}
