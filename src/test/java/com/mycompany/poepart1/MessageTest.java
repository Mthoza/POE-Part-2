package com.mycompany.poepart1;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * @author Student
 */
public class MessageTest {

    private Message message1;
    private Message message2;

    // runs before every test so the tests do not affect each other
    @BeforeEach
    public void setUp() {
        Message.clearAll();
        message1 = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        message2 = new Message("0587654321", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
    }

    // ---------- message length (success and failure) ----------

    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longText = "a".repeat(260);
        Message longMessage = new Message("0012345678", 0, "+27718693002", longText);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                longMessage.checkMessageLength());
    }

    // ---------- recipient number (success and failure) ----------

    @Test
    public void testRecipientNumberSuccess() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void testRecipientNumberFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    // ---------- message hash ----------

    @Test
    public void testMessageHashTestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    // tests the remaining message hashes in a loop
    @Test
    public void testAllMessageHashesInLoop() {
        Message[] messages = {message1, message2};
        String[] expectedHashes = {"00:0:HITONIGHT", "05:1:HIPAYMENT"};

        for (int i = 0; i < messages.length; i++) {
            assertEquals(expectedHashes[i], messages[i].createMessageHash());
        }
    }

    // ---------- message ID ----------

    @Test
    public void testMessageIDCreated() {
        String id = Message.generateMessageID();
        Message message = new Message(id, 0, "+27718693002", "Hello there");
        assertEquals("Message ID generated: " + id, message.getMessageIDStatus());
        assertEquals(10, id.length());
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testMessageIDTooLong() {
        Message message = new Message("12345678901", 0, "+27718693002", "Hello there");
        assertFalse(message.checkMessageID());
    }

    // ---------- send, disregard and store ----------

    @Test
    public void testSendMessage() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    public void testDisregardMessage() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    public void testStoreMessage() {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
        assertTrue(new File(Message.JSON_FILE).exists());
    }

    // ---------- totals and printing ----------

    // message 1 is sent and message 2 is discarded, so the total must be 1
    @Test
    public void testReturnTotalMessages() {
        message1.sentMessage(1);
        message2.sentMessage(2);
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessagesShowsDetailsInOrder() {
        message1.sentMessage(1);
        String expected = "Message ID: 0012345678\n"
                + "Message Hash: 00:0:HITONIGHT\n"
                + "Recipient: +27718693002\n"
                + "Message: Hi Mike, can you join us for dinner tonight?\n\n";
        assertEquals(expected, Message.printMessages());
    }
}
