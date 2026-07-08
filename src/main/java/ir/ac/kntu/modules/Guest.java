package ir.ac.kntu.modules;

import java.io.Serializable;

public class Guest extends NormalUser implements Serializable {

    private static final long serialVersionUID = 1L;

    public Guest(String firstName, String lastName, String id, String email, String phoneNum, String password) {
        super(firstName, lastName, id, email, phoneNum, password);
    }

    @Override
    public int getBorrowLimit() {
        return 2;
    }

    @Override
    public String toString() {
        return super.toString() + "Type: Guest";
    }
}
