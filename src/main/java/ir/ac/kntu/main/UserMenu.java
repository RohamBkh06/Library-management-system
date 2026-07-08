package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;
import ir.ac.kntu.util.ConsoleStyle;
import ir.ac.kntu.util.Pagination;
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
                            "4. see Reservations     \n" +
                            "5. Setting              \n" +
                            "6. Support              \n" +
                            "7. see SupportTickets   \n" +
                            "8. Return               \n" +
                            ConsoleStyle.RESET
            );

            int select = ScannerWrapper.nextInt(selectionString);
            switch (select) {
                case 1 -> seeLibraryItems(user);
                case 2 -> seeWallet(user);
                case 3 -> seeBorrowedItems(user);
                case 4 -> seeReservations(user);
                case 5 -> setting(user);
                case 6 -> support(user);
                case 7 -> seeSupportTickets(user);
                case 8 -> {
                    return;
                }
                default -> {
                    ScannerWrapper.rewritePrompt(selcetionError);
                    ScannerWrapper.pause();
                }
            }
        }
    }

    private static void seeLibraryItems(NormalUser user) {
        while (true) {
            ConsoleStyle.clearScreen();
            Pagination<LibraryItem> pagination = new Pagination<>(Catalog.getInstance().getItems());
            for (LibraryItem item : pagination.getCurrentPage()) {
                System.out.println(ConsoleStyle.YELLOW +item+ ConsoleStyle.RESET +"\n");
            }
            System.out.println();
            System.out.println(
                    ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                            "Filter by:         \n" +
                            "1. title           \n" +
                            "2. category        \n" +
                            "3. publish year    \n" +
                            "4. borrow an item  \n" +
                            "5. reserve an item \n" +
                            "====== page " + pagination.getCurrentPageNumber() + "/" + pagination.gerTotalPageNumber() + "======\n" +
                            (pagination.hasNextPage() ? "n. Next page       \n" : "") +
                            (pagination.hasPreviousPage() ? "p. Previous page   \n" : "") +
                            "6. return          \n" +
                            ConsoleStyle.RESET
            );

            String select = ScannerWrapper.nextLine(selectionString);

            switch (select.toLowerCase()) {
                case "1" -> filterByTitle();
                case "2" -> filterByCategory();
                case "3" -> filterByPublishYear();
                case "4" -> borrowItem(user);
                case "5" -> reserveItem(user);
                case "n" -> pagination.nextPage();
                case "p" -> pagination.previousPage();
                case "6" -> {
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

    private static void filterByPublishYear() {

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
                            "2. see transactions  \n" +
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
                    System.out.println(ConsoleStyle.GREEN + "Wallet Charged successfully" + ConsoleStyle.RESET);
                    ScannerWrapper.pause();
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
            System.out.println(ConsoleStyle.YELLOW + transaction + ConsoleStyle.RESET + "\n");
        }
        ScannerWrapper.pause();
    }

    private static void seeFines(NormalUser user) {
        if (user.getFines().isEmpty()) {
            System.out.println(ConsoleStyle.CYAN + "You have no Unpaid fines!" + ConsoleStyle.RESET);
            ScannerWrapper.pause();
            return;
        }
        while (true) {
            Pagination<Fine> pagination = new Pagination<>(user.getFines());
            ConsoleStyle.clearScreen();
            System.out.println("--------Page " + pagination.getCurrentPageNumber() + "/" + pagination.gerTotalPageNumber() + "--------");
            for (Fine fine : pagination.getCurrentPage()) {
                System.out.println(ConsoleStyle.YELLOW + fine + ConsoleStyle.RESET);
            }
            System.out.println(
                    "==============================\n" + ConsoleStyle.BG_WHITE + ConsoleStyle.CYAN +
                            "1. pay a fine     \n" +
                            (pagination.hasNextPage() ? "N. Next Page      \n" : "") +
                            (pagination.hasPreviousPage() ? "P. Previous Page  \n" : "") +
                            "E. Exit             " + ConsoleStyle.RESET);
            String select = ScannerWrapper.nextLine(selectionString);
            switch (select.toLowerCase()) {
                case "1" -> payFine(user);
                case "n" -> pagination.nextPage();
                case "p" -> pagination.previousPage();
                case "e" -> {
                    return;
                }
                default -> {
                    ScannerWrapper.rewritePrompt(selcetionError);
                    ScannerWrapper.pause();
                }
            }
        }
    }

    private static void payFine(NormalUser user){
        try {
            String id = ScannerWrapper.nextLine("Enter fine ID: ");
            Fine fine = null;
            for (Fine userFine : user.getFines()) {
                if (userFine.getId().equalsIgnoreCase(id)){
                    fine = userFine;
                }
            }
            if (fine == null){
                throw new IllegalStateException("Fine not found");
            }
            PaymentService.payFine(fine, user);
            System.out.println(ConsoleStyle.GREEN + "Fine paid successfully" + ConsoleStyle.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
        }
        ScannerWrapper.pause();
    }

    private static void seeBorrowedItems(NormalUser user) {
        while (true) {
            ConsoleStyle.clearScreen();
            System.out.println("Capacity: " + (user.getBorrowLimit() - user.activeBorrows()));
            for (Borrowed borrowed : user.getBorrowedList()) {
                System.out.println(ConsoleStyle.YELLOW + borrowed + ConsoleStyle.RESET + "\n");
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

    private static void seeReservations(NormalUser user) {
        if (user.getReservationById().isEmpty()) {
            System.out.println(ConsoleStyle.CYAN + "You have no Reservations so far." + ConsoleStyle.RESET);
            ScannerWrapper.pause();
            return;
        }
        while (true) {
            ConsoleStyle.clearScreen();
            Pagination<Reservation> pagination = new Pagination<>(user.getReservationById().values().stream().toList());
            ConsoleStyle.clearScreen();
            System.out.println("--------Page " + pagination.getCurrentPageNumber() + "/" + pagination.gerTotalPageNumber() + "--------");
            for (Reservation reservation : user.getReservationById().values()) {
                System.out.println(ConsoleStyle.YELLOW + reservation + ConsoleStyle.RESET);
            }
            System.out.println(
                    "==============================\n" + ConsoleStyle.BG_WHITE + ConsoleStyle.CYAN +
                            "1. borrow a reservation \n" +
                            "2. Cancel a reservation \n" +
                            (pagination.hasNextPage() ? "N. Next Page            \n" : "") +
                            (pagination.hasPreviousPage() ? "P. Previous Page        \n" : "") +
                            "E. Exit                   " + ConsoleStyle.RESET);
            String select = ScannerWrapper.nextLine(selectionString);
            switch (select.toLowerCase()) {
                case "1" -> borrowReservation(user);
                case "2" -> cancelReservation(user);
                case "n" -> pagination.nextPage();
                case "p" -> pagination.previousPage();
                case "e" -> {
                    return;
                }
                default -> {
                    ScannerWrapper.rewritePrompt(selcetionError);
                    ScannerWrapper.pause();
                }
            }
        }
    }

    private static void borrowReservation(NormalUser user){
        String id = ScannerWrapper.nextLine("Reservation ID: ");
        Reservation reservation = user.getReservationById().get(id);
        try {
            if (reservation == null) {
                throw new IllegalArgumentException("Item not found");
            }
            user.borrowItem(reservation.getItem());
            System.out.println(ConsoleStyle.GREEN + "Item Reserved successfully" + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        } catch (RuntimeException e) {
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        }
    }

    private static void cancelReservation(NormalUser user){
        String id = ScannerWrapper.nextLine("Reservation ID: ");
        Reservation reservation = user.getReservationById().get(id);
        try {
            if (reservation == null) {
                throw new IllegalArgumentException("Item not found");
            }
            reservation.cancelReservation();
            System.out.println(ConsoleStyle.GREEN + "Reservation canceled successfully" + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        } catch (RuntimeException e) {
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        }
    }



    private static void reserveItem(NormalUser user) {
        String title = ScannerWrapper.nextLine("Item title: ");
        LibraryItem item = Catalog.getInstance().getItem(title);
        try {
            if (item == null) {
                throw new IllegalArgumentException("Item not found");
            }
            user.reserveItem(item);
            System.out.println(ConsoleStyle.GREEN + "Item Reserved successfully" + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        } catch (RuntimeException e) {
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        }
    }

    private static void borrowItem(NormalUser user) {
        ConsoleStyle.clearScreen();
        String title = ScannerWrapper.nextLine("Item title: ");
        LibraryItem item = Catalog.getInstance().getItem(title);

        try {
            if (item == null) {
                throw new IllegalArgumentException("Item not found");
            }
            user.borrowItem(item);
            System.out.println(ConsoleStyle.GREEN + "Item borrowed successfully" + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        } catch (RuntimeException e) {
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        }

    }

    private static void extendBorrow(NormalUser user) {
        ConsoleStyle.clearScreen();
        String id = ScannerWrapper.nextLine("borrow ID: ");
        Borrowed borrowed = user.getBorrowedMap().get(id);

        if (borrowed == null) {
            throw new IllegalArgumentException("Borrow record not found");
        }

        long days = ScannerWrapper.nextLong("Days to extend: ");
        borrowed.extendBorrowTime(days);
        System.out.println(ConsoleStyle.GREEN + "Borrow time extended successfully" + ConsoleStyle.RESET);
        ScannerWrapper.pause();
    }

    private static void returnBorrow(NormalUser user) {
        ConsoleStyle.clearScreen();
        String id = ScannerWrapper.nextLine("borrow ID: ");
        Borrowed borrowed = user.getBorrowedMap().get(id);

        if (borrowed == null) {
            throw new IllegalArgumentException("Borrow record not found");
        }
        user.returnItem(borrowed);
        System.out.println(ConsoleStyle.GREEN + "Item returned successfully" + ConsoleStyle.RESET);
        ScannerWrapper.pause();
    }

    private static void setting(NormalUser user) {
        while (true) {
            ConsoleStyle.clearScreen();
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
            } else {
                System.out.println(ConsoleStyle.GREEN + "Data updated successfully" + ConsoleStyle.RESET);
                ScannerWrapper.pause();
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
                        "Types :             \n" +
                        "1. Report Problem   \n" +
                        "2. Request Item     \n" +
                        "3. Financial affair \n" +
                        "4. Reserve item     \n" +
                        ConsoleStyle.RESET
        );

        int select = ScannerWrapper.nextInt("Select ticket type(Enter any other number to abort): ");
        Department type;
        switch (select) {
            case 1 -> type = Department.REPORT_PROBLEM;
            case 2 -> type = Department.REQUEST_ITEM;
            case 3 -> type = Department.FINANCIAL_AFFAIRS;
            case 4 -> type = Department.RESERVE_ITEM;
            default -> {
                return;
            }
        }
        String message = ScannerWrapper.nextLine("Message: ");

        user.requestSupport(message, type);
        System.out.println(ConsoleStyle.GREEN + "Support request submitted successfully." + ConsoleStyle.RESET);
        ScannerWrapper.pause();
    }

    public static void seeSupportTickets(NormalUser user){
        ConsoleStyle.clearScreen();
        for (SupportTicket ticket : user.getTicketList()) {
            System.out.println(ConsoleStyle.YELLOW + ticket + ConsoleStyle.RESET + "\n");
        }
        ScannerWrapper.pause();
    }
}