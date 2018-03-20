package pt.ulisboa.tecnico.sdis.kerby;

public class TicketCollectionEntry {
	private CipheredView cipheredTicket;
	private long finalValidTime;
	
	public TicketCollectionEntry(CipheredView cipheredTicket, long finalValidTime) {
		this.cipheredTicket = cipheredTicket;
		this.finalValidTime = finalValidTime;
	}
	
	public CipheredView getTicket() {
		return cipheredTicket;
	}
	
	public long getFinalValidTime() {
		return finalValidTime;
	}
	
	// object methods --------------------------------------------------------
	
	/** Create a textual representation of the TicketCollectionEntry. */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TicketCollectionEntry [cipheredTicket=");
		builder.append(cipheredTicket.toString());
		builder.append(", finalValidTime=");
		builder.append(finalValidTime);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cipheredTicket == null) ? 0 : cipheredTicket.hashCode());
		result = prime * result + (int) (finalValidTime ^ (finalValidTime >>> 32));
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
		TicketCollectionEntry other = (TicketCollectionEntry) obj;
		if (cipheredTicket == null) {
			if (other.cipheredTicket != null)
				return false;
		} else if (!cipheredTicket.equals(other.cipheredTicket))
			return false;
		if (finalValidTime != other.finalValidTime)
			return false;
		return true;
	}
	
}
