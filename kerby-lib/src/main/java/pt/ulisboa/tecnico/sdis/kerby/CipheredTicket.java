package pt.ulisboa.tecnico.sdis.kerby;

import java.io.ByteArrayInputStream;
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
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Immutable class that represents a ciphered Kerberos ticket.
 * 
 * @author Miguel Pardal
 *
 */
public class CipheredTicket {

	private byte[] cipherData;

	/**
	 * @param cipherData
	 */
	public CipheredTicket(byte[] cipherData) {
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
		return "CipheredTicket [cipherData=" + Arrays.toString(cipherData) + "]";
	}

	public Ticket decipher(Key key) throws KerbyException {
		byte[] newPlainBytes = null;
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);

			newPlainBytes = cipher.doFinal(this.cipherData);
		} catch (Exception e) {
			throw new KerbyException("Exception while deciphering ticket!", e);
		}

		try {
			return deserialize(newPlainBytes);
		} catch (KerbyException e) {
			throw e;
		} catch (Exception e) {
			throw new KerbyException("Exception while deserializing ticket!", e);
		}
	}

	protected Ticket deserialize(byte[] bytes) throws ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException, KerbyException {

		// Create DocumentBuilder instance.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		transformer.transform(new StreamSource(bais), new DOMResult(document));

		// TODO validate schema ?
		
		Element xElement = XMLHelper.getDirectChild(document, "x");
		String xText = xElement.getTextContent();
		// TODO check null, empty, etc

		Element yElement = XMLHelper.getDirectChild(document, "y");
		String yText = yElement.getTextContent();
		// TODO check null, empty, etc

		// TODO times and key

		// TODO fix times and key
		return new Ticket(xText, yText, Instant.now(), Instant.now().plusSeconds(10), null);
	}

}
