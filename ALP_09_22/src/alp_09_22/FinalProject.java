package alp_09_22;

import java.util.Scanner;

public class FinalProject {

    FinalProject func;
    Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        FinalProject func = new FinalProject();
        func.first();
        func.budgeting();
    }

    public void first() {
        FinalProject func = new FinalProject();
        String[] user = new String[10];
        String[] pass = new String[10];

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Login \n2. Create Account \n3. Continue as a Guest");
            System.out.print("Reply: ");
            int option = scan.nextInt();

            switch (option) {
                case 1 -> {
                    for (int i = 0; i < 10; i++) {
                        System.out.print("Username: ");
                        String username = scan.next();
                        System.out.print("Password: ");
                        String password = scan.next();
                        boolean validity = false;
                        if (username.equals(user[i]) && password.equals(pass[i])) {
                            System.out.println("You have successfully logged in");
                            validity = true;
                            func.budgeting();
                            break;
                        } else if (!validity) {
                            System.out.println("Invalid username or Password. Please Try Again.");
                            i--;
                        }
                    }
                }

                case 2 -> {
                    for (int i = 0; i < 10; i++) {
                        System.out.print("Username: ");
                        user[i] = scan.next();

                        boolean checkUpper = false;
                        boolean checkLower = false;
                        boolean checkDigit = false;
                        boolean checkSymbol = false;

                        do {
                            System.out.print("Password (at least 8 characters): ");
                            pass[i] = scan.next();

                            if (pass[i].length() < 8) {
                                System.out.println("Password must be at least 8 characters long. Try again.");
                                continue;
                            }

                            for (int j = 0; j < pass[i].length(); j++) {
                                char pw = pass[i].charAt(j);

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
                        System.out.println("Welcome ");
                        func.budgeting();
                    }
                }

                case 3 -> {
                    System.out.println("Exiting program");
                    scan.close();
                    System.exit(0);
                }

                default ->
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }

    public void budgeting() {
        System.out.print("What's your budget per month?: ");
        int budget = scan.nextInt();
        
        int needs = budget*5/10, wants = budget*3/10, save = budget*2/10;
        String choice;
        
        System.out.println("Here is your financial recommendation:");
        System.out.println("Needs: " +needs +"\nWants: " +wants +"\nSave: " +save);
        System.out.println("Do you want to modify? (Y/N): ");
        choice = scan.next();
        
        if (choice.equalsIgnoreCase("Y")) {
            System.out.println("How do you want to plan your month?");
            System.out.println("Needs: ");
            needs = scan.nextInt();
            System.out.println("Remaining Budget: Rp " +(budget -= needs));
            System.out.println("Wants: ");
            wants = scan.nextInt();
            System.out.println("Remaining Budget: Rp " +(budget -= wants));
            System.out.println("Save: ");
            save = scan.nextInt(); 
            System.out.println("Remaining Budget: Rp " +(budget -= save));
        } 
        if (choice.equalsIgnoreCase("N")){
            func.second();
        }   
    }

    public void second() {
        System.out.println("Menu:");
        System.out.println("1. Spendings");
    }
}
