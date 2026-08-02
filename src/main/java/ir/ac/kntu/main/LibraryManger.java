package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

public class LibraryManger implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public static void setInstance(LibraryManger manager){
        instance = manager;
    }

    public void addTicket(SupportTicket ticket){
        this.ticketById.put(ticket.getId(), ticket);
    }

    public void addUser(NormalUser user){
        if (this.userById.containsKey(user.getId())){
            throw new IllegalStateException("User already exists.");
        }
        this.userById.put(user.getId(), user);
    }

    public void removeUser(NormalUser user){
        this.userById.remove(user.getId());
    }

    public void addSupporter(Supporter supporter){
        if (this.supporterByPassword.containsKey(supporter.getPassword())){
            throw new IllegalStateException("Supporter already exists.");
        }
        this.supporterByPassword.put(supporter.getPassword(), supporter);
    }

    public void removeSupporter(Supporter supporter){
        this.supporterByPassword.remove(supporter.getPassword());
    }

    public void addAdmin(Admin admin){
        if (this.adminByPassword.containsKey(admin.getPassword())){
            throw new IllegalStateException("Admin already exists.");
        }
        this.adminByPassword.put(admin.getPassword(), admin);
    }

    public void removeAdmin(Admin editor, Admin target){
        if (editor.canEdit(target)){
            for (Admin admin : adminByPassword.values()) {
                if (admin.getCreator().equals(target)){
                    admin.setCreator(target.getCreator());
                }
            }
            this.adminByPassword.remove(target.getPassword());
        } else {
            throw new IllegalStateException("You don't have the access to edit/remove this admin.");
        }
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

    public Map<String, Supporter> getSupporterByPassword() {
        return new HashMap<>(supporterByPassword);
    }

    public Map<String, Admin> getAdminByPassword() {
        return new HashMap<>(adminByPassword);
    }

    public Admin loginAdmin(String password) {
        Admin admin = adminByPassword.get(password);
        if (admin == null) {
            throw new IllegalArgumentException("Wrong password");
        }  else if (!admin.isActive()){
            throw new IllegalStateException("Admin's account is not active.");
        }
        return admin;
    }

    public Supporter loginSupporter(String password) {
        Supporter supporter = supporterByPassword.get(password);
        if (supporter == null) {
            throw new IllegalArgumentException("Wrong password");
        } else if (!supporter.isActive()){
            throw new IllegalStateException("Supporter's account is not active.");
        }
        return supporter;
    }

    public NormalUser loginUser(String id) {
        NormalUser user = userById.get(id);
        if (user == null) {
            throw new IllegalArgumentException("Wrong ID");
        } else if (!user.isActive()){
            throw new IllegalStateException("User's account is not active.");
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

    public List<Entity> filteredSearch(Predicate<Entity> predicate){
        ArrayList<Entity> ans = new ArrayList<>();
        for (Entity entity : getAllEntities()) {
            if (predicate.test(entity)){
                ans.add(entity);
            }
        }
        return ans;
    }

    public void reset(){
        userById.clear();
        supporterByPassword.clear();
        ticketById.clear();
        adminByPassword.clear();
    }
}
