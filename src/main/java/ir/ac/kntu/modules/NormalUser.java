package ir.ac.kntu.modules;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.util.SystemProperties;
import ir.ac.kntu.util.Validator;

import java.util.*;

public abstract class NormalUser implements Entity {
    private String firstName;
    private String lastName;
    private String id;
    private String email;
    private String phoneNum;
    private String password;
    private boolean isActive;
    private Wallet wallet;
    private Map<String, Borrowed> borrowById;
    private List<SupportTicket> ticketList;
    private Map<String, Reservation> reservationById;

    public abstract int getBorrowLimit();

    public int activeBorrows(){
        int counter = 0;
        for (Borrowed borrowed : borrowById.values()) {
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
            if (item.getActiveReservation() != null){
                if (!reservationById.containsValue(item.getActiveReservation())){
                    throw new IllegalStateException("This reservation is not yet active.");
                }
                    item.getActiveReservation().borrowReserved();
            }
            Borrowed borrowed = new Borrowed(item, this);
            borrowById.put(borrowed.getId(), borrowed);
        }
    }

    public void returnItem(Borrowed borrowed){
        borrowed.returnBorrow();
    }

    public void reserveItem(LibraryItem item){
        if (this.getWaitingReservations().size() > SystemProperties.getReserveLimit()){
            throw new IllegalStateException("Reserve limit exceeded");
        }
        Reservation reservation = new Reservation(item, this);
        for (Reservation value : reservationById.values()) {
            if (value.equals2(reservation) && (value.getStatus() == ReservationStatus.ACTIVE || value.getStatus() == ReservationStatus.WAITING)){
                throw new IllegalStateException("Item already reserved.");
            }
        }
        reservationById.put(reservation.getId(), reservation);
        item.addToAQueue(reservation);
    }

    protected NormalUser(String firstName, String lastName, String id, String email, String phoneNum,String password){
        setId(id);
        setEmail(email);
        setPhoneNum(phoneNum);
        setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.wallet = new Wallet();
        this.borrowById = new HashMap<>();
        this.ticketList = new ArrayList<>();
        this.reservationById = new HashMap<>();
        this.isActive = true;

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
        for (Borrowed item : this.borrowById.values()) {
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
        return new HashMap<>(borrowById);
    }

    public List<Borrowed> getBorrowedList() {
        return new ArrayList<>(borrowById.values());
    }

    public void requestSupport(String message, Department type){
        SupportTicket ticket = new SupportTicket(this, type, message);
        this.ticketList.add(ticket);
        LibraryManger.getInstance().addTicket(ticket);
    }

    public boolean hasUnpaidFine(){
        for (Borrowed borrowed : borrowById.values()) {
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

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Map<String, Borrowed> getBorrowById() {
        return borrowById;
    }

    public List<SupportTicket> getTicketList() {
        return new ArrayList<>(ticketList);
    }

    public boolean isActive() {
        return isActive;
    }

    public void changeState() {
        isActive = !isActive;
    }

    public Map<String, Reservation> getReservationById() {
        return new HashMap<>(reservationById);
    }

    public List<Reservation> getWaitingReservations(){
        List<Reservation> ans = new ArrayList<>();
        for (Reservation value : reservationById.values()) {
            if (value.getStatus() == ReservationStatus.WAITING){
                ans.add(value);
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "Role: User{ " + "firstName= " + firstName + ", lastName= " + lastName + ", id= " + id + ", email= " + email + ", phoneNum= " + phoneNum + "Status= "+ (isActive ? "Active" : "Inactive") + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        NormalUser that = (NormalUser) obj;
        return Objects.equals(id, that.id) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password);
    }
}
