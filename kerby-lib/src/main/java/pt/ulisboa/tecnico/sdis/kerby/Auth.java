package pt.ulisboa.tecnico.sdis.kerby;

import static pt.ulisboa.tecnico.sdis.kerby.SecurityHelper.seal;
import static pt.ulisboa.tecnico.sdis.kerby.SecurityHelper.unseal;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.dateToXML;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.viewToXML;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.viewToXMLBytes;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.xmlBytesToView;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.xmlNodeToView;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.xmlToDate;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.w3c.dom.Node;
/**
 * Class that represents a Kerberos auth and can use different data formats.
 * 
 * @author Miguel Amaral
 *
 */
public class Auth {

	/** Ticket data container. After creation, cannot be null. */
	private AuthView view;

	// Auth creation -------------------------------------------------------

	/** Create AuthView from arguments. */
	public AuthView authBuild(String x, Date timeRequest) {
		AuthView auth= new AuthView();
		auth.setTimeRequest(dateToXML(timeRequest));
		auth.setX(x);
		return auth;
	}
	
	/** Create a default view */
	//public Auth() {
	//	AuthView view = new AuthView();
	//	view.setTimeRequest(dateToXML(new Date()));
	//	view.setX("X");
	//	setView(view);
	//}
	
	/** Create ticket from data view. */
	public Auth(AuthView view) {
		setView(view);
	}

	// After construction, view can never be null, and can never be set to null.
	// This invariant is assumed to be true in the remaining code.

	// accessors -------------------------------------------------------------

	protected AuthView getView() {
		return view;
	}

	protected void setView(AuthView view) {
		if (view == null)
			throw new IllegalArgumentException("View cannot be null!");
		this.view = view;
	}

	public String getX() {
		return view.getX();
	}

	public void setX(String x) {
		view.setX(x);
	}

	public Date getTimeRequest() {
		Date time = xmlToDate(view.getTimeRequest());
		return time;
	}

	public void setTimeRequest(Date timeRequest) {
		XMLGregorianCalendar xgc = dateToXML(timeRequest);
		view.setTimeRequest(xgc);
	}

	// object methods --------------------------------------------------------

	/** Create a textual representation of the AuthView. */
	public String authToString() {
		if (view == null) {
			return "null";
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("AuthView [x=");
		builder.append(view.getX());
		builder.append(", timeRequest=");
		builder.append(view.getTimeRequest());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((view.getTimeRequest() == null) ? 0 : view.getTimeRequest().hashCode());
		result = prime * result + ((view.getX() == null) ? 0 : view.getX().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Auth other = (Auth) obj;
		AuthView otherView = other.getView();

		return authViewEquals(view, otherView);
	}

	/** Compare contents of two auth views. */
	protected boolean authViewEquals(AuthView view1, AuthView view2) {
		if (view1 == view2)
			return true;
		if (view2 == null)
			return false;
		if (view1.getClass() != view2.getClass())
			return false;
		if (view1.getTimeRequest() == null) {
			if (view2.getTimeRequest() != null)
				return false;
		} else if (!view1.getTimeRequest().equals(view2.getTimeRequest()))
			return false;
		if (view1.getX() == null) {
			if (view2.getX() != null)
				return false;
		} else if (!view1.getX().equals(view2.getX()))
			return false;
		return true;
	}

	// ticket validation -----------------------------------------------------

	/** Validate contents of TicketView. */
	public void validate() throws KerbyException {

		// check nulls and empty strings
		if (view == null)
			throw new KerbyException("Null ticket!");

		final String x = view.getX();
		if (x == null || x.trim().length() == 0)
			throw new KerbyException("X cannot be empty!");

		final XMLGregorianCalendar xgc1 = view.getTimeRequest();
		if (xgc1 == null)
			throw new KerbyException("Time 1 cannot be empty!");
	}

	// XML serialization -----------------------------------------------------

	/**
	 * Marshal ticket to XML document.
	 */
	public Node toXMLNode(String ticketTagName) throws JAXBException {
		return viewToXML(AuthView.class, view, new QName(ticketTagName));
	}

	/** Marshal ticket to XML bytes. */
	public byte[] toXMLBytes(String ticketTagName) throws JAXBException {
		return viewToXMLBytes(AuthView.class, view, new QName(ticketTagName));
	}

	/**
	 * Unmarshal ticket from XML document.
	 */
	public void fromXMLNode(Node xml) throws JAXBException {
		AuthView view= xmlNodeToView(AuthView.class, xml);
		// set view should not allow null
		setView(view);
	}

	/** Unmarshal byte array to a view object. */
	public void fromXMLBytes(byte[] bytes) throws JAXBException {
		AuthView view = xmlBytesToView(AuthView.class, bytes);
		// set view should not allow null
		setView(view);
	}

	// sealing ---------------------------------------------------------------

	public SealedView seal(Key key) throws KerbyException {
		return SecurityHelper.seal(AuthView.class, view, key);
	}

	public void unseal(SealedView sealedView, Key key) throws KerbyException {
		AuthView view = SecurityHelper.unseal(AuthView.class, sealedView, key);
		// set view should not allow null
		setView(view);
	}

}