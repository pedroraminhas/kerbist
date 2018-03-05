package pt.ulisboa.tecnico.sdis.kerby;

import javax.jws.WebService;


@WebService(endpointInterface="pt.ulisboa.tecnico.sdis.kerby.KerbyPortType")
public class KerbyPortImpl implements KerbyPortType {

	// end point manager
	private KerbyEndpointManager endpointManager;

	public KerbyPortImpl(KerbyEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	public String sayHello(String name) {
		return "Hello " + name + "!";
	}

}
