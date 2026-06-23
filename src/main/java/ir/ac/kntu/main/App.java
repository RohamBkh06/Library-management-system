package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;
import ir.ac.kntu.util.ConsoleStyle;
import ir.ac.kntu.util.EmailService;
import ir.ac.kntu.util.ScannerWrapper;

public class App {

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
