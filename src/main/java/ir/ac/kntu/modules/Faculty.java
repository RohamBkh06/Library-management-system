package ir.ac.kntu.modules;

import java.io.Serializable;

public class Faculty extends NormalUser implements Serializable {

    private static final long serialVersionUID = 1L;

    public Faculty(String firstName, String lastName, String id, String email, String phoneNum, String password) {
        super(firstName, lastName, id, email, phoneNum, password);
    }

    @Override
    public int getBorrowLimit() {
        return 10;
    }

    @Override
    public String toString() {
        return super.toString() + "Type: Faculty Member";
    }
}
