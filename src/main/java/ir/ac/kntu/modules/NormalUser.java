package ir.ac.kntu.modules;

import java.util.List;

public abstract class NormalUser {
    private String firstName;
    private String lastName;
    private String id;
    private String email;
    private String phoneNum;
    private String password;
    private Wallet wallet;
    private List<Borrowed> borrowedList;

    public abstract int getBorrowLimit();
}
