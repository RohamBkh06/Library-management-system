package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;
import ir.ac.kntu.util.ConsoleStyle;
import ir.ac.kntu.util.EmailService;
import ir.ac.kntu.util.HtmlReportGenerator;
import ir.ac.kntu.util.ScannerWrapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class App {

    public static void test(){
        LibraryManger.getInstance().addAdmin(new Admin("Parsa", "Abdollahi", "Parsa_admin", "1386", Admin.NULL_ADMIN));
        LibraryManger.getInstance().addUser(new Student("Ali", "Ahmadi", "STU-123456", "ali.ahmadi@gmail.com", "09123456789", "Ali@1234"));
        LibraryManger.getInstance().addUser(new Faculty("Reza", "Karimi", "FAC-124578", "r.karimi@university.edu", "09125556677", "Prof@123"));
        LibraryManger.getInstance().addUser(new Guest("Roham", "Bakhtiari", "GST-125690", "roham.bkh@gmail.com", "09123456789", "Guest@123"));
        Supporter supporter = new Supporter("Ali", "Ahmadi", "ali_support", "123456", List.of(Department.FINANCIAL_AFFAIRS, Department.REPORT_PROBLEM));
        LibraryManger.getInstance().addSupporter(supporter);
        supporter.addItem(new Magazine("Time", "MAG-11111111", 2025, "News", 12, "0040-781X", PublicationFrequency.WEEKLY));
        supporter.addItem(new Book("Clean Code", "BOK-12453678", 2008, "Programming", "Robert C. Martin", 464, 5, "9780132350884"));
        supporter.addItem(new Book("Introduction to Algorithms", "BOK-12345678", 2022, "Programming", "Thomas H. Cormen", 1312, 2, "9780262046305"));
        supporter.addItem(new Ebook("Java Concurrency in Practice", "EBK-13264544", 2020, "Programming", DigitalFormat.PDF, 12.5, "https://library.local/ebooks/java-concurrency.pdf", 432));
        supporter.addItem(new AudioBook("The Cosmos Explained", "AUD-25874693", 2023, "Science", DigitalFormat.MP3, 540.0, "https://library.local/audio/cosmos.mp3", 390));

    }

    public static void main(String[] args) {
        test();
        while(true){
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.BOLD + ConsoleStyle.CYAN +
                    "===============================================================\n" +
                    "                   Library Management System                   \n" +
                    "===============================================================" + ConsoleStyle.RESET);
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "1. Sign up         \n" +
                    "2. Log in          \n" +
                    "3. Exit            \n" + ConsoleStyle.RESET);

            int select = ScannerWrapper.nextInt("Please select one of the options: ");
            switch (select) {
                case 1:
                    userSignUp();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    finishProgram();
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
            System.out.println(ConsoleStyle.BRIGHT_PURPLE + "Leave all fields empty to exit\n\n" + ConsoleStyle.RESET);
            try {
                String firstName = ScannerWrapper.nextLine("First Name: ");
                String lastName = ScannerWrapper.nextLine("Last Name: ");
                String id = ScannerWrapper.nextLine("Member ID: ");
                String email = ScannerWrapper.nextLine("Email: ");
                String phoneNumber = ScannerWrapper.nextLine("Phone Number: ");
                String password = ScannerWrapper.nextLine("Password: ");

                if (id.contains("GST")){
                    UserMenu.show(twoFA(new Guest(firstName, lastName, id, email, phoneNumber, password)));
                } else if (id.contains("STU")) {
                    UserMenu.show(twoFA(new Student(firstName, lastName, id, email, phoneNumber, password)));
                } else if (id.contains("FAC")) {
                    UserMenu.show(twoFA(new Faculty(firstName, lastName, id, email, phoneNumber, password)));
                } else if (firstName.isBlank() && lastName.isBlank() && id.isBlank() && email.isBlank() && phoneNumber.isBlank() && password.isBlank()){
                    return;
                } else{
                    throw new IllegalStateException("Invalid ID tag");
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
        if (LibraryManger.getInstance().getUserById().containsKey(user.getId())){
            throw new IllegalStateException("User already exists.");
        }
        int code = 123_456;
        //new Random().nextInt(100000,1000000);

        EmailService.sendVerificationCode(user.getEmail(), code);

        System.out.println("Verification code sent.");
        int enteredCode = ScannerWrapper.nextInt("Verification Code: ");

        if (enteredCode != code) {
            throw new IllegalStateException("Wrong verification code.");
        }
        LibraryManger.getInstance().addUser(user);
        return user;
    }

    public static void login() {
        while (true) {
            try {
                ConsoleStyle.clearScreen();
                System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                        "1. Supporter   \n" +
                        "2. Admin       \n" +
                        "3. User        \n" +
                        "4. Exit        \n" + ConsoleStyle.RESET);

                int select = ScannerWrapper.nextInt("Please select one of the options: ");
                switch (select){
                    case 1 -> {
                        String password = ScannerWrapper.nextLine("Password: " + ConsoleStyle.PURPLE + "Leave Empty to return." + ConsoleStyle.RESET);
                        if (password.isBlank()){
                            return;
                        }
                        Supporter supporter = LibraryManger.getInstance().loginSupporter(password);
                        SupporterMenu.show(supporter);
                    }
                    case 2-> {
                        String password = ScannerWrapper.nextLine("Password: " + ConsoleStyle.PURPLE + "Leave Empty to return." + ConsoleStyle.RESET);
                        if (password.isBlank()){
                            return;
                        }
                        Admin admin = LibraryManger.getInstance().loginAdmin(password);
                        //adminMenu
                    }
                    case 3-> {
                        String id = ScannerWrapper.nextLine("User ID: " + ConsoleStyle.PURPLE + "Leave Empty to return." + ConsoleStyle.RESET);
                        if (id.isBlank()){
                            return;
                        }
                        NormalUser user = LibraryManger.getInstance().loginUser(id);
                        UserMenu.show(user);
                    }
                    case 4 -> {
                        return;
                    }
                    default -> {
                        throw new IllegalArgumentException("Selection out of bound.");
                    }

                }

            } catch (RuntimeException e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    public static void finishProgram(){
        ScannerWrapper.close();
        try {
            HtmlReportGenerator.generateReport("report.html");
            System.out.println("Report generated successfully.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
