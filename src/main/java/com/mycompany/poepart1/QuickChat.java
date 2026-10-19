package com.mycompany.poepart1;

import java.util.Scanner;

/**
 * QuickChat (Part 2) - the menu and the loops.
 * This is only run once the user has logged in successfully.
 *
 * @author Student
 */
public class QuickChat {

    public static void start(Scanner input) {
        System.out.println("Welcome to QuickChat.");

        // the user decides at the start how many messages they want to enter
        int numMessages = 0;
        while (numMessages < 1) {
            System.out.println("How many messages do you wish to enter?");
            numMessages = readNumber(input);
            if (numMessages < 1) {
                System.out.println("Please enter a number of 1 or more.");
            }
        }

        boolean messagesEntered = false;
        int choice = 0;

        // the application keeps running until the user selects 3 (Quit)
        while (choice != 3) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            choice = readNumber(input);

            if (choice == 1) {
                if (messagesEntered) {
                    System.out.println("You have already entered all " + numMessages + " messages.");
                } else {
                    enterMessages(input, numMessages);
                    messagesEntered = true;
                }
            } else if (choice == 2) {
                System.out.println("Coming Soon.");
            } else if (choice == 3) {
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid option, please enter 1, 2 or 3.");
            }
        }
    }

    // Lets the user enter the set number of messages (for loop)
    private static void enterMessages(Scanner input, int numMessages) {
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\nMessage " + (i + 1) + " of " + numMessages);

            // keep asking until the recipient number is correct
            String recipient = "";
            boolean recipientOk = false;
            while (!recipientOk) {
                System.out.println("Please enter the recipient cell number (e.g. +27718693002):");
                recipient = input.nextLine();
                Message check = new Message("0000000000", i, recipient, "check");
                String result = check.checkRecipientCell();
                System.out.println(result);
                recipientOk = result.equals("Cell phone number successfully captured.");
            }

            // keep asking until the message is not longer than 250 characters
            String text = "";
            boolean textOk = false;
            while (!textOk) {
                System.out.println("Please enter your message:");
                text = input.nextLine();
                Message check = new Message("0000000000", i, recipient, text);
                String result = check.checkMessageLength();
                if (result.equals("Message ready to send.")) {
                    textOk = true;
                } else {
                    System.out.println("Please enter a message of less than 250 characters.");
                }
                System.out.println(result);
            }

            // the loop counter i is the message number, so the first message is number 0
            Message message = new Message(Message.generateMessageID(), i, recipient, text);
            System.out.println(message.getMessageIDStatus());

            int sendChoice = 0;
            while (sendChoice < 1 || sendChoice > 3) {
                System.out.println("Please choose an option:");
                System.out.println("1) Send Message");
                System.out.println("2) Disregard Message");
                System.out.println("3) Store Message to send later");
                sendChoice = readNumber(input);
            }

            System.out.println(message.sentMessage(sendChoice));

            if (sendChoice == 1) {
                System.out.println("\n" + message.getDetails());
            } else if (sendChoice == 2) {
                String answer = input.nextLine();
                if (answer.equals("0")) {
                    System.out.println("Message deleted.");
                }
            }
        }

        System.out.println("All messages have been entered.");
        System.out.println("Messages sent:\n" + Message.printMessages());
        System.out.println("Total messages sent: " + Message.returnTotalMessages());
    }

    // Reads a whole number from the user without crashing if they type letters
    private static int readNumber(Scanner input) {
        int number = 0;
        boolean valid = false;
        while (!valid) {
            try {
                number = Integer.parseInt(input.nextLine().trim());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("That is not a number, please try again.");
            }
        }
        return number;
    }
}
/* References
freeCodeCamp.org (2022) Java Programming for Beginners – Full Course. Available at: https://www.youtube.com/watch?v=A74TOX803D0 (Accessed: 9 September 2026).

Anthropic (2026) Claude (AI assistant) was used to help plan and structure this code. Available at: https://claude.ai (Accessed: 15 October 2026).
*/
