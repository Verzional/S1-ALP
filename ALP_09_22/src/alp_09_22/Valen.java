package alp_09_22;

import java.io.*;
import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Valen {

    private UserData currentUser;
    private int count;
    private final Scanner scan;
    private final String[] pass;
    private final Map<String, UserData> users;
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
        Valen finner = new Valen();
        finner.displayMenu();
    }

    private void serializeUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("UserData.ser"))) {
            oos.writeObject(users);
            System.out.println("Serialization complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("UserData.ser"))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                users.putAll((Map<String, UserData>) obj);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: UserData.ser");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void displayMenu() {
        while (true) {
            System.out.println(MENU_HEADER + """
                       Menu: 1. Login 
                             2. Create an Account 
                             3. Continue as a Guest
                             4. Exit Program""");
            System.out.print("Choose an Option: ");
            int option = scan.nextInt();
            System.out.println("---------------------------");

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
        System.out.print("Username: ");
        String username = scan.next();
        System.out.print("Password: ");
        String password = scan.next();

        UserData user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("You have successfully logged in!");
            currentUser = user;
            mainMenu();
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private void register() {
        System.out.print("Username: ");
        String newUsername = scan.next();

        while (!validPass()) {
        }

        users.put(newUsername, new UserData(newUsername, pass[count]));
        System.out.println("Account created successfully!");
        count++;
    }

    private void exit() {
        serializeUserData();
        System.out.println("Thank you for using Finner!");
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

    private void mainMenu() {
        while (true) {
            System.out.println(MENU_HEADER + """
                       Menu: 1. Record Expenses
                             2. Record Income 
                             3. View Record
                             4. Logout""");
            System.out.print("Choose an Option: ");
            int option = scan.nextInt();
            System.out.println("---------------------------");

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
        UserData user = users.get(currentUser.getUsername());

        try {
            System.out.print("Enter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            LocalDate date = LocalDate.parse(dateInput, dateFormatter);

            System.out.print("Enter the expense title: ");
            String expenseTitle = scan.next() + scan.nextLine();
            System.out.print("Enter the expense amount: ");
            long expenseAmount = scan.nextLong();

            FinancialData data = new ExpenseData(expenseTitle, expenseAmount);

            List<FinancialData> dataList = user.getFinancialData().get(date);
            if (dataList == null) {
                dataList = new ArrayList<>();
                user.getFinancialData().put(date, dataList);
            }

            dataList.add(data);

            System.out.println("Expense recorded successfully!");

            extraRecordExpense();
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            scan.nextLine();
        }
        serializeUserData();
    }

    private void income() {
        UserData user = users.get(currentUser.getUsername());

        try {
            System.out.print("Enter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            LocalDate date = LocalDate.parse(dateInput, dateFormatter);

            System.out.print("Enter the income title: ");
            String incomeTitle = scan.next() + scan.nextLine();
            System.out.print("Enter the income amount: ");
            long incomeAmount = scan.nextLong();

            FinancialData data = new IncomeData(incomeTitle, incomeAmount);

            List<FinancialData> dataList = user.getFinancialData().get(date);
            if (dataList == null) {
                dataList = new ArrayList<>();
                user.getFinancialData().put(date, dataList);
            }

            dataList.add(data);

            System.out.println("Income recorded successfully!");

            extraRecordIncome();
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            scan.nextLine();
        }
        serializeUserData();
    }

    private void view() {
        UserData user = users.get(currentUser.getUsername());

        System.out.println("Expense Record:");
        for (Map.Entry<LocalDate, List<FinancialData>> entry : user.getFinancialData().entrySet()) {
            if (entry.getValue().stream().anyMatch(data -> data instanceof ExpenseData)) {
                System.out.println("Date: " + entry.getKey().format(dateFormatter));
                for (FinancialData data : entry.getValue()) {
                    if (data instanceof ExpenseData) {
                        System.out.println("Title: " + data.getTitle() + ", Amount: Rp " + data.getAmount());
                    }
                }
                System.out.println("");
            }
        }

        System.out.println("Income Record: ");
        for (Map.Entry<LocalDate, List<FinancialData>> entry : user.getFinancialData().entrySet()) {
            if (entry.getValue().stream().anyMatch(data -> data instanceof IncomeData)) {
                System.out.println("Date: " + entry.getKey().format(dateFormatter));
                for (FinancialData data : entry.getValue()) {
                    if (data instanceof IncomeData) {
                        System.out.println("Title: " + data.getTitle() + ", Amount: Rp " + data.getAmount());
                    }
                }
                System.out.println("");
            }
        }
    }

    private void logout() {
        System.out.print("Are you sure you want to log out? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            displayMenu();
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu();
        } else {
            System.out.println("Invalid Option!");
            logout();
        }
    }

    private void extraRecordExpense() {
        System.out.print("\nDo you want to record another one? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            expenses();
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu();
        } else {
            System.out.println("Invalid Option, please use Y/N.");
            extraRecordExpense();
        }
    }

    private void extraRecordIncome() {
        System.out.print("\nDo you want to record another one? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            income();
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu();
        } else {
            System.out.println("Invalid Option, please use Y/N.");
            extraRecordIncome();
        }
    }

    private static class UserData implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String username;
        private final String password;
        private final Map<LocalDate, List<FinancialData>> financialData;

        public UserData(String username, String password) {
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

        public Map<LocalDate, List<FinancialData>> getFinancialData() {
            return financialData;
        }
    }

    private static class FinancialData implements Serializable {

        private static final long serialVersionUID = 2L;

        private final String title;
        private final long amount;

        public FinancialData(String title, long amount) {
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

    private static class ExpenseData extends FinancialData implements Serializable {

        private static final long serialVersionUID = 3L;

        public ExpenseData(String title, long amount) {
            super(title, amount);
        }
    }

    private static class IncomeData extends FinancialData implements Serializable {

        private static final long serialVersionUID = 4L;

        public IncomeData(String title, long amount) {
            super(title, amount);
        }
    }
}
