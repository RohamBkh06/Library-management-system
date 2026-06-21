package ir.ac.kntu.main;

import ir.ac.kntu.modules.Borrowed;
import ir.ac.kntu.modules.Fine;
import ir.ac.kntu.modules.NormalUser;
import ir.ac.kntu.modules.Supporter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LibraryManger {
    private static LibraryManger instance;
    private List<NormalUser> userList;
    private List<Supporter> supporterList;


    private LibraryManger(){
        this.supporterList = new ArrayList<>();
        this.userList = new ArrayList<>();
    }

    public static LibraryManger getInstance(){
        if (instance == null){
            instance = new LibraryManger();
        }
        return instance;
    }

    public List<Borrowed> getRecentBorrows(){
        List<Borrowed> ans = new ArrayList<>();
        for (NormalUser normalUser : userList) {
            ans.addAll(normalUser.getBorrowedList());
        }
        ans.sort(Comparator.comparing(Borrowed::getBorrowDate).reversed());
        return ans;
    }

    public List<Fine> getAllFines(){
        List<Fine> ans = new ArrayList<>();
        for (NormalUser normalUser : userList) {
            ans.addAll(normalUser.getFines());
        }
        ans.sort(Comparator.comparing(Fine::getAmount));
        return ans;
    }
}
