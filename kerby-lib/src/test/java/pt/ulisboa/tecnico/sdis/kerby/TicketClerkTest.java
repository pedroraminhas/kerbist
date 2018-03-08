package pt.ulisboa.tecnico.sdis.kerby;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static pt.ulisboa.tecnico.sdis.kerby.SecurityHelper.generateKey;
import static pt.ulisboa.tecnico.sdis.kerby.SecurityHelper.recodeKey;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.dateToXML;
import static pt.ulisboa.tecnico.sdis.kerby.XMLHelper.xmlToDate;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test suite
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

	/** Create a test ticket with the specified times and key. */
	private TicketView newTestTicket(Date t1, Date t2, Key key) throws NoSuchAlgorithmException {
		TicketView ticket = clerk.ticketBuild("C", "S", t1, t2, key);
		return ticket;
	}

	/** Create a test ticket with the specified times. */
	private TicketView newTestTicket(Date t1, Date t2) throws NoSuchAlgorithmException {
		final Key key = SecurityHelper.generateKey();
		TicketView ticket = clerk.ticketBuild("C", "S", t1, t2, key);
		return ticket;
	}

	/** Create a test ticket. */
	private TicketView newTestTicket() throws NoSuchAlgorithmException {
		final Calendar calendar = Calendar.getInstance();
		final Date t1 = calendar.getTime();
		calendar.add(Calendar.SECOND, 60);
		final Date t2 = calendar.getTime();
		return newTestTicket(t1, t2);
	}

	// tests

	@Test
	public void testCreateTicket() throws Exception {
		// gets a calendar using the default time zone and locale
		final Calendar calendar = Calendar.getInstance();
		final Date t1 = calendar.getTime();
		calendar.add(Calendar.SECOND, 60);
		final Date t2 = calendar.getTime();
		final Key key = SecurityHelper.generateKey();
		TicketView ticket = newTestTicket(t1, t2, key);

		// compare contents individually
		assertEquals(/* expected */ "C", /* actual */ ticket.getX());
		assertEquals(/* expected */ "S", /* actual */ ticket.getY());
		assertEquals(/* expected */ t1, /* actual */ xmlToDate(ticket.getTime1()));
		assertEquals(/* expected */ t2, /* actual */ xmlToDate(ticket.getTime2()));
		assertArrayEquals(/* expected */ key.getEncoded(), /* actual */ ticket.getEncodedKeyXY());

		final Key recodedKey = recodeKey(ticket.getEncodedKeyXY());
		assertEquals(/* expected */ key, /* actual */ recodedKey);
	}

	@Test
	public void testTicketToString() throws Exception {
		TicketView ticket = newTestTicket();
		String string = clerk.ticketToString(ticket);
		assertNotNull(string);
		assertTrue(string.trim().length() > 0);
	}

	@Test
	public void testCreateEqualsTicket() throws Exception {
		TicketView ticket1 = newTestTicket();
		TicketView ticket2 = newTestTicket();
		ticket2.setEncodedKeyXY(ticket1.getEncodedKeyXY());
		ticket2.setTime1(ticket1.getTime1());
		ticket2.setTime2(ticket1.getTime2());

		// equals is not defined by JAX-B
		assertFalse(ticket1.equals(ticket2));
		// clerk equals is correct
		assertTrue(clerk.ticketEquals(ticket1, ticket2));

		// make a (small) difference
		ticket1.setY(ticket2.getY() + "notTheSame");
		// clerk equals should detect difference
		assertFalse(clerk.ticketEquals(ticket1, ticket2));
	}

	@Test(expected = KerbyException.class)
	public void testValidateTicketIncompleteX() throws Exception {
		TicketView ticket = newTestTicket();
		ticket.setX(null);
		clerk.ticketValidate(ticket);
	}

	@Test(expected = KerbyException.class)
	public void testValidateTicketIncompleteY() throws Exception {
		TicketView ticket = newTestTicket();
		ticket.setY(null);
		clerk.ticketValidate(ticket);
	}

	@Test(expected = KerbyException.class)
	public void testValidateTicketIncompleteTime1() throws Exception {
		TicketView ticket = newTestTicket();
		ticket.setTime1(null);
		clerk.ticketValidate(ticket);
	}

	@Test(expected = KerbyException.class)
	public void testValidateTicketIncompleteTime2() throws Exception {
		TicketView ticket = newTestTicket();
		ticket.setTime2(null);
		clerk.ticketValidate(ticket);
	}

	@Test(expected = KerbyException.class)
	public void testValidateTicketIncompleteKey() throws Exception {
		TicketView ticket = newTestTicket();
		ticket.setEncodedKeyXY(null);
		clerk.ticketValidate(ticket);
	}

	@Test(expected = KerbyException.class)
	public void testValidateTicketWrongTimes() throws Exception {
		TicketView ticket = newTestTicket();
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -60);
		final Date beforeT1 = calendar.getTime();

		ticket.setTime2(dateToXML(beforeT1));
		clerk.ticketValidate(ticket);
	}

	@Test
	public void testMarshalTicket() throws Exception {
		TicketView ticket1 = newTestTicket();

		// convert to XML
		byte[] bytes = clerk.ticketToXMLBytes(ticket1);

		// convert back to Java object
		TicketView ticket2 = clerk.ticketFromXMLBytes(bytes);

		// compare tickets
		assertTrue(clerk.ticketEquals(ticket1, ticket2));
	}

	@Test
	public void testSealTicket() throws Exception {
		TicketView ticket = newTestTicket();

		// seal ticket with server key
		final Key serverKey = generateKey();
		SealedView sealedTicket = clerk.ticketSeal(ticket, serverKey);

		// decipher ticket with server key
		TicketView decipheredTicket = clerk.ticketUnseal(sealedTicket, serverKey);

		// compare tickets
		assertTrue(clerk.ticketEquals(ticket, decipheredTicket));
	}

}
