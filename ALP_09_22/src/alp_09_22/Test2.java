package alp_09_22;

import java.util.Scanner;

public class Test2 {

    private static final int MAX_USERS = 10;

    private String[] user = new String[MAX_USERS];
    private String[] pass = new String[MAX_USERS];
    private int count = 0;
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Test2 finalProject = new Test2();
        finalProject.run();
    }

    public void run() {
        while (true) {
            displayMenu();
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    displaySecondMenu();
                    return;
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Login\n2. Create Account\n3. Continue as a Guest");
        System.out.print("Reply: ");
    }

    private void login() {
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();

        if (isValidLogin(username, password)) {
            System.out.println("You have successfully logged in");
            // Add logic for logged-in user actions
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private boolean isValidLogin(String username, String password) {
        for (int i = 0; i < count; i++) {
            if (username.equals(user[i]) && password.equals(pass[i])) {
                return true;
            }
        }
        return false;
    }

    private void createAccount() {
        System.out.print("Username: ");
        String newUsername = scanner.next();
        System.out.print("Password (at least 8 characters, including uppercase, lowercase, digit, and symbol): ");
        String newPassword = scanner.next();

        while (!isValidPassword(newPassword)) {
            System.out.println("Password does not meet the requirements. Try again.");
            System.out.print("Password: ");
            newPassword = scanner.next();
        }

        user[count] = newUsername;
        pass[count] = newPassword;
        count++;
        System.out.println("Account created successfully. Welcome!");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*\\d.*") &&
               password.matches(".*[^\\w\\d].*");
    }

    private void displaySecondMenu() {
        System.out.println("Menu:");
        System.out.println("1. Spendings");
        // Add more options as needed
    }
}
