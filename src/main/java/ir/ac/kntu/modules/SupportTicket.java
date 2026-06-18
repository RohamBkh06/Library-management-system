package ir.ac.kntu.modules;

public class SupportTicket {
    private TicketType type;
    private String message;
    private boolean isAnswered;
    private String answer;

    public SupportTicket(TicketType type, String message){
        this.message = message;
        this.type = type;
        isAnswered = false;
        this.answer = "No answers has been submitted by the supporters yet.";
    }

    public void setAnswer(String answer){
        this.answer = answer;
        isAnswered = true;
    }


}
