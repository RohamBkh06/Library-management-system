package ir.ac.kntu.modules;

public class Faculty extends NormalUser{

    public Faculty(String firstName, String lastName, String id, String email, String phoneNum, String password) {
        super(firstName, lastName, id, email, phoneNum, password);
    }

    @Override
    public int getBorrowLimit() {
        return 10;
    }
}
