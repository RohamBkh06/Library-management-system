package ir.ac.kntu.modules;

import ir.ac.kntu.main.LibraryManger;
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
    private Map<String, Borrowed> allBorrows;
    private List<SupportTicket> ticketList;

    public abstract int getBorrowLimit();

    public int activeBorrows(){
        int counter = 0;
        for (Borrowed borrowed : allBorrows.values()) {
            if (!borrowed.isReturned()){
                counter++;
            }
        }
        return counter;
    }

    public void borrowItem(LibraryItem item) {
        if( this.activeBorrows() >= this.getBorrowLimit()){
            throw new IllegalStateException("Borrow limit exceeded");
        } else if (this.hasUnpaidFine()){
            throw new IllegalStateException("You have unpaid fine(s)");
        } else if (!item.lend()){
            throw new IllegalStateException("Item not available");
        } else{
            Borrowed borrowed = new Borrowed(item, this);
            allBorrows.put(borrowed.getId(), borrowed);
        }
    }

    public void returnItem(Borrowed borrowed){
        borrowed.returnBorrow();
    }

    protected NormalUser(String firstName, String lastName, String id, String email, String phoneNum,String password){
        setId(id);
        setEmail(email);
        setPhoneNum(phoneNum);
        setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.wallet = new Wallet();
        this.allBorrows = new HashMap<>();
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

    public List<Fine> getFines(){
        List<Fine> ans = new ArrayList<>();
        for (Borrowed item : this.allBorrows.values()) {
            if (!item.getFine().isPaid()){
                ans.add(item.getFine());
            }
        }
        return ans;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Map<String, Borrowed> getBorrowedMap() {
        return new HashMap<>(allBorrows);
    }

    public List<Borrowed> getBorrowedList() {
        return new ArrayList<>(allBorrows.values());
    }

    public void requestSupport(String message, TicketType type){
        SupportTicket ticket = new SupportTicket(this, type, message);
        this.ticketList.add(ticket);
        LibraryManger.getInstance().addTicket(ticket);
    }

    public boolean hasUnpaidFine(){
        for (Borrowed borrowed : allBorrows.values()) {
            if (!borrowed.getFine().isPaid()){
                return true;
            }
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, Borrowed> getAllBorrows() {
        return allBorrows;
    }

    public List<SupportTicket> getTicketList() {
        return new ArrayList<>(ticketList);
    }

    @Override
    public String toString() {
        return "User{ " + "firstName= " + firstName + ", lastName= " + lastName + ", id= " + id + ", email= " + email + ", phoneNum= " + phoneNum + " }";
    }
}
