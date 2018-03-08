package pt.ulisboa.tecnico.sdis.kerby;

import java.io.PrintStream;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLHelper {

	private static final DatatypeFactory DATATYPE_FACTORY;
	private static final TransformerFactory TRANSFORMER_FACTORY;

	/** XML transformer property name for XML indentation amount. */
	private static final String XML_INDENT_AMOUNT_PROPERTY = "{http://xml.apache.org/xslt}indent-amount";
	/** XML indentation amount to use (default=0). */
	private static final Integer XML_INDENT_AMOUNT_VALUE = 2;

	// one-time initialization and clean-up
	static {
		try {
			DATATYPE_FACTORY = DatatypeFactory.newInstance();
			TRANSFORMER_FACTORY = TransformerFactory.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(XMLHelper.class.getSimpleName() + " initialization failed!", e);
		}
	}

	// Namespace utilities --------------------------------------------------- 
	
	private static final String reversePackageName(final String packageName) {
		if (packageName == null || packageName.length() == 0)
			throw new IllegalArgumentException("Package name to reverse cannot be empty!");
		String[] part = packageName.split("\\.");
		StringBuilder builder = new StringBuilder();
		for (int i = part.length - 1; i > 0; i--) {
			builder.append(part[i]);
			if (i != 1)
				builder.append(".");
		}
		return builder.toString();
	}

	public static String xmlNamespaceFromJavaPackage(final String packageName) {
		String reverse = reversePackageName(packageName);
		return "http://" + reverse + "/";
	}
	
	
	// DOM navigation --------------------------------------------------------

	/**
	 * Return direct child element with specified name. If not found, returns null.
	 */
	public static Element getDirectChild(Element parent, String name) {
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
	public static Element getDirectChild(Document document, String name) {
		return getDirectChild(document.getDocumentElement(), name);
	}

	// Textual representation ------------------------------------------------

	public static void printXML(Source xmlSource, PrintStream out) throws TransformerException {
		// transform the (DOM) Source into a StreamResult
		Transformer transformer = TRANSFORMER_FACTORY.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(XML_INDENT_AMOUNT_PROPERTY, XML_INDENT_AMOUNT_VALUE.toString());
		StreamResult result = new StreamResult(out);
		transformer.transform(xmlSource, result);
	}

	// Date processing -------------------------------------------------------

	public static XMLGregorianCalendar dateToXML(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return DATATYPE_FACTORY.newXMLGregorianCalendar(gc);
	}

	public static Date xmlToDate(XMLGregorianCalendar xgc) {
		return xgc.toGregorianCalendar().getTime();
	}

}
