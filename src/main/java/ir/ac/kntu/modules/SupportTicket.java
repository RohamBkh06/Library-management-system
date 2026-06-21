package ir.ac.kntu.modules;

import ir.ac.kntu.util.IdGenerator;

public class SupportTicket {
    private NormalUser user;
    private String id;
    private TicketType type;
    private String message;
    private boolean isAnswered;
    private String answer;

    public SupportTicket(NormalUser user, TicketType type, String message){
        this.user = user;
        this.message = message;
        this.type = type;
        isAnswered = false;
        this.id = IdGenerator.generateTicketId();
        this.answer = "No answers has been submitted by the supporters yet.";
    }

    public void setAnswer(String answer){
        this.answer = answer;
        isAnswered = true;
    }


}
