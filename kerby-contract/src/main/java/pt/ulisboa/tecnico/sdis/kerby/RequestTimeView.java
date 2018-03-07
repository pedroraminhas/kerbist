package pt.ulisboa.tecnico.sdis.kerby;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that represents a Kerberos authenticator response with the request
 * time.
 * 
 * @author Miguel Pardal
 *
 */
@XmlRootElement // to add element declaration to schema
public class RequestTimeView implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date timeRequest;

	public Date getTimeRequest() {
		return timeRequest;
	}

	public void setTimeRequest(Date timeRequest) {
		this.timeRequest = timeRequest;
	}

}
