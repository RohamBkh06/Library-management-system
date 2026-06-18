package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

import java.util.List;
import java.util.Map;

public abstract class NormalUser {
    private String firstName;
    private String lastName;
    private String id;
    private String email;
    private String phoneNum;
    private String password;
    private Wallet wallet;
    private Map<String, Borrowed> borrowedList;
    private List<SupportTicket> ticketList;

    public abstract int getBorrowLimit();

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public boolean setId(String id) {
        if (Validator.isValidMemberId(id)){
            this.id = id;
            return true;
        }
        return false;
    }

    public boolean setEmail(String email) {
        if (Validator.isValidEmail(email)){
            this.email = email;
            return true;
        }
        return false;
    }

    public boolean setPhoneNum(String phoneNum) {
        if (Validator.isValidPhoneNum(phoneNum)){
            this.phoneNum = phoneNum;
            return true;
        }
        return false;
    }

    public boolean setPassword(String password) {
        if (Validator.isValidPassword(password)){
            this.password = password;
            return true;
        }
        return false;
    }
}
