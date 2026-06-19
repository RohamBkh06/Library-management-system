package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
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

    protected NormalUser(String firstName, String lastName, String id, String email, String phoneNum,String password){
        setId(id);
        setEmail(email);
        setPhoneNum(phoneNum);
        setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.wallet = new Wallet();
        this.borrowedList = new HashMap<>();
        this.ticketList = new ArrayList<>();

    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setId(String id) {
        if (!Validator.isValidMemberId(id)){
            throw new IllegalArgumentException("Invalid Id.");

        }
        this.id = id;
    }

    public void setEmail(String email) {
        if (!Validator.isValidEmail(email)){
            throw new IllegalArgumentException("Invalid Email.");
        }
        this.email = email;
    }

    public void setPhoneNum(String phoneNum) {
        if (!Validator.isValidPhoneNum(phoneNum)){
            throw new IllegalArgumentException("Invalid Phone number.");
        }
        this.phoneNum = phoneNum;
    }

    public void setPassword(String password) {
        if (!Validator.isValidPassword(password)){
            throw new IllegalArgumentException("Password is weak.");
        }
            this.password = password;
    }
}
