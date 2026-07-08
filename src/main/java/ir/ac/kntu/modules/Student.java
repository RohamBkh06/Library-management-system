package ir.ac.kntu.modules;

import java.io.Serializable;

public class Student extends NormalUser implements Serializable {

    private static final long serialVersionUID = 1L;

    public Student(String firstName, String lastName, String id, String email, String phoneNum, String password) {
        super(firstName, lastName, id, email, phoneNum, password);
    }

    @Override
    public int getBorrowLimit() {
        return 5;
    }

    @Override
    public String toString() {
        return super.toString() + "Type: Student";
    }
}
