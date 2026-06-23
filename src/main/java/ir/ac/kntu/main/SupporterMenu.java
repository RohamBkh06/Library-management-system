package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;
import ir.ac.kntu.util.ScannerWrapper;
import ir.ac.kntu.util.ConsoleStyle;

import java.util.List;
import java.util.Map;

public final class SupporterMenu {

    private SupporterMenu() {
    }

    public static void show(Supporter supporter) {
        while (true) {
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.BOLD + ConsoleStyle.CYAN +
                    "================ SUPPORTER MENU ================\n" +
                    "1. View all users       \n" +
                    "2. View recent borrows  \n" +
                    "3. View all fines       \n" +
                    "4. Search user by ID    \n" +
                    "5. Exit                 \n" +
                    "===============================================" +
                    ConsoleStyle.RESET);
            try {
                int choice = ScannerWrapper.nextInt("Select option: ");
                switch (choice) {
                    case 1 -> viewAllUsers();
                    case 2 -> viewRecentBorrows();
                    case 3 -> viewAllFines();
                    case 4 -> searchUserById();
                    case 5 -> {
                        return;
                    }
                    default -> throw new IllegalArgumentException("Invalid option");
                }
                ScannerWrapper.pause();
            } catch (RuntimeException e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    private static void viewAllUsers() {
        Map<String, NormalUser> users = LibraryManger.getInstance().getUserById();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        for (NormalUser user : users.values()) {
            System.out.println("ID: " + user.getId() + " | Active Borrows: " + user.activeBorrows() + " | Fines: " + user.getFines().size());
        }
    }

    private static void viewRecentBorrows() {

        List<Borrowed> borrows = LibraryManger.getInstance().getRecentBorrows();

        if (borrows.isEmpty()) {
            System.out.println("No borrows found.");
            return;
        }

        for (Borrowed b : borrows) {
            System.out.println(b);
        }
    }

    private static void viewAllFines() {

        List<Fine> fines =
                LibraryManger.getInstance().getAllFines();

        if (fines.isEmpty()) {
            System.out.println("No fines found.");
            return;
        }

        for (Fine f : fines) {
            System.out.println(f);
        }
    }

    private static void searchUserById() {

        String id =
                ScannerWrapper.nextLine("Enter user ID: ");

        NormalUser user =
                LibraryManger.getInstance()
                        .getUserById()
                        .get(id);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        System.out.println(
                "ID: " + user.getId()
        );
    }
}
