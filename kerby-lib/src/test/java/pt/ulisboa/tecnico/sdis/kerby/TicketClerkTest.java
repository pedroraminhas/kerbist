package pt.ulisboa.tecnico.sdis.kerby;

import org.junit.*;
import org.w3c.dom.Document;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.dateToXML;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.xmlToDate;
import static pt.ulisboa.tecnico.sdis.kerby.SecurityHelper.*;

import static org.junit.Assert.*;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static javax.xml.bind.DatatypeConverter.printHexBinary;


/**
 *  Test suite
 */
public class TicketClerkTest {

    // static members


    // one-time initialization and clean-up

    @BeforeClass
    public static void oneTimeSetUp() {

    }

    @AfterClass
    public static void oneTimeTearDown() {

    }


    // members
    private TicketClerk clerk;



    // initialization and clean-up for each test

    @Before
    public void setUp() {
    	clerk = new TicketClerk();
    }

    @After
    public void tearDown() {
    	clerk = null;
    }

    
    // helpers

    private TicketView newTestTicket(Date t1, Date t2, Key key) throws NoSuchAlgorithmException {
    	TicketView ticket = clerk.newTicketView("C", "S", t1, t2, key);
    	return ticket;
    }
    
    private TicketView newTestTicket(Date t1, Date t2) throws NoSuchAlgorithmException {
    	final Key key = SecurityHelper.generateKey("AES", 256);
    	TicketView ticket = clerk.newTicketView("C", "S", t1, t2, key);
    	return ticket;
    }
    
    private TicketView newTestTicket() throws NoSuchAlgorithmException {
    	final Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
    	final Date t1 = calendar.getTime();
    	calendar.add(Calendar.SECOND, 60);
    	final Date t2 = calendar.getTime();
    	return newTestTicket(t1, t2);
    }


    // tests

	@Test
	public void testCreateTicket() throws Exception {

		final Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
		final Date t1 = calendar.getTime();
		calendar.add(Calendar.SECOND, 60);
		final Date t2 = calendar.getTime();
    	final Key key = SecurityHelper.generateKey("AES", 256);
		TicketView ticket = newTestTicket(t1, t2, key);

		assertEquals(/* expected */ "C", /* actual */ ticket.getX());
		assertEquals(/* expected */ "S", /* actual */ ticket.getY());
		assertEquals(/* expected */ t1, /* actual */ xmlToDate(ticket.getTime1()));
		assertEquals(/* expected */ t2, /* actual */ xmlToDate(ticket.getTime2()));
		assertArrayEquals(/* expected */ key.getEncoded(), /* actual */ ticket.getEncodedKeyXY());

		final Key recodedKey = new SecretKeySpec(ticket.getEncodedKeyXY(), "AES");
		assertEquals(/* expected */ key, /* actual */ recodedKey);
	}

	@Test
	public void testTicketToXML() throws Exception {
		TicketView ticket = newTestTicket();
		System.out.println(ticket);

		// convert to XML document

		// create a JAXBContext
		JAXBContext jc = JAXBContext.newInstance("pt.ulisboa.tecnico.sdis.kerby");

		// create XML element (a complex type cannot be instantiated by itself)
		JAXBElement<TicketView> jaxbElementMarshal = new JAXBElement<>(
				new QName("http://kerby.sdis.tecnico.ulisboa.pt/", "ticket", "k"),
				pt.ulisboa.tecnico.sdis.kerby.TicketView.class, ticket);

		// create a Marshaller and marshal
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE); // omit xml declaration
		m.marshal(jaxbElementMarshal, System.out);

		DOMResult res = new DOMResult();
		// JAXBContext jc = JAXBContext.newInstance(obj.getClass());
		jc.createMarshaller().marshal(jaxbElementMarshal, res);
		Document document = (Document) res.getNode();

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(document), new StreamResult(System.out));
	}

	@Test
	public void testXMLToTicket() throws Exception {
		// convert from XML
		// reference: https://stackoverflow.com/a/33824472/129497

		final String xml = "<ns2:ticket xmlns:ns2=\"http://kerby.sdis.tecnico.ulisboa.pt/\"><encodedKeyXY>W0TyEos75xnqGygBTLWoHA1aOWf2rPDrmKTUNYntcKk=</encodedKeyXY><time1>2018-03-07T10:06:40.696Z</time1><time2>2018-03-07T10:07:40.696Z</time2><x>C</x><y>S</y></ns2:ticket>";

		// create a JAXBContext
		JAXBContext jc = JAXBContext.newInstance("pt.ulisboa.tecnico.sdis.kerby");

		// create an Unmarshaller
        Unmarshaller u = jc.createUnmarshaller();
		
        // unmarshal, get element and cast to expected type
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        JAXBElement<TicketView> ticketElement = (JAXBElement<TicketView>) u.unmarshal(new StreamSource(is), TicketView.class);
        TicketView ticket = ticketElement.getValue();
        
//        TicketView ticket = (TicketView) jaxbElementUnmarshal.getValue();

        // print part of the read information
        System.out.println("ticket");
        System.out.println("X= " + ticket.getX());
        System.out.println("Y= " + ticket.getY());
        System.out.println("T1= " + ticket.getTime1());
        System.out.println("T2= " + ticket.getTime2());
        System.out.println("Key= " + printHexBinary(ticket.getEncodedKeyXY()));
	}

	// @Test(expected = KerbyException.class)
	// public void testCreateTicketWrongTimes() throws Exception {
	// final String client = "C";
	// final String server = "S";
	// final Instant now = Instant.now();
	// final Instant beforeNow = now.minusSeconds(1);
	// final Key key = ;
	//
	// new Ticket(client, server, now, beforeNow, key);
	// }
	//
	// @Test
	// public void testCipherTicket() throws Exception {
	// final String client = "C";
	// final String server = "S";
	// final Instant now = Instant.now();
	// final Instant later = now.plusSeconds(60);
	// final Key sessionKey = generateKey("AES", 128);
	// // create ticket to hold session key
	// Ticket ticket = new Ticket(client, server, now, later, sessionKey);
	//
	// // cipher ticket with server key
	// final Key serverKey = generateKey("AES", 128);
	// CipheredTicket cipheredTicket = ticket.cipher(serverKey);
	//
	// // decipher ticket with server key
	// Ticket decipheredTicket = cipheredTicket.decipher(serverKey);
	//
	// // compare that obtained ticket is equal to original ticket
	// assertEquals(/* expected */ ticket, /* actual */ decipheredTicket);
	// }

    
    @Test
    public void testSealTicket() throws Exception {
    	TicketView ticket = newTestTicket();

    	// seal ticket with server key
    	final Key serverKey = generateKey("AES", 128);
    	SealedView sealedTicket = clerk.seal(ticket, key);
    	
    	// decipher ticket with server key
    	TicketView decipheredTicket = clerk.unseal(sealedTicket, key);
    	
    	// compare that obtained ticket is equal to original ticket
    	assertEquals(/*expected*/ ticket, /*actual*/ decipheredTicket);
    }
    
}
