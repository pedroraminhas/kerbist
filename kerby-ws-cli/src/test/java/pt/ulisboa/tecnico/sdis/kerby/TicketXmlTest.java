package pt.ulisboa.tecnico.sdis.kerby;

import org.junit.*;
import static org.junit.Assert.*;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * Test suite
 */
public class TicketXmlTest {

	// static members
	private static DatatypeFactory datatypeFactory;
	
	// one-time initialization and clean-up

	@BeforeClass
	public static void oneTimeSetUp() throws DatatypeConfigurationException {
		datatypeFactory = DatatypeFactory.newInstance();
	}

	@AfterClass
	public static void oneTimeTearDown() {

	}

	// members
	private TicketView ticket;

	// initialization and clean-up for each test

	@Before
	public void setUp() {

	}

	@After
	public void tearDown() {
	}

	// helpers

	private XMLGregorianCalendar dateToXML(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return datatypeFactory.newXMLGregorianCalendar(gc);
	}

	private Date xmlToDate(XMLGregorianCalendar xgc) {
		return xgc.toGregorianCalendar().getTime();
	}

	
	private TicketView newTicketView(String x, String y, Date time1, Date time2, Key key) {
		TicketView ticket = new TicketView();
		ticket.setX(x);
		ticket.setY(y);
		ticket.setTime1(dateToXML(time1));
		ticket.setTime2(dateToXML(time2));
		ticket.setEncodedKeyXY(key.getEncoded());
		return ticket;
	}
	
	private Key generateKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
		keyGen.init(keySize);
		Key key = keyGen.generateKey();
		return key;
	}

	// tests

	@Test
	public void testCreateTicket() throws Exception {
		
		final Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
		final Date t1 = calendar.getTime();
		calendar.add(Calendar.SECOND, 60);
		final Date t2 = calendar.getTime();
		
		final Key key = generateKey("AES", 256);
		
		
		TicketView ticket = newTicketView("C", "S", t1, t2, key);
		
		assertEquals(/* expected */ "C", /* actual */ ticket.getX());
		assertEquals(/* expected */ "S", /* actual */ ticket.getY());
		assertEquals(/* expected */ t1, /* actual */ xmlToDate(ticket.getTime1()));
		assertEquals(/* expected */ t2, /* actual */ xmlToDate(ticket.getTime2()));
		assertArrayEquals(/* expected */ key.getEncoded(), /* actual */ ticket.getEncodedKeyXY());
		
		final Key recodedKey = new SecretKeySpec(ticket.getEncodedKeyXY(), "AES");
		assertEquals(/* expected */ key, /* actual */ recodedKey);
	}

//	@Test(expected = KerbyException.class)
//	public void testCreateTicketWrongTimes() throws Exception {
//		final String client = "C";
//		final String server = "S";
//		final Instant now = Instant.now();
//		final Instant beforeNow = now.minusSeconds(1);
//		final Key key = ;
//
//		new Ticket(client, server, now, beforeNow, key);
//	}
//
//	@Test
//	public void testCipherTicket() throws Exception {
//		final String client = "C";
//		final String server = "S";
//		final Instant now = Instant.now();
//		final Instant later = now.plusSeconds(60);
//		final Key sessionKey = generateKey("AES", 128);
//		// create ticket to hold session key
//		Ticket ticket = new Ticket(client, server, now, later, sessionKey);
//
//		// cipher ticket with server key
//		final Key serverKey = generateKey("AES", 128);
//		CipheredTicket cipheredTicket = ticket.cipher(serverKey);
//
//		// decipher ticket with server key
//		Ticket decipheredTicket = cipheredTicket.decipher(serverKey);
//
//		// compare that obtained ticket is equal to original ticket
//		assertEquals(/* expected */ ticket, /* actual */ decipheredTicket);
//	}

}
