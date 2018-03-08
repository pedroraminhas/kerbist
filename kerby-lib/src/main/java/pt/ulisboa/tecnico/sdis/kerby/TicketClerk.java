package pt.ulisboa.tecnico.sdis.kerby;

import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.dateToXML;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Cipher;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

/**
 * Class that handlers Kerberos tickets in different formats.
 * 
 * @author Miguel Pardal
 *
 */
public class TicketClerk {

	// ticket creation -------------------------------------------------------

	/** Create TicketView from arguments. */
	public TicketView newTicketView(String x, String y, Date time1, Date time2, Key key) {
		TicketView ticket = new TicketView();
		ticket.setX(x);
		ticket.setY(y);
		ticket.setTime1(dateToXML(time1));
		ticket.setTime2(dateToXML(time2));
		ticket.setEncodedKeyXY(key.getEncoded());
		return ticket;
	}

	/** Create a textual representation of the TicketView. */
	public String ticketViewToString(TicketView ticket) {
		if (ticket == null)
			return "null";

		StringBuilder builder = new StringBuilder();
		builder.append("TicketView [x=");
		builder.append(ticket.getX());
		builder.append(", y=");
		builder.append(ticket.getY());
		builder.append(", time1=");
		builder.append(ticket.getTime1());
		builder.append(", time2=");
		builder.append(ticket.getTime2());
		builder.append(", encodedKeyXY=");
		builder.append(Arrays.toString(ticket.getEncodedKeyXY()));
		builder.append("]");
		return builder.toString();
	}

	/** Compare contents of two tickets. */
	public boolean ticketViewEquals(TicketView ticket1, TicketView ticket2) {
		if (ticket1 == ticket2)
			return true;
		if (ticket2 == null)
			return false;
		if (ticket1.getClass() != ticket2.getClass())
			return false;
		if (ticket1.getEncodedKeyXY() == null) {
			if (ticket2.getEncodedKeyXY() != null)
				return false;
		} else if (ticket2.getEncodedKeyXY() == null
				|| !Arrays.equals(ticket1.getEncodedKeyXY(), ticket2.getEncodedKeyXY()))
			// compare keys in encoded form
			// uses Arrays.equals to compare array contents
			return false;
		if (ticket1.getTime1() == null) {
			if (ticket2.getTime1() != null)
				return false;
		} else if (!ticket1.getTime1().equals(ticket2.getTime1()))
			return false;
		if (ticket1.getTime2() == null) {
			if (ticket2.getTime2() != null)
				return false;
		} else if (!ticket1.getTime2().equals(ticket2.getTime2()))
			return false;
		if (ticket1.getX() == null) {
			if (ticket2.getX() != null)
				return false;
		} else if (!ticket1.getX().equals(ticket2.getX()))
			return false;
		if (ticket1.getY() == null) {
			if (ticket2.getY() != null)
				return false;
		} else if (!ticket1.getY().equals(ticket2.getY()))
			return false;
		return true;
	}

	// ticket validation -----------------------------------------------------

	/** Validate contents of TicketView. */
	public void validateTicket(TicketView ticket) throws KerbyException {

		// check nulls and empty strings

		if (ticket == null)
			throw new KerbyException("Null ticket!");

		final String x = ticket.getX();
		if (x == null || x.trim().length() == 0)
			throw new KerbyException("X cannot be empty!");

		final String y = ticket.getY();
		if (y == null || y.trim().length() == 0)
			throw new KerbyException("Y cannot be empty!");

		final XMLGregorianCalendar xgc1 = ticket.getTime1();
		if (xgc1 == null)
			throw new KerbyException("Time 1 cannot be empty!");

		final XMLGregorianCalendar xgc2 = ticket.getTime2();
		if (xgc2 == null)
			throw new KerbyException("Time 2 cannot be empty!");

		final byte[] encodedKeyXY = ticket.getEncodedKeyXY();
		if (encodedKeyXY == null)
			throw new KerbyException("Encoded key cannot be empty!");

		// check times
		int result = xgc1.toGregorianCalendar().compareTo(xgc2.toGregorianCalendar());
		// if result is positive, then date1 is "later" than date2
		if (result > 0)
			throw new KerbyException("Time 2 of ticket must be after time 1!");

		// does not check key encoding to avoid having one conversion here at
		// validation, and one later when key is needed for use
	}

