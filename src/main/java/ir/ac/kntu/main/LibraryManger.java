package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;

import java.util.*;

public class LibraryManger {
    private static LibraryManger instance;
    private Map<String, NormalUser> userById;
    private Map<String, Supporter> supporterByPassword;
    private Map<String, SupportTicket> ticketById;
    private Map<String, Admin> adminByPassword;


    private LibraryManger(){
        this.supporterByPassword = new HashMap<>();
        this.userById = new HashMap<>();
        this.ticketById = new HashMap<>();
        this.adminByPassword = new HashMap<>();
    }

    public static LibraryManger getInstance(){
        if (instance == null){
            instance = new LibraryManger();
        }
        return instance;
    }

    public void addTicket(SupportTicket ticket){
        this.ticketById.put(ticket.getId(), ticket);
    }

    public void addUser(NormalUser user){
        this.userById.put(user.getId(), user);
    }

    public void addSupporter(Supporter supporter){
        this.supporterByPassword.put(supporter.getPassword(), supporter);
    }

    public void addAdmin(Admin admin){
        this.adminByPassword.put(admin.getPassword(), admin);
    }

    public List<Borrowed> getRecentBorrows(){
        List<Borrowed> ans = new ArrayList<>();
        for (NormalUser normalUser : userById.values()) {
            ans.addAll(normalUser.getBorrowedList());
        }
        ans.sort(Comparator.comparing(Borrowed::getBorrowDate).reversed());
        return ans.subList(0, Math.min(10, ans.size()));
    }

    public List<Fine> getAllFines(){
        List<Fine> ans = new ArrayList<>();
        for (NormalUser normalUser : userById.values()) {
            ans.addAll(normalUser.getFines());
        }
        ans.sort(Comparator.comparing(Fine::getAmount));
        return ans;
    }

    public Map<String, NormalUser> getUserById() {
        return new HashMap<>(this.userById);
    }

    public Supporter loginSupporter(String password) {
        Supporter supporter = supporterByPassword.get(password);
        if (supporter == null) {
            throw new IllegalArgumentException("Wrong password");
        }

        return supporter;
    }

    public NormalUser loginUser(String id) {
        NormalUser user = userById.get(id);
        if (user == null) {
            throw new IllegalArgumentException("Wrong ID");
        }

        return user;
    }

    public List<SupportTicket> getAllTickets() {
        return new ArrayList<>(this.ticketById.values());
    }

    public Map<String, SupportTicket> getTicketById() {
        return new HashMap<>(ticketById);
    }

    public List<Entity> getAllEntities() {
        ArrayList<Entity> ans = new ArrayList<>();
        ans.addAll(this.userById.values());
        ans.addAll(this.supporterByPassword.values());
        ans.addAll(this.adminByPassword.values());
        return ans;
    }
}
