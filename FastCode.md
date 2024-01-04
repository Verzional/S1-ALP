package alp_09_22;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.NumberFormat;

public class Finner {

    private List<Map.Entry<LocalDate, List<FinancialData>>> allEntries;
    private UserData currentUser;
    private int count;
    private final Scanner scan;
    private final Map<String, String> pass;
    private final Map<String, UserData> users;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String USER_DATA_FILE = "UserData.ser";
    private static final String MENU_HEADER = """
                                     ---------------------------
                                          Welcome to Finner!
                                     ---------------------------
                                     """;
    private static final String BALANCE_SHEET_HEADER = """
                                                       
                                                       ---------------------------------------------------------------------
                                                      |        Date        |         Title         |         Amount         |
                                                       --------------------------------------------------------------------- """;

    public Finner() {
        count = 0;
        scan = new Scanner(System.in);
        pass = new HashMap<>();
        users = new HashMap<>();
        loadUserData();
    }

    private void serializeUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error serializing user data: " + e.getMessage());
        }
    }

    private void loadUserData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                users.putAll((Map<String, UserData>) obj);
            }
        } catch (FileNotFoundException e) {
            System.err.println("User data file not found, starting with an empty user map.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Finner app = new Finner();
        app.displayMenu();
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
                        System.out.println("Invalid option choice, please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid option choice, please try again.");
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
            System.out.println("Invalid username or password, please try again.");
        }
    }

    private void register() {
        System.out.print("Username: ");
        String newUsername = scan.next();

        while (!validPass(newUsername)) {
        }

        users.put(newUsername, new UserData(newUsername, pass.get(newUsername)));
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
                        System.out.println("Invalid option choice, please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid option choice, please try again.");
                scan.nextLine();
            }
        }
    }

    private void expenses() {
        UserData user = users.get(currentUser.getUsername());

        try {
            System.out.print("Enter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);

            System.out.print("Enter the expense title: ");
            String expenseTitle = scan.next() + scan.nextLine();

            long expenseAmount;
            do {
                System.out.print("Enter the expense amount: ");
                expenseAmount = scan.nextLong();

                if (expenseAmount < 0) {
                    System.out.println("Invalid amount, please enter a positive amount.");
                }
            } while (expenseAmount < 0);

            FinancialData data = new ExpenseData(expenseTitle, expenseAmount);

            List<FinancialData> dataList = user.getFinancialData().get(date);
            if (dataList == null) {
                dataList = new ArrayList<>();
                user.getFinancialData().put(date, dataList);
            }
            dataList.add(data);

            System.out.println("Expense recorded successfully!");

            extraRecordExpense();
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format, please use DD-MM-YYYY.");
            scan.nextLine();
        }
        serializeUserData();
    }

    private void income() {
        UserData user = users.get(currentUser.getUsername());

        try {
            System.out.print("Enter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);

            System.out.print("Enter the income title: ");
            String incomeTitle = scan.next() + scan.nextLine();

            long incomeAmount;
            do {
                System.out.print("Enter the income amount: ");
                incomeAmount = scan.nextLong();

                if (incomeAmount < 0) {
                    System.out.println("Invalid amount, please enter a positive amount.");
                }
            } while (incomeAmount < 0);

            FinancialData data = new IncomeData(incomeTitle, incomeAmount);

            List<FinancialData> dataList = user.getFinancialData().get(date);
            if (dataList == null) {
                dataList = new ArrayList<>();
                user.getFinancialData().put(date, dataList);
            }
            dataList.add(data);

            System.out.println("Income recorded successfully!");

            extraRecordIncome();
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format, please use DD-MM-YYYY.");
            scan.nextLine();
        }
        serializeUserData();
    }

    private void view() {
        System.out.println("""
                           Menu: 1. Find Record
                                 2. All Records
                                 3. Full Balance Sheet
                                 4. Yearly Balance Sheet
                                 5. Monthly Balance Sheet""");
        try {
            System.out.print("Choose an Option: ");
            int option = scan.nextInt();
            System.out.println("---------------------------");

            switch (option) {
                case 1 ->
                    findRecord();
                case 2 ->
                    allRecords();
                case 3 ->
                    fullBalanceSheet();
                case 4 ->
                    yearlyBalanceSheet();
                case 5 ->
                    monthlyBalanceSheet();
                default ->
                    System.out.println("Invalid option choice, please try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid option choice, please try again.");
        }
    }

    private void editRecord() {
        try {
            UserData user = users.get(currentUser.getUsername());
            allEntries = new ArrayList<>(user.getFinancialData().entrySet());
            allEntries.sort(Comparator.comparing(Map.Entry::getKey));

            System.out.print("Enter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            scan.nextLine();
            LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);

            Optional<Map.Entry<LocalDate, List<FinancialData>>> entryOptional = allEntries.stream()
                    .filter(entry -> entry.getKey().equals(date))
                    .findFirst();

            if (entryOptional.isPresent()) {
                Map.Entry<LocalDate, List<FinancialData>> entry = entryOptional.get();

                entry.getValue().forEach(data -> {
                    String formattedAmount = formatAmount(data.getAmount(), data instanceof ExpenseData);
                    System.out.println("Title: " + data.getTitle() + ", Amount: " + formattedAmount);
                });

                System.out.print("Enter the title to edit: ");
                String titleToEdit = scan.nextLine();

                Optional<FinancialData> dataOptional = entry.getValue().stream()
                        .filter(data -> data.getTitle().equals(titleToEdit))
                        .findFirst();

                if (dataOptional.isPresent()) {
                    FinancialData data = dataOptional.get();

                    System.out.print("Enter the new title: ");
                    String newTitle = scan.nextLine();

                    long newAmount;
                    do {
                        System.out.print("Enter the new amount: ");
                        newAmount = scan.nextLong();

                        if (newAmount < 0) {
                            System.out.println("Invalid amount, please enter a positive amount.");
                        }
                    } while (newAmount < 0);

                    data.setTitle(newTitle);
                    data.setAmount(newAmount);

                    System.out.println("Record edited successfully!");
                    serializeUserData();
                } else {
                    System.out.println("Title not found.");
                }
            } else {
                System.out.println("No records found for the given date.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format, please use DD-MM-YYYY.");
            mainMenu();
        }
    }

    private void removeRecord() {
        try {
            UserData user = users.get(currentUser.getUsername());
            allEntries = new ArrayList<>(user.getFinancialData().entrySet());
            allEntries.sort(Comparator.comparing(Map.Entry::getKey));

            System.out.print("Enter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            scan.nextLine();
            LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);

            Optional<Map.Entry<LocalDate, List<FinancialData>>> entryOptional = allEntries.stream()
                    .filter(entry -> entry.getKey().equals(date))
                    .findFirst();

            if (entryOptional.isPresent()) {
                Map.Entry<LocalDate, List<FinancialData>> entry = entryOptional.get();

                entry.getValue().forEach(data -> {
                    String formattedAmount = formatAmount(data.getAmount(), data instanceof ExpenseData);
                    System.out.println("Title: " + data.getTitle() + ", Amount: " + formattedAmount);
                });

                System.out.print("Enter the title to remove: ");
                String titleToRemove = scan.nextLine();

                boolean removed = entry.getValue().removeIf(data -> data.getTitle().equals(titleToRemove));

                if (removed) {
                    System.out.println("Record removed successfully!");
                    serializeUserData();
                } else {
                    System.out.println("Title not found.");
                }
            } else {
                System.out.println("No records found for the given date.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format, please use DD-MM-YYYY.");
            mainMenu();
        }
    }

    private void findRecord() {
        try {
            UserData user = users.get(currentUser.getUsername());
            allEntries = new ArrayList<>(user.getFinancialData().entrySet());
            allEntries.sort(Comparator.comparing(Map.Entry::getKey));

            System.out.print("Enter the date (DD-MM-YYYY): ");
            String dateInput = scan.next();
            scan.nextLine();
            LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);

            Optional<Map.Entry<LocalDate, List<FinancialData>>> entryOptional = allEntries.stream()
                    .filter(entry -> entry.getKey().equals(date))
                    .findFirst();

            if (entryOptional.isPresent()) {
                Map.Entry<LocalDate, List<FinancialData>> entry = entryOptional.get();

                entry.getValue().forEach(data -> {
                    String formattedAmount = formatAmount(data.getAmount(), data instanceof ExpenseData);
                    System.out.println("Title: " + data.getTitle() + ", Amount: " + formattedAmount);
                });
            } else {
                System.out.println("No records found for the given date.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format, please use DD-MM-YYYY.");
            mainMenu();
        }
    }

    private void allRecords() {
        UserData user = users.get(currentUser.getUsername());
        allEntries = new ArrayList<>(user.getFinancialData().entrySet());
        allEntries.sort(Comparator.comparing(Map.Entry::getKey));

        System.out.println("Expense Record:");
        for (Map.Entry<LocalDate, List<FinancialData>> entry : allEntries) {
            if (entry.getValue().stream().anyMatch(data -> data instanceof ExpenseData)) {
                System.out.println("Date: " + entry.getKey().format(DATE_FORMATTER));
                for (FinancialData data : entry.getValue()) {
                    if (data instanceof ExpenseData) {
                        String formattedAmount = formatAmount(data.getAmount(), true);
                        System.out.println("Title: " + data.getTitle() + ", Amount: " + formattedAmount);
                    }
                }
                System.out.println("");
            }
        }

        System.out.println("---------------------------");
        System.out.println("Income Record:");
        for (Map.Entry<LocalDate, List<FinancialData>> entry : allEntries) {
            if (entry.getValue().stream().anyMatch(data -> data instanceof IncomeData)) {
                System.out.println("Date: " + entry.getKey().format(DATE_FORMATTER));
                for (FinancialData data : entry.getValue()) {
                    if (data instanceof IncomeData) {
                        String formattedAmount = formatAmount(data.getAmount(), false);
                        System.out.println("Title: " + data.getTitle() + ", Amount: " + formattedAmount);
                    }
                }
                System.out.println("");
            }
        }
    }

    private void fullBalanceSheet() {
        UserData user = users.get(currentUser.getUsername());
        allEntries = new ArrayList<>(user.getFinancialData().entrySet());
        allEntries.sort(Comparator.comparing(Map.Entry::getKey));

        System.out.println(BALANCE_SHEET_HEADER);

        long totalIncome = 0;
        long totalExpense = 0;

        for (Map.Entry<LocalDate, List<FinancialData>> entry : allEntries) {
            LocalDate date = entry.getKey();
            List<FinancialData> dataList = entry.getValue();
            if (!dataList.isEmpty()) {
                System.out.println(String.format("| %-18s | %-21s | %-22s |", date.format(DATE_FORMATTER), dataList.get(0).getTitle(), formatAmount(dataList.get(0).getAmount(), dataList.get(0) instanceof ExpenseData)));
                for (int i = 1; i < dataList.size(); i++) {
                    System.out.println(String.format("| %-18s | %-21s | %-22s |", "", dataList.get(i).getTitle(), formatAmount(dataList.get(i).getAmount(), dataList.get(i) instanceof ExpenseData)));
                }
            }
            totalIncome += dataList.stream().filter(d -> d instanceof IncomeData).mapToLong(FinancialData::getAmount).sum();
            totalExpense += dataList.stream().filter(d -> d instanceof ExpenseData).mapToLong(FinancialData::getAmount).sum();
        }
        System.out.println(" ---------------------------------------------------------------------");
        System.out.println(String.format("| Total Income       | %-32s               |", formatAmount(totalIncome, false)));
        System.out.println(String.format("| Total Expense      | %-32s               |", formatAmount(totalExpense, true)));
        System.out.println(" ---------------------------------------------------------------------");
        System.out.println(String.format("| Net Cash Flow      | %-32s               |", formatNetCashFlow(totalIncome - totalExpense)));
        System.out.println(" ---------------------------------------------------------------------\n");
    }

    private void yearlyBalanceSheet() {
        try {
            UserData user = users.get(currentUser.getUsername());
            allEntries = new ArrayList<>(user.getFinancialData().entrySet());
            allEntries.sort(Comparator.comparing(Map.Entry::getKey));

            System.out.print("Enter the year (YYYY): ");
            int desiredYear = scan.nextInt();
            System.out.println("---------------------------");

            System.out.println(BALANCE_SHEET_HEADER);

            long totalIncome = 0;
            long totalExpense = 0;

            for (Map.Entry<LocalDate, List<FinancialData>> entry : allEntries) {
                LocalDate date = entry.getKey();
                if (date.getYear() == desiredYear) {
                    List<FinancialData> dataList = entry.getValue();
                    if (!dataList.isEmpty()) {
                        System.out.println(String.format("| %-18s | %-21s | %-22s |", date.format(DATE_FORMATTER), dataList.get(0).getTitle(), formatAmount(dataList.get(0).getAmount(), dataList.get(0) instanceof ExpenseData)));
                        for (int i = 1; i < dataList.size(); i++) {
                            System.out.println(String.format("| %-18s | %-21s | %-22s |", "", dataList.get(i).getTitle(), formatAmount(dataList.get(i).getAmount(), dataList.get(i) instanceof ExpenseData)));
                        }
                    }
                    totalIncome += dataList.stream().filter(d -> d instanceof IncomeData).mapToLong(FinancialData::getAmount).sum();
                    totalExpense += dataList.stream().filter(d -> d instanceof ExpenseData).mapToLong(FinancialData::getAmount).sum();
                }
            }
            System.out.println(" ---------------------------------------------------------------------");
            System.out.println(String.format("| Total Income       | %-32s               |", formatAmount(totalIncome, false)));
            System.out.println(String.format("| Total Expense      | %-32s               |", formatAmount(totalExpense, true)));
            System.out.println(" ---------------------------------------------------------------------");
            System.out.println(String.format("| Net Cash Flow      | %-32s               |", formatNetCashFlow(totalIncome - totalExpense)));
            System.out.println(" ---------------------------------------------------------------------\n");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input, please enter a valid year.");
            scan.nextLine();
        }
    }

    private void monthlyBalanceSheet() {
        try {
            UserData user = users.get(currentUser.getUsername());
            allEntries = new ArrayList<>(user.getFinancialData().entrySet());
            allEntries.sort(Comparator.comparing(Map.Entry::getKey));

            System.out.print("Enter the month (MM): ");
            int desiredMonth = scan.nextInt();
            System.out.print("Enter the year (YYYY): ");
            int desiredYear = scan.nextInt();
            System.out.println("---------------------------");

            System.out.println(BALANCE_SHEET_HEADER);

            long totalIncome = 0;
            long totalExpense = 0;

            for (Map.Entry<LocalDate, List<FinancialData>> entry : allEntries) {
                LocalDate date = entry.getKey();
                if (date.getMonthValue() == desiredMonth && date.getYear() == desiredYear) {
                    List<FinancialData> dataList = entry.getValue();
                    if (!dataList.isEmpty()) {
                        System.out.println(String.format("| %-18s | %-21s | %-22s |", date.format(DATE_FORMATTER), dataList.get(0).getTitle(), formatAmount(dataList.get(0).getAmount(), dataList.get(0) instanceof ExpenseData)));
                        for (int i = 1; i < dataList.size(); i++) {
                            System.out.println(String.format("| %-18s | %-21s | %-22s |", "", dataList.get(i).getTitle(), formatAmount(dataList.get(i).getAmount(), dataList.get(i) instanceof ExpenseData)));
                        }
                    }
                    totalIncome += dataList.stream().filter(d -> d instanceof IncomeData).mapToLong(FinancialData::getAmount).sum();
                    totalExpense += dataList.stream().filter(d -> d instanceof ExpenseData).mapToLong(FinancialData::getAmount).sum();
                }
            }
            System.out.println(" ---------------------------------------------------------------------");
            System.out.println(String.format("| Total Income       | %-32s               |", formatAmount(totalIncome, false)));
            System.out.println(String.format("| Total Expense      | %-32s               |", formatAmount(totalExpense, true)));
            System.out.println(" ---------------------------------------------------------------------");
            System.out.println(String.format("| Net Cash Flow      | %-32s               |", formatNetCashFlow(totalIncome - totalExpense)));
            System.out.println(" ---------------------------------------------------------------------\n");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input, please enter valid month and year.");
            scan.nextLine();
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
            System.out.println("Invalid option choice, please use Y/N.");
            logout();
        }
    }

    private void extraRecordExpense() {
        System.out.print("\nDo you want to record another expense? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            expenses();
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu();
        } else {
            System.out.println("Invalid option choice, please use Y/N.");
            extraRecordExpense();
        }
    }

    private void extraRecordIncome() {
        System.out.print("\nDo you want to record another income? (Y/N): ");
        String choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            income();
        } else if (choice.equalsIgnoreCase("N")) {
            mainMenu();
        } else {
            System.out.println("Invalid option choice, please use Y/N.");
            extraRecordIncome();
        }
    }

    private boolean validPass(String username) {
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
            pass.put(username, newPassword);
            return true;
        } else {
            System.out.println("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one symbol.");
            return false;
        }
    }

    private String formatAmount(long amount, boolean isExpense) {
        String prefix = (amount >= 0 && !isExpense) ? "+ " : "- ";

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedAmount = numberFormat.format(Math.abs(amount));

        return prefix + "Rp " + formattedAmount;
    }

    private String formatNetCashFlow(long netCashFlow) {
        String prefix = (netCashFlow >= 0) ? "+ " : "- ";
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedNetCashFlow = numberFormat.format(Math.abs(netCashFlow));
        return prefix + "Rp " + formattedNetCashFlow;

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
