package pt.ulisboa.tecnico.sdis.kerby;

import org.junit.*;
import static org.junit.Assert.*;

import java.time.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import static javax.xml.bind.DatatypeConverter.printHexBinary;


/**
 *  Test suite
 */
public class TicketTest {

    // static members


    // one-time initialization and clean-up

    @BeforeClass
    public static void oneTimeSetUp() {

    }

    @AfterClass
    public static void oneTimeTearDown() {

    }


    // members



    // initialization and clean-up for each test

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    
    // helpers

    private Key generateKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
		keyGen.init(keySize);
		Key key = keyGen.generateKey();
		return key;
	}

    // tests

    @Test
    public void testCreateTicket() throws Exception {
    	final String client = "C";
    	final String server = "S";
    	final Instant now = Instant.now();
    	final Instant later = now.plusSeconds(60);
    	final Key key = generateKey("AES", 256);
    	
    	Ticket ticket = new Ticket(client, server, now, later, key);

    	assertEquals(/*expected*/ client, /*actual*/ ticket.getX());
    	assertEquals(/*expected*/ server, /*actual*/ ticket.getY());
    	assertEquals(/*expected*/ now, /*actual*/ ticket.getTime1());
    	assertEquals(/*expected*/ later, /*actual*/ ticket.getTime2());
    	assertEquals(/*expected*/ key, /*actual*/ ticket.getKeyXY());
    }

    @Test(expected=KerbyException.class)
    public void testCreateTicketWrongTimes() throws Exception {
    	final String client = "C";
    	final String server = "S";
    	final Instant now = Instant.now();
    	final Instant beforeNow= now.minusSeconds(1);
    	final Key key = generateKey("AES", 256);
    	
    	new Ticket(client, server, now, beforeNow, key);
    }

    @Test
    public void testCipherTicket() throws Exception {
    	final String client = "C";
    	final String server = "S";
    	final Instant now = Instant.now();
    	final Instant later = now.plusSeconds(60);
    	final Key sessionKey = generateKey("AES", 128);
    	// create ticket to hold session key
    	Ticket ticket = new Ticket(client, server, now, later, sessionKey);

    	// cipher ticket with server key
    	final Key serverKey = generateKey("AES", 128);
    	CipheredTicket cipheredTicket = ticket.cipher(serverKey);
    	
    	// decipher ticket with server key
    	Ticket decipheredTicket = cipheredTicket.decipher(serverKey);
    	
    	// compare that obtained ticket is equal to original ticket
    	assertEquals(/*expected*/ ticket, /*actual*/ decipheredTicket);
    }
    
}
