package alp_09_22;

import java.io.*;
import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Valen {

    private int count;
    private final Scanner scan;
    private final String[] pass;
    private final Map<String, userData> users;
    private final DateTimeFormatter dateFormatter;
    private static final String MENU_HEADER = """
                                     ---------------------------
                                          Welcome to Finner!
                                     ---------------------------
                                     """;

    public Valen() {
        count = 0;
        scan = new Scanner(System.in);
        pass = new String[10];
        users = new HashMap<>();
        dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        loadUserData();
    }

    public static void main(String[] args) {
        Valen func = new Valen();
        func.firstMenu();
    }

    private void serializeUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("userData.ser"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("userData.ser"))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                users.putAll((Map<String, userData>) obj);
            }
        } catch (FileNotFoundException e) {
            // Ignore if the file doesn't exist, it will be created when saving
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
                    mainMenu("Guest");
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

        userData user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("You have successfully logged in!");
            mainMenu(username);
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private void register() {
        System.out.print("\nUsername: ");
        String newUsername = scan.next();

        while (!validPass()) {
            System.out.println("");
        }

        users.put(newUsername, new userData(newUsername, pass[count]));
        System.out.println("Account created successfully!");
        count++;
    }

    private void exit() {
        serializeUserData();
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

    private void mainMenu(String username) {
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
                    expenses(username);
                case 2 ->
                    income(username);
                case 3 ->
                    view(username);
                case 4 ->
                    logout(username);
                default ->
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void expenses(String username) {
        userData user = users.get(username);
        try {
            System.out.print("\nEnter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            LocalDate date = LocalDate.parse(dateInput, dateFormatter);

            System.out.print("Enter the expense title: ");
            String expenseTitle = scan.next() + scan.nextLine();
            System.out.print("Enter the expense amount: ");
            long expenseAmount = scan.nextLong();

            financialData data = new expenseData(expenseTitle, expenseAmount);
            user.getFinancialData().computeIfAbsent(date, k -> new ArrayList<>()).add(data);

            System.out.println("Expense recorded successfully!");

            extraRecordExpense(username);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            scan.nextLine();
        }
    }

    private void income(String username) {
        userData user = users.get(username);
        try {
            System.out.print("\nEnter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            LocalDate date = LocalDate.parse(dateInput, dateFormatter);

            System.out.print("Enter the income title: ");
            String incomeTitle = scan.next() + scan.nextLine();
            System.out.print("Enter the income amount: ");
            long incomeAmount = scan.nextLong();

            financialData data = new incomeData(incomeTitle, incomeAmount);
            user.getFinancialData().computeIfAbsent(date, k -> new ArrayList<>()).add(data);

            System.out.println("Income recorded successfully!");

            extraRecordIncome(username);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            scan.nextLine();
        }
    }

    private void view(String username) {
        userData user = users.get(username);

        System.out.println("\nExpense Record:");
        for (Map.Entry<LocalDate, List<financialData>> entry : user.getFinancialData().entrySet()) {
            if (entry.getValue().stream().anyMatch(data -> data instanceof expenseData)) {
                System.out.println("Date: " + entry.getKey().format(dateFormatter));
                for (financialData data : entry.getValue()) {
                    if (data instanceof expenseData) {
                        System.out.println("Title: " + data.getTitle() + ", Amount: Rp " + data.getAmount());
                    }
                }
                System.out.println("");
            }
        }

        System.out.println("Income Record: ");
        for (Map.Entry<LocalDate, List<financialData>> entry : user.getFinancialData().entrySet()) {
            if (entry.getValue().stream().anyMatch(data -> data instanceof incomeData)) {
                System.out.println("Date: " + entry.getKey().format(dateFormatter));
                for (financialData data : entry.getValue()) {
                    if (data instanceof incomeData) {
                        System.out.println("Title: " + data.getTitle() + ", Amount: Rp " + data.getAmount());
                    }
                }
                System.out.println("");
            }
        }
    }

    private void logout(String username) {
        System.out.print("\nAre you sure you want to log out? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            firstMenu();
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu(username);
        } else {
            System.out.println("Invalid Option!\n");
            logout(username);
        }
    }

    private void extraRecordExpense(String username) {
        System.out.print("\nDo you want to record another one? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            expenses(username);
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu(username); // Pass the username to mainMenu
        } else {
            System.out.println("Invalid Option, please use Y/N.");
            extraRecordExpense(username);

        }
    }

    private void extraRecordIncome(String username) {
        System.out.print("\nDo you want to record another one? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            income(username);
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu(username); // Pass the username to mainMenu
        } else {
            System.out.println("Invalid Option, please use Y/N.");
            extraRecordIncome(username);

        }
    }

    private static class userData implements Serializable {

        private final String username;
        private final String password;
        private final Map<LocalDate, List<financialData>> financialData;

        public userData(String username, String password) {
            this.username = username;
            this.password = password;
            this.financialData = new HashMap<>();
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public Map<LocalDate, List<financialData>> getFinancialData() {
            return financialData;
        }
    }

    private static class financialData implements Serializable {

        private final String title;
        private final long amount;

        public financialData(String title, long amount) {
            this.title = title;
            this.amount = amount;
        }

        public String getTitle() {
            return title;
        }

        public long getAmount() {
            return amount;
        }
    }

    private static class expenseData extends financialData {

        public expenseData(String title, long amount) {
            super(title, amount);
        }
    }

    private static class incomeData extends financialData {

        public incomeData(String title, long amount) {
            super(title, amount);
        }
    }

}
