package pt.ulisboa.tecnico.sdis.kerby.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.Key;
import java.security.SecureRandom;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.kerby.SealedView;
import pt.ulisboa.tecnico.sdis.kerby.SecurityHelper;
import pt.ulisboa.tecnico.sdis.kerby.SessionKey;
import pt.ulisboa.tecnico.sdis.kerby.SessionKeyAndTicketView;
import pt.ulisboa.tecnico.sdis.kerby.SessionKeyView;
import pt.ulisboa.tecnico.sdis.kerby.Ticket;
import pt.ulisboa.tecnico.sdis.kerby.TicketView;


/**
 * Test suite
 */
public class RequestTicketIT extends BaseIT {
	
	private static SecureRandom randomGenerator = new SecureRandom();

	@Test
	public void testValidRequest() throws Exception {
		final String clientName = "kerbyclient1";
		final String clientPass = "password1";
		final String serverName = "kerbyserver1";
		final String serverPass = "password2";
		final Key clientKey = SecurityHelper.generateKeyFromPassword(clientPass, clientName);
		final Key serverKey = SecurityHelper.generateKeyFromPassword(serverPass, serverName);
		long nounce = randomGenerator.nextLong();
		int duration = 30;
		
		SessionKeyAndTicketView result = client.requestTicket(clientName, serverName, nounce, duration);
		
		SealedView sealedSessionKey = result.getSessionKey();
		SealedView sealedTicket = result.getTicket();
		
		SessionKey sessionKey = new SessionKey(SecurityHelper.unseal(SessionKeyView.class, sealedSessionKey, clientKey));
		
		Ticket ticket = new Ticket(SecurityHelper.unseal(TicketView.class, sealedTicket, serverKey));
		long timeDiff = ticket.getTime2().getTime() - ticket.getTime1().getTime();
		
		
		assertEquals(nounce, sessionKey.getNounce());
		assertNotNull(sessionKey.getKeyXY());
		assertEquals(clientName, ticket.getX());
		assertEquals(serverName, ticket.getY());
		assertEquals((long) duration * 1000, timeDiff);
		assertNotNull(ticket.getKeyXY());
		assertEquals(sessionKey.getKeyXY(), ticket.getKeyXY());
		
		/* System.out.println(ticket.toString());
		   System.out.println(sessionKey.toString()); */
	}

}
