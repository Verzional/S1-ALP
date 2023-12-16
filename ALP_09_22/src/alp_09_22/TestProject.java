package alp_09_22;

import java.util.Scanner;

public class TestProject {

    private static final int MAX_USERS = 10;
    private String[] user = new String[MAX_USERS];
    private String[] pass = new String[MAX_USERS];
    private int count = 0;
    private Scanner scan = new Scanner(System.in);
    boolean validity = false;
    String choice;

    public static void main(String[] args) {
        TestProject finalProject = new TestProject();
        finalProject.run();

    }

    public void run() {
        while (true) {
            displayMenu();
            int option = scan.nextInt();
            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    budgeting();
                    return;
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Login \n2. Create Account \n3. Continue as a Guest");
        System.out.print("Reply: ");
    }

    private void login() {
        System.out.print("Username: ");
        String username = scan.next() + scan.nextLine();
        System.out.print("Password: ");
        String password = scan.next() + scan.nextLine();

        // Implement login logic here
        for (int i = 0; i < 10; i++) {

            if (username.equals(user[i]) && password.equals(pass[i])) {
                System.out.println("You have successfully logged in");
                validity = true;
            }
        }
        if (validity == true) {
            budgeting();
        } else {
            System.out.println("Invalid username or Password. Please Try Again.");
            login();
        }
    }

    private void createAccount() {
        // Implement account creation logic here
        for (int i = 0; i < 10; i++) {
            System.out.print("Username: ");
            user[count] = scan.next();

            boolean checkUpper = false;
            boolean checkLower = false;
            boolean checkDigit = false;
            boolean checkSymbol = false;

            do {
                System.out.print("Password (at least 8 characters): ");
                pass[count] = scan.next();

                if (pass[count].length() < 8) {
                    System.out.println("Password must be at least 8 characters long. Try again.");
                    continue;
                }

                for (int j = 0; j < pass[count].length(); j++) {
                    char pw = pass[count].charAt(j);

                    if (Character.isUpperCase(pw)) {
                        checkUpper = true;
                    } else if (Character.isLowerCase(pw)) {
                        checkLower = true;
                    } else if (Character.isDigit(pw)) {
                        checkDigit = true;
                    } else {
                        checkSymbol = true;
                    }
                }

                if (!(checkUpper && checkLower && checkDigit && checkSymbol)) {
                    System.out.println("Password does not meet the requirements. Try again.");
                }
            } while (!(checkUpper && checkLower && checkDigit && checkSymbol));
            System.out.println("Account created successfully");
            count++;
            run();
//                        budgeting();
//                        return;
        }
    }

    private void second() {
        System.out.println("Menu:");
        System.out.println("1. Spendings");
        // Add more options as needed
    }

    public void budgeting() {
        int budget = 0;
        int needs, wants, save;
        System.out.println(" ");
        System.out.println("---------------------------");
        System.out.println("Welcome to Finner!");
        System.out.println("---------------------------");
        System.out.print("What's your budget per month?: ");
        budget = scan.nextInt();
        needs = budget * 5 / 10;
        wants = budget * 3 / 10;
        save = budget * 2 / 10;
        
        System.out.println("Here is your financial recommendation:");
        System.out.println("Needs: " + needs + "\nWants: " + wants + "\nSave: " + save);

        System.out.println("Do you want to modify? (Y/N): ");
        choice = scan.next();
        if (choice.equalsIgnoreCase("Y")) {
            System.out.println("How do you want to plan your month?");
            System.out.print("Needs: ");
            needs = scan.nextInt();
            while ((budget -= needs) < 0) {
                System.out.println("Invalid number!");
                System.out.print("Needs: ");
                needs = scan.nextInt();
            }
            System.out.print("Wants: ");
            wants = scan.nextInt();
            while ((budget -= wants) < 0) {
                System.out.println("Invalid number!");
                System.out.print("Wants: ");
                wants = scan.nextInt();
            }
            System.out.println("Remaining Budget: Rp " + (budget -= wants));
            System.out.print("Save: ");
            save = scan.nextInt();
        }
        if (choice.equalsIgnoreCase("N")) {
            second();
        }
        // Add more methods for budgeting logic or other features
    }
}
