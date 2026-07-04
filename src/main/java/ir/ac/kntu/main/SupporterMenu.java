package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;
import ir.ac.kntu.util.Pagination;
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
                    "Welcome " + supporter.getName() + "\n" +
                    "1. View all users       \n" +
                    "2. View recent borrows  \n" +
                    "3. View all fines       \n" +
                    "4. Search user by ID    \n" +
                    "5. add new item         \n" +
                    "6. see support requests \n" +
                    "7. Exit                 \n" +
                    "===============================================" +
                    ConsoleStyle.RESET);
            try {
                int choice = ScannerWrapper.nextInt("Select option: ");
                switch (choice) {
                    case 1 -> viewAllUsers();
                    case 2 -> viewRecentBorrows();
                    case 3 -> viewAllFines();
                    case 4 -> searchUserById();
                    case 5 -> addItem();
                    case 6 -> seeAllTickets();
                    case 7 -> {
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
        while (true) {
            ConsoleStyle.clearScreen();
            Map<String, NormalUser> users = LibraryManger.getInstance().getUserById();
            if (users.isEmpty()) {
                System.out.println("No users found.");
                return;
            }
            Pagination<NormalUser> pagination = new Pagination<>(users.values().stream().toList());
            for (NormalUser user : pagination.getCurrentPage()) {
                System.out.println(ConsoleStyle.YELLOW + user + "\n" + " | Active Borrows: " + user.activeBorrows() + " | Number of Fines: " + user.getFines().size() + ConsoleStyle.RESET);
            }
            System.out.println();
            System.out.println(ConsoleStyle.BOLD + ConsoleStyle.CYAN +
                    "=========page " + pagination.getCurrentPageNumber() + "/" + pagination.gerTotalPageNumber() + "=========\n" +
                    (pagination.hasNextPage() ? "n. Next page      \n" : "") +
                    (pagination.hasPreviousPage() ? "p. Previous page  \n" : "") +
                    "==========================\n" +
                    "1. Exit           \n" +
                    ConsoleStyle.RESET);
            try {
                String choice = ScannerWrapper.nextLine("Select option: ");
                switch (choice.toLowerCase()) {
                    case "n" -> {
                        if (pagination.hasNextPage()){
                            pagination.nextPage();
                        }
                    }
                    case "p" -> {
                        if (pagination.hasPreviousPage()){
                            pagination.previousPage();
                        }
                    }
                    case "1" -> {
                        return;
                    }
                    default -> throw new IllegalArgumentException("Invalid option");
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void viewRecentBorrows() {
        ConsoleStyle.clearScreen();
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
        ConsoleStyle.clearScreen();
        List<Fine> fines = LibraryManger.getInstance().getAllFines();

        if (fines.isEmpty()) {
            System.out.println("No fines found.");
            return;
        }

        for (Fine f : fines) {
            System.out.println(f);
        }
    }

    private static void searchUserById() {
        ConsoleStyle.clearScreen();
        String id = ScannerWrapper.nextLine("Enter user ID: ");
        NormalUser user = LibraryManger.getInstance().getUserById().get(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        System.out.println(user);
    }

    private static void abort() {
        System.out.println(ConsoleStyle.BRIGHT_PURPLE + "Leave all fields empty to abort\n\n" + ConsoleStyle.RESET);
    }

    public static void addItem() {
        while (true) {
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.BOLD + ConsoleStyle.CYAN +
                    "Please select the type of the item: \n" +
                    "1. Book         \n" +
                    "2. Magazine     \n" +
                    "3. Ebook        \n" +
                    "4. AudioBook    \n" +
                    "================\n" +
                    "5. Exit         \n" +
                    ConsoleStyle.RESET);
            try {
                int choice = ScannerWrapper.nextInt("Select option: ");
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> addMagazine();
                    case 3 -> addEbook();
                    case 4 -> addAudioBook();
                    case 5 -> {
                        return;
                    }
                    default -> throw new IllegalArgumentException("Invalid option");
                }
            } catch (RuntimeException e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    public static void addBook(){
        while(true){
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.BOLD + ConsoleStyle.CYAN +
                    "===================adding a Book===================\n" + ConsoleStyle.RESET);
            abort();
            try {
                String title = ScannerWrapper.nextLine("Title: ");
                String id = ScannerWrapper.nextLine("ID: ");
                String publishYear = ScannerWrapper.nextLine("Publish Year: ");
                String category = ScannerWrapper.nextLine("Category: ");
                String author = ScannerWrapper.nextLine("Author: ");
                String pageCount = ScannerWrapper.nextLine("Page Count: ");
                String availableCopies = ScannerWrapper.nextLine("Available Copies: ");
                String isbn = ScannerWrapper.nextLine("ISBN: ");

                if (title.isBlank() && id.isBlank() && publishYear.isBlank() && category.isBlank() && author.isBlank() && pageCount.isBlank() && availableCopies.isBlank() && isbn.isBlank()) {
                    return;
                } else{
                    Catalog.getInstance().addItem(new Book(title, id, Integer.parseInt(publishYear), category, author, Integer.parseInt(pageCount), Long.parseLong(availableCopies), isbn));
                    System.out.println("Item add successfully.");
                    ScannerWrapper.pause();
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleStyle.RED + "Please Enter a number for number fields." + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            } catch (RuntimeException e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    public  static void addMagazine(){
        while(true){
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.BOLD + ConsoleStyle.CYAN +
                    "===================adding a Magazine===================\n" + ConsoleStyle.RESET);
            abort();
            try {
                String title = ScannerWrapper.nextLine("Title: ");
                String id = ScannerWrapper.nextLine("ID: ");
                String publishYear = ScannerWrapper.nextLine("Publish Year: ");
                String category = ScannerWrapper.nextLine("Category: ");
                String availableCopies = ScannerWrapper.nextLine("Available Copies: ");
                String frequency = ScannerWrapper.nextLine("Publication Frequency: ");
                String issn = ScannerWrapper.nextLine("ISSN: ");

                if (title.isBlank() && id.isBlank() && publishYear.isBlank() && category.isBlank() && availableCopies.isBlank() && frequency.isBlank() && issn.isBlank()) {
                    return;
                } else{
                    Catalog.getInstance().addItem(new Magazine(title, id, Integer.parseInt(publishYear), category, Long.parseLong(availableCopies), issn, PublicationFrequency.fromString(frequency)));
                    System.out.println("Item add successfully.");
                    ScannerWrapper.pause();
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleStyle.RED + "Please Enter a number for number fields." + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            } catch (RuntimeException e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    public static void addEbook(){
        while(true){
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.BOLD + ConsoleStyle.CYAN +
                    "===================adding an Ebook===================\n" + ConsoleStyle.RESET);
            abort();
            try {
                String title = ScannerWrapper.nextLine("Title: ");
                String id = ScannerWrapper.nextLine("ID: ");
                String publishYear = ScannerWrapper.nextLine("Publish Year: ");
                String category = ScannerWrapper.nextLine("Category: ");
                String digitalFormat = ScannerWrapper.nextLine("Digital Format: ");
                String size = ScannerWrapper.nextLine("Size: ");
                String url = ScannerWrapper.nextLine("Download URL: ");
                String pageCount = ScannerWrapper.nextLine("Page Count: ");

                if (title.isBlank() && id.isBlank() && publishYear.isBlank() && category.isBlank() && digitalFormat.isBlank() && size.isBlank() && url.isBlank() && pageCount.isBlank()) {
                    return;
                } else{
                    Catalog.getInstance().addItem(new Ebook(title, id, Integer.parseInt(publishYear), category, DigitalFormat.fromString(digitalFormat), Integer.parseInt(size), url, Integer.parseInt(pageCount)));
                    System.out.println("Item add successfully.");
                    ScannerWrapper.pause();
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleStyle.RED + "Please Enter a number for number fields." + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            } catch (RuntimeException e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    public static void addAudioBook(){
        while(true){
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.BOLD + ConsoleStyle.CYAN +
                    "===================adding an AudioBook===================\n" + ConsoleStyle.RESET);
            abort();
            try {
                String title = ScannerWrapper.nextLine("Title: ");
                String id = ScannerWrapper.nextLine("ID: ");
                String publishYear = ScannerWrapper.nextLine("Publish Year: ");
                String category = ScannerWrapper.nextLine("Category: ");
                String digitalFormat = ScannerWrapper.nextLine("Digital Format: ");
                String size = ScannerWrapper.nextLine("Size: ");
                String url = ScannerWrapper.nextLine("Download URL: ");
                String pageCount = ScannerWrapper.nextLine("Page Count: ");

                if (title.isBlank() && id.isBlank() && publishYear.isBlank() && category.isBlank() && digitalFormat.isBlank() && size.isBlank() && url.isBlank() && pageCount.isBlank()) {
                    return;
                } else{
                    Catalog.getInstance().addItem(new AudioBook(title, id, Integer.parseInt(publishYear), category, DigitalFormat.fromString(digitalFormat), Integer.parseInt(size), url, Integer.parseInt(pageCount)));
                    System.out.println("Item add successfully.");
                    ScannerWrapper.pause();
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleStyle.RED + "Please Enter a number for number fields." + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            } catch (RuntimeException e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    public static void seeAllTickets(){
        while (true){
            ConsoleStyle.clearScreen();
            for (SupportTicket ticket : LibraryManger.getInstance().getAllTickets()) {
                System.out.println(ConsoleStyle.YELLOW + ticket + ConsoleStyle.RESET + "\n");
            }
            System.out.println();

            String id = ScannerWrapper.nextLine("Enter the ticket id to answer(Leave empty to return): ");
            if (id.isBlank()){
                return;
            } else{
                String answer = ScannerWrapper.nextLine("Enter the answer: ");
                LibraryManger.getInstance().getTicketMap().get(id).setAnswer(answer);
                System.out.println(ConsoleStyle.GREEN + "Ticket answered successfully" + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }

    }
}
