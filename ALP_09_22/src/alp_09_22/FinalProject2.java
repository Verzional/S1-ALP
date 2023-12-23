package alp_09_22;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

public class FinalProject2 {

    int count2 = 0;
    private Scanner scan;
    private String[] user;
    private String[] pass;
    private int count;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1; // Note: Month is 0-based
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    long[] foodBudget = new long[1000];
    long[] transportationBudget = new long[1000];
    long[] utilitiesBudget = new long[1000];
    long[] educationBudget = new long[1000];
    int budget = 0;
    boolean exit = false;

    public FinalProject2() {
        scan = new Scanner(System.in);
        user = new String[10];
        pass = new String[10];
        count = 0;
    }

    public static void main(String[] args) {

        FinalProject2 finalProject = new FinalProject2();
        finalProject.firstPage();
    }

    public void firstPage() {

        while (!exit) {
            System.out.println("Menu:");
            System.out.println("1. Login" + "\n2. Create Account" + "\n3. Continue as a Guest");
            System.out.print("Choice: ");
            int choice = scan.nextInt();
            switch (choice) {
                case 1:
                    login();
                    break;

                case 2:
                    register();
                    break;

                case 3:
                    run2();
                    exit = true;
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void login() {
        System.out.println("");
        System.out.print("Username: ");
        String username = scan.next();
        System.out.print("Password: ");
        String password = scan.next();

        for (int i = 0; i < count; i++) {
            if (username.equals(user[i]) && password.equals(pass[i])) {
                System.out.println("You have successfully logged in");
                run2();
                exit = true;
                return;
            }
        }
        System.out.println("Invalid username or password. Please try again.");
    }

    private void register() {
        System.out.println("=====================");
        System.out.print("Username: ");
        String newUsername = scan.next();

        while (!validPass()) {
            System.out.println("");
        }

        user[count] = newUsername;
        System.out.println("Account created successfully!");
        System.out.println("=====================");
        count++;
        writeArraysToFile();
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
        System.out.println(year + "/" + month + "/" + day);
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
            for (int i = 0; i < count; i++) {
                writer.write(String.valueOf(user[i]));
                writer.newLine();  // Add a newline for each element
            }

            // Write only the valid elements of the pass array to the file
            for (int i = 0; i < count; i++) {
                writer.write(String.valueOf(pass[i]));
                writer.newLine();  // Add a newline for each element
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