	// ticket serialization --------------------------------------------------

	/** Marshal i.e. convert view object to XML result provided by caller. */
	public void viewToXMLResult(TicketView view, Result xmlResult) throws JAXBException {
		// create a JAXBContext
		// JAXBContext jc = JAXBContext.newInstance("pt.ulisboa.tecnico.sdis.kerby");
		JAXBContext jabx = JAXBContext.newInstance(TicketView.class);

		// create XML element (a complex type cannot be instantiated by itself)
		// JAXBElement<TicketView> jaxbElementMarshal = new JAXBElement<>(
		// new QName("http://kerby.sdis.tecnico.ulisboa.pt/", "ticket", "k"),
		// pt.ulisboa.tecnico.sdis.kerby.TicketView.class, ticket);

		// create a Marshaller and marshal
		Marshaller m = jabx.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // indent
		m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE); // omit xml declaration
		// m.marshal(jaxbElementMarshal, System.out);

		// JAXBContext jc = JAXBContext.newInstance(obj.getClass());
		// jabx.createMarshaller().marshal(view, xmlResult);
		m.marshal(view, xmlResult);
	}

	/**
	 * Marshal view object to XML document (in-memory tree, following the Document
	 * Object Model).
	 */
	public Document viewToXMLDoc(TicketView view) throws JAXBException {
		DOMResult domResult = new DOMResult();
		viewToXMLResult(view, domResult);
		return (Document) domResult.getNode();
	}

	/** Marshal view object to a byte array. */
	public byte[] viewToXMLBytes(TicketView view) throws JAXBException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StreamResult streamResult = new StreamResult(baos);
		viewToXMLResult(view, streamResult);
		return baos.toByteArray();
	}

	/** Unmarshal i.e. convert XML source provided by caller to a view object. */
	public TicketView xmlSourceToView(Source xmlSource) throws JAXBException {
		JAXBContext jaxb = JAXBContext.newInstance(TicketView.class);
		Unmarshaller u = jaxb.createUnmarshaller();
		// unmarshal, get element and cast to expected type
		JAXBElement<TicketView> element = (JAXBElement<TicketView>) u.unmarshal(xmlSource, TicketView.class);
		return element.getValue();
	}

	/**
	 * Unmarshal XML document (in-memory tree, following the Document Object Model)
	 * to a view object.
	 */
	public TicketView xmlDocToView(Document document) throws JAXBException {
		DOMSource domSource = new DOMSource(document);
		return xmlSourceToView(domSource);
	}

	/** Unmarshal byte array to a view object. */
	public TicketView xmlBytesToView(byte[] bytes) throws JAXBException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		StreamSource streamSource = new StreamSource(bais);
		return xmlSourceToView(streamSource);
	}

	// ticket sealing --------------------------------------------------------

	public SealedView seal(TicketView view, Key key) throws KerbyException {
		byte[] plainBytes = null;
		try {
			plainBytes = viewToXMLBytes(view);
		} catch (Exception e) {
			throw new KerbyException("Exception while serializing ticket!", e);
		}

		try {
			// TODO extract cipher to constant
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cipherBytes = cipher.doFinal(plainBytes);

			SealedView sealedView = new SealedView();
			sealedView.setCipheredView(cipherBytes);
			return sealedView;

		} catch (Exception e) {
			throw new KerbyException("Exception while ciphering ticket!", e);
		}
	}

	public TicketView unseal(SealedView sealedView, Key key) throws KerbyException {
		byte[] newPlainBytes = null;
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);

			newPlainBytes = cipher.doFinal(sealedView.getCipheredView());
		} catch (Exception e) {
			throw new KerbyException("Exception while deciphering ticket!", e);
		}

		try {
			return xmlBytesToView(newPlainBytes);
			// } catch (KerbyException e) {
			// throw e;
		} catch (Exception e) {
			throw new KerbyException("Exception while deserializing ticket!", e);
		}
	}
}
