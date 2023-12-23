package alp_09_22;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Valen {
    
    private int count;
    private Scanner scan;
    private String[] user;
    private String[] pass;
    private Map<LocalDate, Long> expensesMap;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public Valen() {
        count = 0;
        scan = new Scanner(System.in);
        user = new String[10];
        pass = new String[10];
        expensesMap = new HashMap<>();
        
    }

    public static void main(String[] args) {
        Valen func = new Valen();
        func.firstPage();
    }

    public void firstPage() {
        while (true) {
            System.out.println("""
                               ---------------------------
                                    Welcome to Finner!
                               ---------------------------
                               Menu: 1. Login
                                     2. Create an Account 
                                     3. Continue as a Guest
                                     4. Exit Program""");
            System.out.print("Choose an Option: ");
            int option = scan.nextInt();

            switch (option) {
                case 1 ->
                    login();

                case 2 ->
                    register();

                case 3 ->
                    mainMenu();

                case 4 -> {
                    System.out.println("\nThank you for using Finner!");
                    System.exit(0);
                }

                default ->
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
        while (true) {
            System.out.println("""
                               ---------------------------
                                    Welcome to Finner!
                               ---------------------------
                               Menu: 1. Record Expenses
                                     2. Record Income 
                                     3. View Record
                                     4. Logout""");
            System.out.print("Choose an Option: ");
            int option = scan.nextInt();

            switch (option) {
                case 1 ->
                    expenses();

                case 2 ->
                    income();

                case 3 ->
                    view();

                case 4 ->
                    logout();

                default ->
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void expenses() {
        System.out.println("\nRecord Expenses:");

        System.out.print("Enter the date (DD-MM-YYYY): ");
        String dateInput = scan.next();
        LocalDate date = LocalDate.parse(dateInput, dateFormatter);

        System.out.print("Enter the expense amount: ");
        long expenseAmount = scan.nextLong();

        expensesMap.put(date, expensesMap.getOrDefault(date, 0L) + expenseAmount);

        System.out.println("Expense recorded successfully!");
    }

    private void income() {

    }

    private void view() {
        System.out.println("\nView Records:");

        System.out.print("Enter the date to view expenses (DD-MM-YYYY): ");
        String dateInput = scan.next();
        LocalDate date = LocalDate.parse(dateInput, dateFormatter);

        long expensesForDate = expensesMap.getOrDefault(date, 0L);
        System.out.println("Expenses for " + date + ": Rp" + expensesForDate);
    }

    private void logout() {
        System.out.print("\nAre you sure you want to log out? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            firstPage();
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu();
        } else {
            System.out.println("Invalid Option!\n");
            logout();
        }
    }
}
