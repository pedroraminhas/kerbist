package pt.ulisboa.tecnico.sdis.kerby;

import java.io.Serializable;
import java.security.Key;

public class TicketView implements Serializable {

	private static final long serialVersionUID = 1L;

	private String x;
	private String y;
	private java.time.Instant time1;
	private java.time.Instant time2;
	private Key keyXY;

	/**
	 * @return the x
	 */
	public String getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(String x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public String getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(String y) {
		this.y = y;
	}

	/**
	 * @return the time1
	 */
	public java.time.Instant getTime1() {
		return time1;
	}

	/**
	 * @param time1
	 *            the time1 to set
	 */
	public void setTime1(java.time.Instant time1) {
		this.time1 = time1;
	}

	/**
	 * @return the time2
	 */
	public java.time.Instant getTime2() {
		return time2;
	}

	/**
	 * @param time2
	 *            the time2 to set
	 */
	public void setTime2(java.time.Instant time2) {
		this.time2 = time2;
	}

	/**
	 * @return the keyXY
	 */
	public Key getKeyXY() {
		return keyXY;
	}

	/**
	 * @param keyXY
	 *            the keyXY to set
	 */
	public void setKeyXY(Key keyXY) {
		this.keyXY = keyXY;
	}

}
