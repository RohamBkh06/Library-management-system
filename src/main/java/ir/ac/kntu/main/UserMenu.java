package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;
import ir.ac.kntu.util.ConsoleStyle;
import ir.ac.kntu.util.PaymentService;
import ir.ac.kntu.util.ScannerWrapper;

import java.util.List;

public final class UserMenu {
    private static String selectionString = "Please select one of the options: ";
    private static String selcetionError = "Selection out of bound.";

    private UserMenu() {}

    public static void show(NormalUser user) {
        while (true) {
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.CYAN + "==========Welcome: " + user.getFirstName() + "==========" + ConsoleStyle.RESET);
            System.out.println(
                    ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                            "1. See LibraryItems     \n" +
                            "2. Wallet               \n" +
                            "3. See BorrowedItems    \n" +
                            "4. Setting              \n" +
                            "5. Support              \n" +
                            "6. Return               \n" +
                            ConsoleStyle.RESET
            );

            int select = ScannerWrapper.nextInt(selectionString);
            switch (select) {
                case 1 -> seeLibraryItems();
                case 2 -> seeWallet(user);
                case 3 -> seeBorrowedItems(user);
                case 4 -> setting(user);
                case 5 -> support(user);
                case 6 -> {
                    return;
                }
                default -> {
                    ScannerWrapper.rewritePrompt(selcetionError);
                    ScannerWrapper.pause();
                }
            }
        }
    }

    private static void seeLibraryItems() {
        while (true) {
            ConsoleStyle.clearScreen();
            for (LibraryItem item : Catalog.getInstance().getItems()) {
                System.out.println(item);
            }
            System.out.println();
            System.out.println(
                    ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                            "Filter by:        \n" +
                            "1. title          \n" +
                            "2. category       \n" +
                            "3. publish year   \n" +
                            "                  \n" +
                            "4. return         \n" +
                            ConsoleStyle.RESET
            );

            int select = ScannerWrapper.nextInt(selectionString);

            switch (select) {
                case 1 -> filterByTitle();
                case 2 -> filterByCategory();
                case 3 -> filterByPubishYear();
                case 4 -> {
                    return;
                }
                default -> {
                    ScannerWrapper.rewritePrompt(selcetionError);
                    ScannerWrapper.pause();
                }
            }
        }
    }

    private static void filterByTitle() {
        ConsoleStyle.clearScreen();
        String title = ScannerWrapper.nextLine("Title: ");
        List<LibraryItem> result = Catalog.getInstance().filteredSearch(item -> item.getTitle().toLowerCase().contains(title.toLowerCase()));
        for (LibraryItem item : result) {
            System.out.println(item);
        }
        ScannerWrapper.pause();
    }

    private static void filterByCategory() {
        ConsoleStyle.clearScreen();
        String category = ScannerWrapper.nextLine("Category: ");
        List<LibraryItem> result = Catalog.getInstance().filteredSearch(item -> item.getCategory().equalsIgnoreCase(category));

        for (LibraryItem item : result) {
            System.out.println(item);
        }
        ScannerWrapper.pause();
    }

    private static void filterByPubishYear() {

        ConsoleStyle.clearScreen();
        int from = ScannerWrapper.nextInt("From Year: ");
        int to = ScannerWrapper.nextInt("To Year: ");

        List<LibraryItem> result = Catalog.getInstance().filteredSearch(item -> item.getPublishYear() >= from && item.getPublishYear() <= to);
        for (LibraryItem item : result) {
            System.out.println(item);
        }
        ScannerWrapper.pause();
    }

    private static void seeWallet(NormalUser user) {
        while (true) {
            ConsoleStyle.clearScreen();
            System.out.println(user.getWallet());
            System.out.println(
                    ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                            "Actions:             \n" +
                            "1. Charge wallet     \n" +
                            "2. see transaction   \n" +
                            "3. see fines         \n" +
                            "                     \n" +
                            "4. return            \n" +
                            ConsoleStyle.RESET
            );

            int select = ScannerWrapper.nextInt(selectionString);
            switch (select) {
                case 1 -> {
                    int amount = ScannerWrapper.nextInt("Enter the amount: ");
                    PaymentService.chargeWallet(amount, user);
                }
                case 2 -> seeTransactions(user);
                case 3 -> seeFines(user);
                case 4 -> {
                    return;
                }
                default -> {
                    ScannerWrapper.rewritePrompt(selcetionError);
                    ScannerWrapper.pause();
                }
            }
        }
    }

    private static void seeTransactions(NormalUser user) {
        ConsoleStyle.clearScreen();
        for (Transaction transaction : user.getWallet().getTransactions()) {
            System.out.println(transaction);
        }
        ScannerWrapper.pause();
    }

    private static void seeFines(NormalUser user) {
        ConsoleStyle.clearScreen();
        for (Fine fine : user.getFines()) {
            System.out.println(fine);
        }
        ScannerWrapper.pause();
    }

    private static void seeBorrowedItems(NormalUser user) {
        while (true) {
            ConsoleStyle.clearScreen();
            System.out.println("Capacity: " + (user.getBorrowLimit() - user.activeBorrows()));
            for (Borrowed borrowed : user.getBorrowedList()) {
                System.out.println(borrowed);
            }

            System.out.println(
                    ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                            "Actions:             \n" +
                            "1. borrow new item   \n" +
                            "2. extend a borrow   \n" +
                            "3. return a borrow   \n" +
                            "                     \n" +
                            "4. return            \n" +
                            ConsoleStyle.RESET
            );

            try {
                int select = ScannerWrapper.nextInt(selectionString);
                switch (select) {
                    case 1 -> borrowItem(user);
                    case 2 -> extendBorrow(user);
                    case 3 -> returnBorrow(user);
                    case 4 -> {
                        return;
                    }
                    default -> {
                        ScannerWrapper.rewritePrompt(selcetionError);
                        ScannerWrapper.pause();
                    }
                }
            } catch (Exception e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    private static void borrowItem(NormalUser user) {
        ConsoleStyle.clearScreen();
        String title = ScannerWrapper.nextLine("Item title: ");
        LibraryItem item = Catalog.getInstance().getItem(title);

        if (item == null) {
            throw new IllegalArgumentException("Item not found");
        }
        user.borrowItem(item);
    }

    private static void extendBorrow(NormalUser user) {

        String id = ScannerWrapper.nextLine("borrow ID: ");
        Borrowed borrowed = user.getBorrowedMap().get(id);

        if (borrowed == null) {
            throw new IllegalArgumentException("Borrow record not found");
        }

        long days = ScannerWrapper.nextLong("Days to extend: ");
        borrowed.extendBorrowTime(days);
    }

    private static void returnBorrow(NormalUser user) {
        String id = ScannerWrapper.nextLine("borrow ID: ");
        Borrowed borrowed = user.getBorrowedMap().get(id);

        if (borrowed == null) {
            throw new IllegalArgumentException("Borrow record not found");
        }
        user.returnItem(borrowed);
    }

    private static void setting(NormalUser user) {
        while (true) {
            System.out.println(
                    ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                            "options:           \n" +
                            "1. First Name      \n" +
                            "2. Last Name       \n" +
                            "3. Email           \n" +
                            "4. Phone Number    \n" +
                            "5. Password        \n" +
                            "6. Exit            \n" +
                            ConsoleStyle.RESET
            );

            int select = ScannerWrapper.nextInt("Choose an option to change: ");
            if (handleSettingSelection(select, user)) {
                return;
            }
        }
    }

    private static boolean handleSettingSelection(int select, NormalUser user) {
        try {
            switch (select) {
                case 1 -> UserSettingsMenu.updateFirstName(user);
                case 2 -> UserSettingsMenu.updateLastName(user);
                case 3 -> UserSettingsMenu.updateEmail(user);
                case 4 -> UserSettingsMenu.updatePhoneNumber(user);
                case 5 -> UserSettingsMenu.updatePassword(user);
                case 6 -> {
                    return true;
                }
                default -> throw new IllegalArgumentException("Invalid option");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        }

        return false;
    }

    private static void support(NormalUser user) {

        System.out.println(
                ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                        "Types :            \n" +
                        "1. Report Problem  \n" +
                        "2. Request Item    \n" +
                        ConsoleStyle.RESET
        );

        int select = ScannerWrapper.nextInt("Select ticket type(Enter anything else to abort): ");
        String message = ScannerWrapper.nextLine("Message: ");
        TicketType type;
        switch (select) {
            case 1 -> type = TicketType.REPORT_PROBLEM;
            case 2 -> type = TicketType.REQUEST_ITEM;
            default -> {
                return;
            }
        }

        user.requestSupport(message, type);

        System.out.println(ConsoleStyle.GREEN + "Support request submitted successfully." + ConsoleStyle.RESET);
    }
}