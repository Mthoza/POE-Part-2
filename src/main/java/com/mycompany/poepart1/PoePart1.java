/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poepart1;

/**
 *
 * @author Student
 */
import java.util.Scanner;
public class PoePart1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Hello there, please enter your username");
        String userName = input.nextLine();

        System.out.println("Please enter your South African cell phone number");
        String phoneNumber = input.nextLine();

        System.out.println("Please enter your password");
        String password = input.nextLine();

        Login login = new Login(phoneNumber, userName, password);

        // to check username
        if (login.checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that\n"
                    + "your username contains an underscore and is no more than five characters in length.");
        }

        //  to check whether cell phone number is valid for south africa
        if (login.checkCellPhoneNumber()) {
            System.out.println("Cell number successfully captured.");
        } else {
            System.out.println("Cell number is incorrectly formatted or does not contain an\n"
                    + "international code; please correct the number and try again.");
        }

        // Check password complexity
        if (login.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that\n"
                    + "the password contains at least eight characters, a capital letter, a number,\n"
                    + "and a special character.");
        }

        // Only allow login if registration was successful
        if (login.checkUserName() && login.checkCellPhoneNumber() && login.checkPasswordComplexity()) {
            System.out.println("\nYou have been registered successfully! Please log in.\n");

            System.out.println("Please enter your username to log in");
            String loginUsername = input.nextLine();

            System.out.println("Please enter your password to log in");
            String loginPassword = input.nextLine();

            System.out.println(login.returnLoginStatus(loginUsername, loginPassword));
            
            // Part 2: users can only use QuickChat if they logged in successfully
            if (login.loginUser(loginUsername, loginPassword)) {
                QuickChat.start(input);
            }
        } else {
            System.out.println("\nRegistration was not successful. Please restart and correct the details above.");
        }

        input.close();
    }
}
/* References 
freeCodeCamp.org (2022) Java Programming for Beginners – Full Course. Available at: https://www.youtube.com/watch?v=A74TOX803D0 (Accessed: 9 September 2026).

Saini, M. (2018) Java NetBeans Tutorial | chat application in java. Available at: https://www.youtube.com/watch?v=dlacc6831zw (Accessed: 9 September 2026).
*/