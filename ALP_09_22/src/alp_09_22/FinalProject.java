package alp_09_22;

import java.util.Scanner;

public class FinalProject {

    FinalProject func;
    Scanner scan = new Scanner(System.in);
    boolean validity = false;
    String[] user = new String[10];
    String[] pass = new String[10];
    int count = 0;

    public static void main(String[] args) {

        FinalProject func = new FinalProject();
        func.first();
    }

    public void first() {

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Login \n2. Create Account \n3. Continue as a Guest");
            System.out.print("Reply: ");
            int option = scan.nextInt();

            switch (option) {
                case 1 -> {
                    System.out.print("Username: ");
                    String username = scan.next() + scan.nextLine();
                    System.out.print("Password: ");
                    String password = scan.next() + scan.nextLine();
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
                    }
                }

                case 2 -> {
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
                        first();
//                        budgeting();
//                        return;
                    }
                }

                case 3 -> {
                    budgeting();
                    return;
                }

                default ->
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }

    public void budgeting() {
        System.out.println(" ");
        System.out.println("---------------------------");
        System.out.println("Welcome to Finner!");
        System.out.println("---------------------------");
        System.out.print("What's your budget per month?: ");       
        int budget = scan.nextInt();

        int needs = budget * 5 / 10, wants = budget * 3 / 10, save = budget * 2 / 10;
        String choice;

        System.out.println("Here is your financial recommendation:");
        System.out.println("Needs: " + needs + "\nWants: " + wants + "\nSave: " + save);
        System.out.println("Do you want to modify? (Y/N): ");
        choice = scan.next();

        if (choice.equalsIgnoreCase("Y")) {
            System.out.println("How do you want to plan your month?");
            System.out.print("Needs: ");
            needs = scan.nextInt();
            if ((budget -= needs) < 0) {
                System.out.println("Invalid");                
            }
            System.out.println("Remaining Budget: Rp " + (budget -= needs));
            System.out.print("Wants: ");
            wants = scan.nextInt();
            System.out.print("Remaining Budget: Rp " + (budget -= wants));
            System.out.print("Save: ");
            save = scan.nextInt();
        }
        if (choice.equalsIgnoreCase("N")) {
            second();
        }
    }

    public void second() {
        System.out.println("Menu:");
        System.out.println("1. Spendings");
        System.out.println("2. Income");
    }
    
    public void spendings(){
        System.out.println("Category:");
        System.out.println("1. Wants");
        System.out.println("2. Needs");
    }
}
