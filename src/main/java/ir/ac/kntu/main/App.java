package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;
import ir.ac.kntu.util.ConsoleStyle;
import ir.ac.kntu.util.EmailService;
import ir.ac.kntu.util.PaymentService;
import ir.ac.kntu.util.ScannerWrapper;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class App {
    private static LibraryManger libraryManger = LibraryManger.getInstance();
    private static Catalog catalog = Catalog.getInstance();
    public static void main(String[] args) {
        while(true){
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.BOLD + ConsoleStyle.CYAN +
                    "===============================================================\n" +
                    "                   Library Management System                   \n" +
                    "===============================================================" + ConsoleStyle.RESET);
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "1. Sign up             \n" +
                    "2. Log in as supporter \n" +
                    "3. Exit                \n" + ConsoleStyle.RESET);

            int select = ScannerWrapper.nextInt("Please select one of the options: ");
            switch (select) {
                case 1:
                    userSignUp();
                    break;
                case 2:
                    supporterLogin();
                    break;
                case 3:
                    ScannerWrapper.close();
                    return;
                default:
                    ScannerWrapper.rewritePrompt("Selection out of bound.");
                    ScannerWrapper.pause();
                    break;
            }
        }
    }

    public static void userSignUp(){
        while(true){
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.BOLD + ConsoleStyle.CYAN +
                "===================Sign up===================\n" + ConsoleStyle.RESET);

            try {
                String firstName =
                        ScannerWrapper.nextLine("First Name: ");

                String lastName =
                        ScannerWrapper.nextLine("Last Name: ");

                String id =
                        ScannerWrapper.nextLine("Member ID: ");

                String email =
                        ScannerWrapper.nextLine("Email: ");

                String phoneNumber =
                        ScannerWrapper.nextLine("Phone Number: ");

                String password =
                        ScannerWrapper.nextLine("Password: ");

                if (id.contains("GST")){
                    userMenu(twoFA(new Guest(firstName, lastName, id, email, phoneNumber, password)));
                } else if (id.contains("STU")) {
                    userMenu(twoFA(new Student(firstName, lastName, id, email, phoneNumber, password)));
                } else if (id.contains("FAC")) {
                    userMenu(twoFA(new Faculty(firstName, lastName, id, email, phoneNumber, password)));
                } else{
                    throw new IllegalStateException("Invalid input");
                }
            } catch (RuntimeException e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
                continue;
            }
            break;
        }
    }

    public static NormalUser twoFA(NormalUser user){
        if (libraryManger.getUserById().containsKey(user.getId())){
            throw new IllegalStateException("User already exists.");
        }
        int code = 123456;
        //new Random().nextInt(100000,1000000);

        EmailService.sendVerificationCode(user.getEmail(), code);

        System.out.println("Verification code sent.");
        int enteredCode = ScannerWrapper.nextInt("Verification Code: ");

        if (enteredCode != code) {
            throw new IllegalStateException("Wrong verification code.");
        }
        libraryManger.addUser(user);
        return user;
    }

    public static void userMenu(NormalUser user){
        while (true){
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.CYAN + "==========Welcome: "+user.getFirstName()+"==========" + ConsoleStyle.RESET);
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "1. See LibraryItems     \n" +
                    "2. Wallet               \n" +
                    "3. See BorrowedItems    \n" +
                    "4. Setting              \n" +
                    "5. Support              \n" +
                    "6. return               \n" +
                    ConsoleStyle.RESET);
            int select = ScannerWrapper.nextInt("Please select one of the options: ");
            switch (select) {
                case 1:
                    seeLibraryItems();
                    break;
                case 2:
                    seeWallet(user);
                    break;
                case 3:
                    seeBorrowedItems(user);
                    break;
                case 4:
                    setting(user);
                    break;
                case 5:
                    support(user);
                    break;
                case 6:
                    return;
                default:
                    ScannerWrapper.rewritePrompt("Selection out of bound.");
                    ScannerWrapper.pause();
                    break;
            }
        }
    }

    public static void seeLibraryItems(){
        while (true){
            ConsoleStyle.clearScreen();
            for (LibraryItem item : catalog.getItems()) {
                System.out.println(item.toString());
            }
            System.out.println();

            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "Filter by:        \n" +
                    "1. title          \n" +
                    "2. category       \n" +
                    "3. publish year   \n" +
                    "                  \n" +
                    "4. return         \n" +
                    ConsoleStyle.RESET);
            int select = ScannerWrapper.nextInt("Please select one of the options: ");
            switch (select) {
                case 1:
                    filterByTitle();
                    break;
                case 2:
                    filterByCategory();
                    break;
                case 3:
                    filterByPubishYear();
                    break;
                case 4:
                    return;
                default:
                    ScannerWrapper.rewritePrompt("Selection out of bound.");
                    ScannerWrapper.pause();
                    break;
            }
        }
    }

    public static void filterByTitle(){
        ConsoleStyle.clearScreen();
        String title = ScannerWrapper.nextLine("Title: ");

        List<LibraryItem> result = catalog.filteredSearch(item -> item.getTitle().toLowerCase().contains(title.toLowerCase()));
        for (LibraryItem item : result) {
            System.out.println(item.toString());
        }
        ScannerWrapper.pause();
    }

    public static void filterByCategory(){
        ConsoleStyle.clearScreen();
        String category = ScannerWrapper.nextLine("Category: ");

        List<LibraryItem> result = catalog.filteredSearch(item -> item.getCategory().equalsIgnoreCase(category));
        for (LibraryItem item : result) {
            System.out.println(item.toString());
        }
        ScannerWrapper.pause();
    }

    public static void filterByPubishYear(){
        ConsoleStyle.clearScreen();
        int from = ScannerWrapper.nextInt("From Year: ");
        int to = ScannerWrapper.nextInt("To Year: ");

        List<LibraryItem> result = catalog.filteredSearch(item -> item.getPublishYear() >= from && item.getPublishYear() <= to);
        for (LibraryItem item : result) {
            System.out.println(item.toString());
        }
        ScannerWrapper.pause();
    }

    public static void seeWallet(NormalUser user){
        while (true){
            ConsoleStyle.clearScreen();
            System.out.println(user.getWallet().toString());
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "Actions:             \n" +
                    "1. Charge wallet     \n" +
                    "2. see transaction   \n" +
                    "3. see fines         \n" +
                    "                     \n" +
                    "4. return            \n" +
                    ConsoleStyle.RESET);
            int select = ScannerWrapper.nextInt("Please select one of the options: ");
            switch (select) {
                case 1:
                    int amount = ScannerWrapper.nextInt("Enter the amount: ");
                    PaymentService.chargeWallet(amount, user);
                    break;
                case 2:
                    seeTransactions(user);
                    break;
                case 3:
                    seeFines(user);
                    break;
                case 4:
                    return;
                default:
                    ScannerWrapper.rewritePrompt("Selection out of bound.");
                    ScannerWrapper.pause();
                    break;
            }
        }
    }

    public static void seeTransactions(NormalUser user){
        ConsoleStyle.clearScreen();
        for (Transaction transaction : user.getWallet().getTransactions()) {
            System.out.println(transaction.toString());
        }
        ScannerWrapper.pause();
    }

    public static void seeFines(NormalUser user){
        ConsoleStyle.clearScreen();
        for (Fine fine : user.getFines()) {
            System.out.println(fine.toString());
        }
        ScannerWrapper.pause();
    }

    public static void seeBorrowedItems(NormalUser user){
        while(true){
            ConsoleStyle.clearScreen();
            System.out.println("Capacity: " + (user.getBorrowLimit() - user.activeBorrows()));
            for (Borrowed borrowed : user.getBorrowedList()) {
                System.out.println(borrowed.toString());
            }
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "Actions:             \n" +
                    "1. borrow new item   \n" +
                    "2. extend a borrow   \n" +
                    "3. return a borrow   \n" +
                    "                     \n" +
                    "4. return            \n" +
                    ConsoleStyle.RESET);
            try {
                int select = ScannerWrapper.nextInt("Please select one of the options: ");
                switch (select) {
                    case 1:
                        borrowItem(user);
                        break;
                    case 2:
                        extendBorrow(user);
                        break;
                    case 3:
                        returnBorrow(user);
                        break;
                    case 4:
                        return;
                    default:
                        ScannerWrapper.rewritePrompt("Selection out of bound.");
                        ScannerWrapper.pause();
                        break;
                }
            } catch (Exception e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    public static void borrowItem(NormalUser user){
        ConsoleStyle.clearScreen();
        String title = ScannerWrapper.nextLine("Item title: ");
        LibraryItem item = catalog.getItem(title);
        if (item == null) {
            throw new IllegalArgumentException("Item not found");
        }

        user.borrowItem(item);
    }

    public static void extendBorrow(NormalUser user) {
        String id = ScannerWrapper.nextLine("borrow ID: ");
        Borrowed borrowed = user.getBorrowedMap().get(id);

        if (borrowed == null) {
            throw new IllegalArgumentException(
                    "Borrow record not found"
            );
        }
        long days = ScannerWrapper.nextLong("Days to extend: ");
        borrowed.extendBorrowTime(days);
    }

    public static void returnBorrow(NormalUser user) {
        String id = ScannerWrapper.nextLine("borrow ID: ");
        Borrowed borrowed = user.getBorrowedMap().get(id);

        if (borrowed == null) {
            throw new IllegalArgumentException(
                    "Borrow record not found"
            );
        }
        user.returnItem(borrowed);
    }

    public static void setting(NormalUser user) {
        while (true) {
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "options:           \n" + "1. First Name      \n" + "2. Last Name       \n" + "3. Email           \n" +
                    "4. Phone Number    \n" +
                    "5. Password        \n" +
                    "6. Exit            \n" +
                    ConsoleStyle.RESET);
            int select = ScannerWrapper.nextInt("Choose an option to change: ");
            try {
                switch (select) {
                    case 1 -> {
                        String firstName = ScannerWrapper.nextLine("New First Name: ");
                        user.setFirstName(firstName);
                        System.out.println("First name updated.");
                    }
                    case 2 -> {
                        String lastName = ScannerWrapper.nextLine("New Last Name: ");
                        user.setLastName(lastName);
                        System.out.println("Last name updated.");
                    }
                    case 3 -> {
                        String email = ScannerWrapper.nextLine("New Email: ");
                        user.setEmail(email);
                        System.out.println("Email updated.");
                    }
                    case 4 -> {
                        String phone = ScannerWrapper.nextLine("New Phone Number: ");
                        user.setPhoneNum(phone);
                        System.out.println("Phone number updated.");
                    }
                    case 5 -> {
                        String password = ScannerWrapper.nextLine("New Password: ");
                        user.setPassword(password);
                        System.out.println("Password updated.");
                    }
                    case 6 -> {
                        return;
                    }
                    default -> throw new IllegalArgumentException("Invalid option");
                }

            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    public static void support(NormalUser user) {
        System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                "Types :            \n" +
                "1. Report Problem  \n" +
                "2. Request Item    \n" +
                ConsoleStyle.RESET);

        int select = ScannerWrapper.nextInt("Select ticket type: ");
        String message = ScannerWrapper.nextLine("Message: ");
        TicketType type;
        switch (select) {
            case 1 -> type = TicketType.REPORT_PROBLEM;
            case 2 -> type = TicketType.REQUEST_ITEM;
            default -> type = TicketType.REPORT_PROBLEM;
        }

        user.requestSupport(message, type);
        System.out.println(ConsoleStyle.GREEN + "Support request submitted successfully." + ConsoleStyle.RESET);
    }

    public static void supporterLogin() {
        while (true) {
            try {
                String password = ScannerWrapper.nextLine("Password: ");
                Supporter supporter = LibraryManger.getInstance().loginSupporter(password);
                System.out.println(ConsoleStyle.GREEN + "Login successful" + ConsoleStyle.RESET);

                SupporterMenu.show(supporter);

            } catch (RuntimeException e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }
}
