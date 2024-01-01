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
    private List<Map.Entry<LocalDate, List<FinancialData>>> allEntries;
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

    private void serializeUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("UserData.ser"))) {
            oos.writeObject(users);
        } catch (IOException e) {
        }
    }

    private void loadUserData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("UserData.ser"))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                users.putAll((Map<String, UserData>) obj);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException | ClassNotFoundException e) {
        }
    }

    private void displayMenu() {
        while (true) {
            System.out.println(MENU_HEADER + """
                       Menu: 1. Login 
                             2. Create an Account 
                             3. Continue as a Guest
                             4. Exit Program""");
            try {
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
            } catch (InputMismatchException e) {
                System.out.println("Invalid Option, please try again.");
                scan.nextLine();
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

    private void mainMenu() {
        while (true) {
            System.out.println(MENU_HEADER + """
                       Menu: 1. Record Expenses
                             2. Record Income 
                             3. View Record
                             4. Edit Record
                             5. Remove Record                
                             6. Logout""");
            try {
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
                        editRecord();
                    case 5 ->
                        removeRecord();
                    case 6 ->
                        logout();
                    default ->
                        System.out.println("Invalid option, please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid option, please try again.");
                scan.nextLine();
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
            System.out.println("Invalid input. Please try again.");
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
            System.out.println("Invalid input. Please try again.");
            scan.nextLine();
        }
        serializeUserData();
    }

    private void view() {
        System.out.println("""
                           Menu: 1. Info
                                 2. Recap""");
        try {
            System.out.print("Choose an Option: ");
            int option = scan.nextInt();
            System.out.println("---------------------------");

            switch (option) {
                case 1 ->
                    info();
                case 2 ->
                    recap();
                default ->
                    System.out.println("Invalid option, please try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid option, please try again.");
        }
    }

    private void editRecord() {
        try {
            System.out.print("Enter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            scan.nextLine();
            LocalDate date = LocalDate.parse(dateInput, dateFormatter);

            Optional<Map.Entry<LocalDate, List<FinancialData>>> entryOptional = allEntries.stream()
                    .filter(entry -> entry.getKey().equals(date))
                    .findFirst();

            if (entryOptional.isPresent()) {
                Map.Entry<LocalDate, List<FinancialData>> entry = entryOptional.get();

                entry.getValue().forEach(data
                        -> System.out.println("Title: " + data.getTitle() + ", Amount: Rp " + data.getAmount()));

                System.out.print("Enter the title to edit: ");
                String titleToEdit = scan.nextLine();  // Change from scan.next() to scan.nextLine()

                Optional<FinancialData> dataOptional = entry.getValue().stream()
                        .filter(data -> data.getTitle().equals(titleToEdit))
                        .findFirst();

                if (dataOptional.isPresent()) {
                    FinancialData data = dataOptional.get();

                    System.out.print("Enter the new title: ");
                    String newTitle = scan.nextLine();  // Change from scan.next() to scan.nextLine()

                    System.out.print("Enter the new amount: ");
                    long newAmount = scan.nextLong();

                    data.setTitle(newTitle);
                    data.setAmount(newAmount);

                    System.out.println("Record edited successfully!");
                    serializeUserData();
                } else {
                    System.out.println("Title not found.");
                }
            } else {
                System.out.println("Date not found.");
            }
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            scan.nextLine();
            scan.nextLine();
        }
    }

    private void removeRecord() {
        try {
            System.out.print("Enter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            scan.nextLine();
            LocalDate date = LocalDate.parse(dateInput, dateFormatter);

            Optional<Map.Entry<LocalDate, List<FinancialData>>> entryOptional = allEntries.stream()
                    .filter(entry -> entry.getKey().equals(date))
                    .findFirst();

            if (entryOptional.isPresent()) {
                Map.Entry<LocalDate, List<FinancialData>> entry = entryOptional.get();

                entry.getValue().forEach(data
                        -> System.out.println("Title: " + data.getTitle() + ", Amount: Rp " + data.getAmount()));

                System.out.print("Enter the title to remove: ");
                String titleToRemove = scan.nextLine();

                entry.getValue().removeIf(data -> data.getTitle().equals(titleToRemove));

                System.out.println("Record removed successfully!");
                serializeUserData();
            } else {
                System.out.println("Date not found.");
            }
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            scan.nextLine();
            scan.nextLine();
        }
    }

    private void info() {
        UserData user = users.get(currentUser.getUsername());
        allEntries = new ArrayList<>(user.getFinancialData().entrySet());
        allEntries.sort(Comparator.comparing(Map.Entry::getKey));

        System.out.println("Expense Record:");
        for (Map.Entry<LocalDate, List<FinancialData>> entry : allEntries) {
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

        System.out.println("---------------------------");
        System.out.println("Income Record:");
        for (Map.Entry<LocalDate, List<FinancialData>> entry : allEntries) {
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

    private void recap() {
        UserData user = users.get(currentUser.getUsername());
        allEntries = new ArrayList<>(user.getFinancialData().entrySet());
        allEntries.sort(Comparator.comparing(Map.Entry::getKey));

        System.out.println(" ---------------------------------------------------------------------");
        System.out.println("|        Date        |         Title         |         Amount         |");
        System.out.println(" ---------------------------------------------------------------------");

        long totalIncome = 0;
        long totalExpense = 0;

        for (Map.Entry<LocalDate, List<FinancialData>> entry : allEntries) {
            LocalDate date = entry.getKey();
            List<FinancialData> dataList = entry.getValue();

            if (!dataList.isEmpty()) {
                System.out.println(String.format("| %-18s | %-21s | %-22s |", date.format(dateFormatter), dataList.get(0).getTitle(), formatAmount(dataList.get(0).getAmount(), dataList.get(0) instanceof ExpenseData)));

                for (int i = 1; i < dataList.size(); i++) {
                    System.out.println(String.format("| %-18s | %-21s | %-22s |", "", dataList.get(i).getTitle(), formatAmount(dataList.get(i).getAmount(), dataList.get(i) instanceof ExpenseData)));
                }
            }

            totalIncome += dataList.stream().filter(d -> d instanceof IncomeData).mapToLong(FinancialData::getAmount).sum();
            totalExpense += dataList.stream().filter(d -> d instanceof ExpenseData).mapToLong(FinancialData::getAmount).sum();
        }

        System.out.println(" ---------------------------------------------------------------------");
        System.out.println(String.format("| Total Income       | Rp %-30d              |", totalIncome));
        System.out.println(String.format("| Total Expense      | Rp %-30d              |", totalExpense));
        System.out.println(" ---------------------------------------------------------------------");
        System.out.println(String.format("| Net Income         | %s Rp %-28d              |", (totalIncome - totalExpense >= 0) ? "+" : "-", Math.abs(totalIncome - totalExpense)));
        System.out.println(" ---------------------------------------------------------------------");
    }

    private String formatAmount(long amount, boolean isExpense) {
        return (isExpense ? "- " : "+ ") + "Rp " + Math.abs(amount);
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

        private String title;
        private long amount;

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

        public void setAmount(long amount) {
            this.amount = amount;
        }

        public void setTitle(String title) {
            this.title = title;
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
