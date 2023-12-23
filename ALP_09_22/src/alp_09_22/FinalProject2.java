package alp_09_22;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

public class FinalProject2 {

    int count = 0, count2 = 0;
    Scanner scan = new Scanner(System.in);
    boolean validity = false;
    String[] user = new String[10];
    String[] pass = new String[10];
    long[] foodBudget = new long[1000];
    long[] transportationBudget = new long[1000];
    long[] utilitiesBudget = new long[1000];
    long[] educationBudget = new long[1000];
    int budget = 0;

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        FinalProject2 finalProject = new FinalProject2();
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
                    run2();
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

        for (int i = 0; i < 10; i++) {

            if (username.equals(user[i]) && password.equals(pass[i])) {
                System.out.println("You have successfully logged in");
                validity = true;
            }
        }
        if (validity == true) {
            mainMenu();
        } else {
            System.out.println("Invalid username or Password. Please Try Again.");
            login();
        }
    }

    private void createAccount() {
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
                    System.out.println(" ");
                }
            } while (!(checkUpper && checkLower && checkDigit && checkSymbol));
            System.out.println("Account created successfully");
            count++;
            writeArraysToFile();
            run2();
        }
    }

    public void run2() {
        while (true) {
            mainMenu();
            int choice = scan.nextInt();
            switch (choice) {
                case 1:
                    pengeluaran();
                    break;
                case 2:
                    pemasukan();
                    break;
                case 3:
                    catatanKeuangan();
                    break;
                case 4:
                    break;
                default:
                    break;
            }
        }
    }

    public void mainMenu() {

        System.out.println("=====================");
        System.out.println(" Welcome to Finner! ");
        System.out.println("=====================");
        // rencananya buat output bisa untuk liat kategori masing-masing (buku besar) & bisa lihat keseluruhan per harinya + total (di nomor 3)
        System.out.println("Menu: " + "\n1. Catat Pengeluaran " + " \n2. Catat Pemasukan" + " \n3. Lihat Catatan Keuangan" + "\n4. Logout");
        System.out.print("Choice: ");
    }

    public void pengeluaran() {
        System.out.println("=====================");
        System.out.println("Silahkan pilih salah satu menu dibawah ini: " + "\n1. Needs" + "\n2. Wants" + "\n3. Save");
        System.out.print("Reply: ");
        int category = scan.nextInt();
        while (true) {
            switch (category) {
                case 1:
                    needsPengeluaran();
                    break;
                case 2:
                    wantsPengeluaran();
                    break;
                case 3:
                    savePemasukan();
                    break;
                default:
                    break;
            }
        }
    }

    private void needsPengeluaran() {
        System.out.println("=====================");
        System.out.println("Menu:");
        System.out.println("1. Food" + "\n2. Transportation" + "\n3. Utilities (gas/water/electricity/internet)" + "\n4. Education" + "\n5. Back to Main Menu");
        System.out.print("Reply: ");
        int choice = scan.nextInt();
        switch (choice) {
            case 1:
                food();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                mainMenu();
                break;
            default:
                break;
        }
    }

    private void food() {
        System.out.println("=====================");
        System.out.print("Jumlah pengeluaran: ");
        foodBudget[count2] = scan.nextLong();
        count2++;
        System.out.println("Pengeluaran berhasil dicatat!");
        needsPengeluaran();
    }

    private void transportation() {
        System.out.println("=====================");
        System.out.print("Jumlah pengeluaran: ");
        foodBudget[count2] = scan.nextLong();
        count2++;
        System.out.println("Pengeluaran berhasil dicatat!");
        needsPengeluaran();
    }

    private void utilities() {
        System.out.println("=====================");
        System.out.print("Jumlah pengeluaran: ");
        foodBudget[count2] = scan.nextLong();
        count2++;
        System.out.println("Pengeluaran berhasil dicatat!");
        needsPengeluaran();
    }

    private void education() {
        System.out.println("=====================");
        System.out.print("Jumlah pengeluaran: ");
        foodBudget[count2] = scan.nextLong();
        count2++;
        System.out.println("Pengeluaran berhasil dicatat!");
        needsPengeluaran();
    }

    public void wantsPengeluaran() {
        System.out.println("Menu: ");
        System.out.println("1. Entertainment" + "\n2. Travel" + "\n3. Fashion" + "\n4. Technology" + "\n5. Back to Main Menu");
    }

    private void savePemasukan() {
        System.out.println("");
    }

    public void pemasukan() {

    }

    private void catatanKeuangan() {

    }

    private void writeArraysToFile() {
        String filePath = "aaa.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write each element of the user array to the file
            for (String value : user) {
                writer.write(String.valueOf(value));
                writer.newLine();  // Add a newline for each element
            }

            // Write each element of the pass array to the file
            for (String value : pass) {
                writer.write(String.valueOf(value));
                writer.newLine();  // Add a newline for each element
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
