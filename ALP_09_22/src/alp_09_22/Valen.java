package alp_09_22;

import java.util.Scanner;

public class Valen {

    private Scanner scan;
    private String[] user;
    private String[] pass;
    private boolean exit;
    private int count;
    
    public Valen() {
        scan = new Scanner(System.in);
        user = new String[10];
        pass = new String[10];
        exit = false;
        count = 0;
    }

    public static void main(String[] args) {
        Valen func = new Valen();
        func.firstPage();
    }

    public void firstPage() {
        while (!exit) {
            System.out.println("Menu:");
            System.out.println("1. Login" + "\n2. Create Account" + "\n3. Continue as a Guest");
            System.out.print("Choice: ");
            int choice = scan.nextInt();

            switch (choice) {
                case 1:
                    login();
                    break;

                case 2:
                    register();
                    break;

                case 3:
                    mainMenu();
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void login() {
        System.out.print("\nUsername: ");
        String username = scan.next();
        System.out.print("Password: ");
        String password = scan.next();

        for (int i = 0; i < count; i++) {
            if (username.equals(user[i]) && password.equals(pass[i])) {
                System.out.println("You have successfully logged in");
                mainMenu();
                exit = true;
                return;
            }
        }
        System.out.println("Invalid username or password. Please try again.");
    }

    private void register() {
        System.out.print("\nUsername: ");
        String newUsername = scan.next();

        while (!validPass()) {
            System.out.println("");
        }

        user[count] = newUsername;
        System.out.println("Account created successfully!\n");
        count++;
    }

    private boolean validPass() {
        System.out.print("Password (at least 8 characters, containing uppercase, lowercase, digit, and symbol): ");
        String newPassword = scan.next();

        if (newPassword.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }

        boolean checkUpper = false;
        boolean checkLower = false;
        boolean checkDigit = false;
        boolean checkSymbol = false;

        for (char pwChar : newPassword.toCharArray()) {
            if (Character.isUpperCase(pwChar)) {
                checkUpper = true;
            } else if (Character.isLowerCase(pwChar)) {
                checkLower = true;
            } else if (Character.isDigit(pwChar)) {
                checkDigit = true;
            } else {
                checkSymbol = true;
            }
        }

        if (checkUpper && checkLower && checkDigit && checkSymbol) {
            pass[count] = newPassword;
            return true;
        } else {
            System.out.println("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one symbol.");
            return false;
        }
    }

    public void mainMenu() {
        System.out.println("\nThis is the main menu");
    }
}
