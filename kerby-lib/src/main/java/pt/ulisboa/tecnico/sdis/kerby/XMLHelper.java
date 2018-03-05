package pt.ulisboa.tecnico.sdis.kerby;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLHelper {

	/**
	 * Return direct child element with specified name. If not found, returns null.
	 */
	static Element getDirectChild(Element parent, String name) {
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
			// TODO replace instanceof with W3C DOM check node type call
			if (child instanceof Element && name.equals(child.getNodeName())) {
				return (Element) child;
			}
		}
		return null;
	}

	/**
	 * Return direct child of document with specified name. If not found, returns
	 * null.
	 */
	static Element getDirectChild(Document document, String name) {
		return getDirectChild(document.getDocumentElement(), name);
	}

}
