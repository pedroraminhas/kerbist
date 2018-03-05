package pt.ulisboa.tecnico.sdis.kerby;

import javax.jws.WebService;

/**
 * Kerby Web Service implementation class.
 * 
 * @author Miguel Pardal
 *
 */
@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.kerby.KerbyPortType")
public class KerbyPortImpl implements KerbyPortType {

	// end point manager
	private KerbyEndpointManager endpointManager;

	public KerbyPortImpl(KerbyEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	@Override
	public TicketResponse requestTicket(String client, String server, long nounce) throws BadTicketRequest {
		// TODO Auto-generated method stub
		return null;
	}

}
