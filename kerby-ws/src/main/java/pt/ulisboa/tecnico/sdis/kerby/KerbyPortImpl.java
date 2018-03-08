package pt.ulisboa.tecnico.sdis.kerby;

import javax.jws.WebService;

/**
 * Kerby Web Service implementation class.
 * 
 * @author Miguel Pardal
 *
 */
@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.kerby.KerbyPortType",
wsdlLocation = "KerbyService.wsdl",
name ="KerbyService",
portName = "KerbyPort",
targetNamespace="http://kerby.sdis.tecnico.ulisboa.pt/",
serviceName = "KerbyService"
)
public class KerbyPortImpl implements KerbyPortType {

	// end point manager
	private KerbyEndpointManager endpointManager;

	public KerbyPortImpl(KerbyEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	@Override
	public SessionKeyAndTicketView requestTicket(String client, String server, long nounce) throws BadTicketRequest_Exception {
		return null;
	}

	@Override
	public String dummy(AuthView arg0, RequestTimeView arg1, SealedView arg2, SessionKeyView arg3, TicketView arg4) {
		return this.endpointManager.getWsName() + " up-and-running";
	}

}
