package pt.ulisboa.tecnico.sdis.kerby;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.time.Instant;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Immutable class that represents a Kerberos ticket.
 * 
 * @author Miguel Pardal
 *
 */
public class Ticket {

	private String x;
	private String y;
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
	public Ticket(String x, String y, Instant time1, Instant time2, Key keyXY) throws KerbyException {
		super();

		// TODO check nulls, empty strings
		this.x = x;
		this.y = y;

		if (time1.isAfter(time2))
			throw new KerbyException("Time 2 of ticket must be after time 1!");
		this.time1 = time1;
		this.time2 = time2;

		// TODO check nulls, supported algorithms ?
		this.keyXY = keyXY;
	}

	/**
	 * @return the identifier of x
	 */
	public String getX() {
		return x;
	}

	/**
	 * @return the identifier of y
	 */
	public String getY() {
		return y;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Ticket [clientId=" + x + ", ServerId=" + y + ", time1=" + time1 + ", time2=" + time2 + ", keyXY="
				+ keyXY + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyXY == null) ? 0 : keyXY.hashCode());
		result = prime * result + ((time1 == null) ? 0 : time1.hashCode());
		result = prime * result + ((time2 == null) ? 0 : time2.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		Ticket other = (Ticket) obj;
		if (keyXY == null) {
			if (other.keyXY != null)
				return false;
		} else if (other.keyXY == null || !Arrays.equals(keyXY.getEncoded(), other.keyXY.getEncoded()))
			// compare keys in encoded form
			// uses Arrays.equals to compare array contents
			return false;
		if (time1 == null) {
			if (other.time1 != null)
				return false;
		} else if (!time1.equals(other.time1))
			return false;
		if (time2 == null) {
			if (other.time2 != null)
				return false;
		} else if (!time2.equals(other.time2))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}

	public CipheredTicket cipher(Key key) throws KerbyException {
		byte[] plainBytes = null;
		try {
			plainBytes = serialize();
		} catch (Exception e) {
			throw new KerbyException("Exception while serializing ticket!", e);
		}

		try {
			// TODO extract cipher to constant
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cipherBytes = cipher.doFinal(plainBytes);

			return new CipheredTicket(cipherBytes);

		} catch (Exception e) {
			throw new KerbyException("Exception while ciphering ticket!", e);
		}
	}

	/**
	 * create serialized version of ticket
	 * 
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	protected byte[] serialize() throws ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException {
		// Create DocumentBuilder instance.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputStream templateStream = Ticket.class.getResourceAsStream("/ticket.xml");
		Document document = builder.parse(templateStream);

		{
			Element element = XMLHelper.getDirectChild(document, "x");
			element.setTextContent(this.x);
		}
		{
			Element element = XMLHelper.getDirectChild(document, "y");
			element.setTextContent(this.y);
		}

		// TODO times and key

		// use transformer to print XML document from memory
		Transformer transformer = TransformerFactory.newInstance().newTransformer();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		transformer.transform(new DOMSource(document), new StreamResult(baos));

		return baos.toByteArray();
	}

}
