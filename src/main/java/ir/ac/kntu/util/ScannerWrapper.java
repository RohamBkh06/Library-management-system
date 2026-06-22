package ir.ac.kntu.util;

import java.util.Scanner;

public final class ScannerWrapper {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ScannerWrapper() {
        throw new AssertionError("Utility class");
    }

    public static void rewritePrompt(String message) {
        ConsoleStyle.restoreCursor();
        ConsoleStyle.clearLine();
        System.out.print(ConsoleStyle.BRIGHT_RED + message + ConsoleStyle.RESET);
    }


    public static String nextLine() {
        return SCANNER.nextLine();
    }

    public static String nextLine(String prompt) {
        System.out.println(prompt);
        return SCANNER.nextLine();
    }

    public static int nextInt() {

        ConsoleStyle.saveCursor();

        while (true) {
            try {
                return Integer.parseInt(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                rewritePrompt("Invalid integer. Try again: ");
            }
        }
    }

    public static int nextInt(String prompt) {
        System.out.println(prompt);
        return nextInt();
    }

    public static long nextLong() {

        ConsoleStyle.saveCursor();

        while (true) {
            try {
                return Long.parseLong(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                rewritePrompt("Invalid long value. Try again: ");
            }
        }
    }

    public static long nextLong(String prompt) {
        System.out.println(prompt);
        return nextLong();
    }

    public static double nextDouble() {

        ConsoleStyle.saveCursor();

        while (true) {
            try {
                return Double.parseDouble(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                rewritePrompt("Invalid decimal number. Try again: ");
            }
        }
    }

    public static double nextDouble(String prompt) {
        System.out.println(prompt);
        return nextDouble();
    }

    public static boolean nextBoolean() {

        ConsoleStyle.saveCursor();

        while (true) {

            String input = SCANNER.nextLine().trim().toLowerCase();

            if (input.equals("true")
                    || input.equals("yes")
                    || input.equals("y")) {
                return true;
            }

            if (input.equals("false")
                    || input.equals("no")
                    || input.equals("n")) {
                return false;
            }

            rewritePrompt("Enter true/false (yes/no): ");
        }
    }

    public static boolean nextBoolean(String prompt) {
        System.out.println(prompt);
        return nextBoolean();
    }

    public static void pause() {
        ConsoleStyle.flush();
        System.out.print(ConsoleStyle.GREEN +"Press Enter to continue..." + ConsoleStyle.RESET);
        SCANNER.nextLine();
    }

    public static void close() {
        SCANNER.close();
    }
}