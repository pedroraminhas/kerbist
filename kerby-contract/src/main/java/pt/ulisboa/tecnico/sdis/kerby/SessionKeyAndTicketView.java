package pt.ulisboa.tecnico.sdis.kerby;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement // to add element declaration to schema
public class SessionKeyAndTicketView implements Serializable {

	private static final long serialVersionUID = 1L;

	private SealedView sessionKey;
	private SealedView ticket;

	public SealedView getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(SealedView sessionKey) {
		this.sessionKey = sessionKey;
	}

	public SealedView getTicket() {
		return ticket;
	}

	public void setTicket(SealedView ticket) {
		this.ticket = ticket;
	}

}
