package pt.ulisboa.tecnico.sdis.kerby;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement // to add element declaration to schema
public class SealedView implements Serializable {

	private static final long serialVersionUID = 1L;

	private byte[] cipheredView;

	public byte[] getCipheredView() {
		return cipheredView;
	}

	public void setCipheredView(byte[] cipheredView) {
		this.cipheredView = cipheredView;
	}

}
