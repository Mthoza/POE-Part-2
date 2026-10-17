package com.mycompany.poepart1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Message class for QuickChat (Part 2).
 * Each Message object holds the details of ONE message:
 * message ID, message number, recipient, message text and message hash.
 * The static lists keep track of every message sent or stored while the
 * program is running.
 *
 * @author Student
 */
public class Message {

    // name of the JSON file that stored messages are saved in
    public static final String JSON_FILE = "messages.json";

    // all messages that were sent while the program is running
    private static ArrayList<Message> sentMessages = new ArrayList<>();

    // all messages the user chose to store for later
    private static ArrayList<Message> storedMessages = new ArrayList<>();

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // The hash is created automatically as soon as the message is created
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Creates a random ten-digit message ID (built one digit at a time in a loop)
    public static String generateMessageID() {
        Random random = new Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + random.nextInt(10);
        }
        return id;
    }

    // Makes sure the message ID is not more than ten characters
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Returns the text that is shown when a message ID has been created
    public String getMessageIDStatus() {
        return "Message ID generated: " + messageID;
    }

    /*
     Checks the recipient cell number. This reuses checkCellPhoneNumber()
     from the Login class in Part 1 (+27 followed by no more than 10 digits).
     */
    public String checkRecipientCell() {
        Login loginHelper = new Login(recipient, "", "");
        if (loginHelper.checkCellPhoneNumber()) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.";
    }

    // Checks that the message is not more than 250 characters
    public String checkMessageLength() {
        int length = messageText.length();
        if (length <= 250) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (length - 250)
                + "; please reduce the size.";
    }

    /*
     Creates the message hash: first two numbers of the message ID, a colon,
     the message number, a colon, then the first and last word of the message
     in capital letters. Example: 00:0:HITONIGHT
     */
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");

        // remove punctuation such as "?" or "," so only letters and numbers are left
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");

        String firstTwoNumbers = messageID.substring(0, 2);
        return (firstTwoNumbers + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    /*
     Does what the user chose: 1 = send, 2 = disregard, 3 = store for later.
     Returns the message that must be shown to the user.
     */
    public String sentMessage(int choice) {
        if (choice == 1) {
            sentMessages.add(this);
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            if (storeMessage()) {
                return "Message successfully stored.";
            }
            return "Message could not be stored.";
        }
        return "Invalid option.";
    }

    // Returns the full details of this message in the required order
    public String getDetails() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText + "\n";
    }

    // Returns the details of ALL messages sent while the program is running
    public static String printMessages() {
        String allMessages = "";
        for (Message message : sentMessages) {
            allMessages = allMessages + message.getDetails() + "\n";
        }
        return allMessages;
    }

    // Returns how many messages have been sent
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    /*
     Stores this message by saving all stored messages to messages.json.
     Returns true if the file was saved. The JSON layout follows the
     rules on json.org and FileWriter is used as shown in the Oracle docs
     (see references at the bottom of this file).
     */
    public boolean storeMessage() {
        storedMessages.add(this);

        String json = "[\n";
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json = json + "  {\n";
            json = json + "    \"messageID\": \"" + clean(m.messageID) + "\",\n";
            json = json + "    \"messageNumber\": " + m.messageNumber + ",\n";
            json = json + "    \"messageHash\": \"" + clean(m.messageHash) + "\",\n";
            json = json + "    \"recipient\": \"" + clean(m.recipient) + "\",\n";
            json = json + "    \"message\": \"" + clean(m.messageText) + "\"\n";
            json = json + "  }";
            if (i < storedMessages.size() - 1) {
                json = json + ",";
            }
            json = json + "\n";
        }
        json = json + "]";

        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write(json);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
            return false;
        }
    }

    // Escapes quotes, backslashes and new lines so the text is valid inside JSON
    private static String clean(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    // Clears both lists (used by the unit tests so every test starts fresh)
    public static void clearAll() {
        sentMessages.clear();
        storedMessages.clear();
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }
}
/* References
Oracle (2026) FileWriter (Java SE 21 API). Available at:
https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/FileWriter.html (Accessed: 15 October 2026).

Ecma International (2017) The JSON Data Interchange Syntax (ECMA-404). Available at:
https://www.json.org/json-en.html (Accessed: 15 October 2026).

Anthropic (2026) Claude (AI assistant) was used to help write and structure this code. Available at:
https://claude.ai (Accessed: 15 October 2026).
*/
