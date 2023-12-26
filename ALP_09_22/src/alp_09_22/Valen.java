package alp_09_22;

import java.text.NumberFormat;
import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Valen {

    private int count;
    private final Scanner scan;
    private final String[] user;
    private final String[] pass;
    private final Map<LocalDate, List<incomeData>> incomeMap;
    private final Map<LocalDate, List<expenseData>> expenseMap;
    private final DateTimeFormatter dateFormatter;
    private static final String MENU_HEADER = """
                                     ---------------------------
                                          Welcome to Finner!
                                     ---------------------------
                                     """;

    public Valen() {
        count = 0;
        scan = new Scanner(System.in);
        user = new String[10];
        pass = new String[10];
        incomeMap = new HashMap<>();
        expenseMap = new HashMap<>();
        dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public static void main(String[] args) {
        Valen func = new Valen();
        func.firstMenu();
    }

    public void firstMenu() {
        while (true) {
            System.out.println(MENU_HEADER + """
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
                case 4 ->
                    exit();
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
    
    private void exit() {
        System.out.println("\nThank you for using Finner!");
        System.exit(0);
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
            System.out.println(MENU_HEADER + """
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
        try {
            System.out.print("\nEnter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            LocalDate date = LocalDate.parse(dateInput, dateFormatter);

            System.out.print("Enter the expense title: ");
            String expenseTitle = scan.next() + scan.nextLine();
            System.out.print("Enter the expense amount: ");
            long expenseAmount = scan.nextLong();

            expenseData data = new expenseData(expenseTitle, expenseAmount);
            expenseMap.computeIfAbsent(date, k -> new ArrayList<>()).add(data);

            System.out.println("Expense recorded successfully!");

            extraRecord();
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            scan.nextLine();
        }
    }

    private void income() {
        try {
            System.out.print("\nEnter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            LocalDate date = LocalDate.parse(dateInput, dateFormatter);

            System.out.print("Enter the income title: ");
            String incomeTitle = scan.next() + scan.nextLine();
            System.out.print("Enter the income amount: ");
            long incomeAmount = scan.nextLong();

            incomeData data = new incomeData(incomeTitle, incomeAmount);
            incomeMap.computeIfAbsent(date, k -> new ArrayList<>()).add(data);

            System.out.println("Income recorded successfully!");

            extraRecord();
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            scan.nextLine();
        }
    }

    private void view() {
        System.out.println("\nExpense Record:");
        for (Map.Entry<LocalDate, List<expenseData>> entry : expenseMap.entrySet()) {
            System.out.println("Date: " + entry.getKey().format(dateFormatter));
            for (expenseData data : entry.getValue()) {
                System.out.println("Title: " + data.getTitle() + ", Amount:  " + data.getAmount());
            }
            System.out.println("");
        }

        System.out.println("Income Record: ");
        for (Map.Entry<LocalDate, List<incomeData>> entry : incomeMap.entrySet()) {
            System.out.println("Date: " + entry.getKey().format(dateFormatter));
            for (incomeData data : entry.getValue()) {
                System.out.println("Title: " + data.getTitle() + ", Amount:  " + data.getAmount());
            }
        }
    }

    private void logout() {
        System.out.print("\nAre you sure you want to log out? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            firstMenu();
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu();
        } else {
            System.out.println("Invalid Option!\n");
            logout();
        }
    }

    private void extraRecord() {
        System.out.print("\nDo you want to record another one? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            expenses();
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu();
        } else {
            System.out.println("Invalid Option, please use Y/N.");
            extraRecord();
        }
    }

    private static class expenseData {

        private final String title;
        private final long amount;

        public expenseData(String title, long amount) {
            this.title = title;
            this.amount = amount;
        }

        public String getTitle() {
            return title;
        }

        public String getAmount() {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String moneyString = formatter.format(amount);
            return moneyString;
        }
    }

    private static class incomeData {

        private final String title;
        private final long amount;

        public incomeData(String title, long amount) {
            this.title = title;
            this.amount = amount;
        }

        public String getTitle() {
            return title;
        }

        public String getAmount() {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String moneyString = formatter.format(amount);
            return moneyString;
        }
    }
}
