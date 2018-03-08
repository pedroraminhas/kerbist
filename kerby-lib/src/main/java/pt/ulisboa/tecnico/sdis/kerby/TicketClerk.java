package pt.ulisboa.tecnico.sdis.kerby;

import static pt.ulisboa.tecnico.sdis.kerby.SecurityHelper.seal;
import static pt.ulisboa.tecnico.sdis.kerby.SecurityHelper.unseal;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.dateToXML;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.viewToXML;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.viewToXMLBytes;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.xmlBytesToView;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.xmlNodeToView;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.w3c.dom.Node;

/**
 * Class that handles Kerberos tickets in different formats.
 * 
 * @author Miguel Pardal
 *
 */
public class TicketClerk {

	// ticket creation -------------------------------------------------------

	/** Create TicketView from arguments. */
	public TicketView ticketBuild(String x, String y, Date time1, Date time2, Key key) {
		TicketView ticket = new TicketView();
		ticket.setX(x);
		ticket.setY(y);
		ticket.setTime1(dateToXML(time1));
		ticket.setTime2(dateToXML(time2));
		ticket.setEncodedKeyXY(key.getEncoded());
		return ticket;
	}

	/** Create a textual representation of the TicketView. */
	public String ticketToString(TicketView ticket) {
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
	public boolean ticketEquals(TicketView ticket1, TicketView ticket2) {
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
	public void ticketValidate(TicketView ticket) throws KerbyException {

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

	/**
	 * Marshal ticket to XML document.
	 */
	public Node ticketToXMLNode(TicketView view, String ticketTagName) throws JAXBException {
		return viewToXML(TicketView.class, view, new QName(ticketTagName));
	}

	/** Marshal ticket to XML bytes. */
	public byte[] ticketToXMLBytes(TicketView view, String ticketTagName) throws JAXBException {
		return viewToXMLBytes(TicketView.class, view, new QName(ticketTagName));
	}

	/**
	 * Unmarshal ticket from XML document.
	 */
	public TicketView ticketFromXMLNode(Node xml) throws JAXBException {
		return xmlNodeToView(TicketView.class, xml);
	}

	/** Unmarshal byte array to a view object. */
	public TicketView ticketFromXMLBytes(byte[] bytes) throws JAXBException {
		return xmlBytesToView(TicketView.class, bytes);
	}

	// ticket sealing --------------------------------------------------------

	public SealedView ticketSeal(TicketView view, Key key) throws KerbyException {
		return seal(TicketView.class, view, key);
	}

	public TicketView ticketUnseal(SealedView sealedView, Key key) throws KerbyException {
		return unseal(TicketView.class, sealedView, key);
	}

}
