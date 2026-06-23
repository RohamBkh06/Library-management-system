package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;

import java.util.*;

public class LibraryManger {
    private static LibraryManger instance;
    private Map<String, NormalUser> userById;
    private Map<String, Supporter> supporterByPassword;


    private LibraryManger(){
        this.supporterByPassword = new HashMap<>();
        this.userById = new HashMap<>();
    }

    public static LibraryManger getInstance(){
        if (instance == null){
            instance = new LibraryManger();
        }
        return instance;
    }

    public void addUser(NormalUser user){
        this.userById.put(user.getId(), user);
    }

    public void addSupporter(Supporter supporter){
        this.supporterByPassword.put(supporter.getPassword(), supporter);
    }

    public List<Borrowed> getRecentBorrows(){
        List<Borrowed> ans = new ArrayList<>();
        for (NormalUser normalUser : userById.values()) {
            ans.addAll(normalUser.getBorrowedList());
        }
        ans.sort(Comparator.comparing(Borrowed::getBorrowDate).reversed());
        return ans.subList(0, 10);
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
}
