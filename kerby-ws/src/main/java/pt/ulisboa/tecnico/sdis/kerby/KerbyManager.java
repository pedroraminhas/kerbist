package pt.ulisboa.tecnico.sdis.kerby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KerbyManager {
	
	private static final String PASSWORDS_FILENAME = "/passwords.txt";
	private static final int MIN_TICKET_DURATION = 10;
	private static final int MAX_TICKET_DURATION = 300;
	private static Set<Long> previousNounces = Collections.synchronizedSet(new HashSet<Long>());
	private static ConcurrentHashMap<String, String> passwords = new ConcurrentHashMap<String, String>();
	
	// Singleton -------------------------------------------------------------
	private KerbyManager() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final KerbyManager INSTANCE = new KerbyManager();
	}

	public static synchronized KerbyManager getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public SessionKeyAndTicketView requestTicket(String client, String server, long nounce, int ticketDuration) 
			throws BadTicketRequestException {
		
		/* Validate parameters */
		if(client == null || client.trim().isEmpty())
			throw new BadTicketRequestException("Null Client.");
		if(server == null || server.trim().isEmpty())
			throw new BadTicketRequestException("Null Server.");
		if(ticketDuration < MIN_TICKET_DURATION || ticketDuration > MAX_TICKET_DURATION)
			throw new BadTicketRequestException("Invalid Ticked Duration.");
		if(previousNounces.contains(nounce))
			throw new BadTicketRequestException("Repeated Nounce, possible Replay Attack.");
		
		
		try {
			/* Get Client and Server Keys from a known Passwords file */
			Key clientKey = getKnownKey(client);
			Key serverKey = getKnownKey(server);
			/* Generate a new key for Client-Server communication */
			Key clientServerKey = SecurityHelper.generateKey();
			
			/* Create and Seal the Ticket */
			Ticket ticket = createTicket(client, server, ticketDuration, clientServerKey);
			SealedView sealedTicket = ticket.seal(serverKey);
			
			/* Create and Seal the Session Key */
			//SessionKey sessionKey = new SessionKey(clientServerKey, nounce);
			//SealedView sealedSessionKey = sessionKey.seal(clientKey);
			
			/* Create SessionKeyAndTicketView */
			SessionKeyAndTicketView response = new SessionKeyAndTicketView();
			response.setTicket(sealedTicket);
			//response.setSessionKey(sealedSessionKey);
			
			/* Store Nounce */
			previousNounces.add(nounce);
			
			return response;
			
		} catch (NoSuchAlgorithmException e) {
			throw new BadTicketRequestException("Error generating shared key.");
		} catch (KerbyException e) {
			throw new BadTicketRequestException("Error while sealing.");
		}
	}
	
	// Helpers -------------------------------------------------------------
	
	/** Reads lines from a text file containing "username,password".
	 *  Stores the pairs in the userPasswords HashMap
	 * @throws IOException 
	 * */
	private void initPasswords() throws IOException {
		InputStream inputStream = KerbyManager.class.getResourceAsStream(PASSWORDS_FILENAME);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		String[] values;
		while((line = reader.readLine()) != null) {
			values = line.split(",");
			passwords.put(values[0], values[1]);
		}
		

	}
	
	/** Returns a Client or Server Key based on a known password. 
	 * If the passwords file has not been read, then it calls initPasswords.
	 * @throws BadTicketRequestException
	 * */
	private Key getKnownKey(String user) throws BadTicketRequestException {
		try {
			if(passwords.isEmpty())
				initPasswords();
			
			String password = passwords.get(user);
			if(password == null)
				throw new BadTicketRequestException("Unknown User.");
			
			Key key = SecurityHelper.generateKeyFromPassword(password, user);
			return key;
		} catch(IOException e) {
			throw new BadTicketRequestException("Error Reading Passwords File.");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new BadTicketRequestException("Error Generating Key from Password.");
		}
	}
	
	private Ticket createTicket(String client, String server, int ticketDuration, Key clientServerKey) {
		final Calendar calendar = Calendar.getInstance();
		final Date t1 = calendar.getTime();
		calendar.add(Calendar.SECOND, ticketDuration);
		final Date t2 = calendar.getTime();
		Ticket ticket = new Ticket(client, server, t1, t2, clientServerKey);
		return ticket;
	}
	
}
