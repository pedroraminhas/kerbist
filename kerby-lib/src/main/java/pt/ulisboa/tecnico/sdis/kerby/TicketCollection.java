package pt.ulisboa.tecnico.sdis.kerby;

import java.util.concurrent.ConcurrentHashMap;

public class TicketCollection {
	/** Maps Server to TicketEntry */
	private ConcurrentHashMap<String, TicketCollectionEntry> ticketCollection = new ConcurrentHashMap<String, TicketCollectionEntry>();
	/** Accepted Difference between Ticket's Final Valid Time and Current Time. Measured in milliseconds. */
	private long acceptedDifference = 2000;
	
	public TicketCollection() {}
	
	public TicketCollection(long difference) {
		acceptedDifference = difference;
	}
	
	/** Stores the Ticket and FinalTime Indexed by the Server Name. 
	 * @param finalValidTime Final Valid Time in Milliseconds since January 1st 1970 */
	public void storeTicket(String serverName, SessionKeyAndTicketView sessionKeyAndTicketView, long finalValidTime) {
		TicketCollectionEntry newEntry = new TicketCollectionEntry(sessionKeyAndTicketView, finalValidTime);
		ticketCollection.put(serverName, newEntry);
	}
	
	/** Returns a Stored SessionKeyAndTicketView for the Given Server if a Valid Entry Exists. Else, Returns null. */
	public SessionKeyAndTicketView getTicket(String serverName) {
		TicketCollectionEntry storedEntry = ticketCollection.get(serverName);
		if(storedEntry == null)
			return null;
		
		long currentTime = System.currentTimeMillis();
		long entryTime = storedEntry.getFinalValidTime();
		
		// Expired Ticket
		if(entryTime - currentTime < acceptedDifference)
			return null;
		else
			return storedEntry.getSessionKeyAndTicketView();
	}
	
	public void clear() {
		ticketCollection.clear();
	}
	
}
