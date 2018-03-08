package pt.ulisboa.tecnico.sdis.kerby;

import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.viewToXML;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.viewToXMLBytes;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.xmlBytesToView;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.xmlNodeToView;

import java.util.Arrays;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.w3c.dom.Node;

/**
 * Class that handles sealed views in different formats. Sealed views are used
 * to transport ciphered Kerberos tickets, authenticators, etc.
 * 
 * @author Miguel Pardal
 *
 */
public class SealClerk {

	// creation --------------------------------------------------------------

	/** Create SealedView from arguments. */
	public SealedView sealBuild(byte[] data) {
		SealedView view = new SealedView();
		view.setData(data);
		return view;
	}

	/** Create a textual representation of the SealedView. */
	public String sealToString(SealedView view) {
		if (view == null)
			return "null";

		StringBuilder builder = new StringBuilder();
		builder.append("SealedView [data=");
		builder.append(Arrays.toString(view.getData()));
		builder.append("]");
		return builder.toString();
	}

	// serialization ---------------------------------------------------------

	/**
	 * Marshal sealed view to XML document.
	 */
	public Node sealToXMLNode(SealedView view, String sealTagName) throws JAXBException {
		return viewToXML(SealedView.class, view, new QName(sealTagName));
	}

	/** Marshal sealed view to XML bytes. */
	public byte[] sealToXMLBytes(SealedView view, String sealTagName) throws JAXBException {
		return viewToXMLBytes(SealedView.class, view, new QName(sealTagName));
	}

	/**
	 * Unmarshal sealed view from XML document.
	 */
	public SealedView sealFromXMLNode(Node xml) throws JAXBException {
		return xmlNodeToView(SealedView.class, xml);
	}

	/** Unmarshal byte array to a sealed view object. */
	public SealedView sealFromXMLBytes(byte[] bytes) throws JAXBException {
		return xmlBytesToView(SealedView.class, bytes);
	}

}
