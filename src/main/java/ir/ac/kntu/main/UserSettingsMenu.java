package ir.ac.kntu.main;

import ir.ac.kntu.modules.NormalUser;
import ir.ac.kntu.util.ScannerWrapper;

public final class UserSettingsMenu {

    private UserSettingsMenu() {}

    public static void updateFirstName(NormalUser user) {
        user.setFirstName(ScannerWrapper.nextLine("New First Name: "));
    }

    public static void updateLastName(NormalUser user) {
        user.setLastName(ScannerWrapper.nextLine("New Last Name: "));
    }

    public static void updateEmail(NormalUser user) {
        user.setEmail(ScannerWrapper.nextLine("New Email: "));
    }

    public static void updatePhoneNumber(NormalUser user) {
        user.setPhoneNum(ScannerWrapper.nextLine("New Phone Number: "));
    }

    public static void updatePassword(NormalUser user) {
        user.setPassword(ScannerWrapper.nextLine("New Password: "));
    }
}