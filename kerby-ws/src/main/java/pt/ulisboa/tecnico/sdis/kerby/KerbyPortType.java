package pt.ulisboa.tecnico.sdis.kerby;

import javax.jws.WebService;

@WebService
public interface KerbyPortType {

	String sayHello(String name);

}
