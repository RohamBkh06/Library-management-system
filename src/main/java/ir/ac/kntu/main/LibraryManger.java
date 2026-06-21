package ir.ac.kntu.main;

import ir.ac.kntu.modules.NormalUser;
import ir.ac.kntu.modules.SupportTicket;
import ir.ac.kntu.modules.Supporter;

import java.util.ArrayList;
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

    public void answer(SupportTicket ticket, String message){
        ticket.setAnswer(message);
    }
}
