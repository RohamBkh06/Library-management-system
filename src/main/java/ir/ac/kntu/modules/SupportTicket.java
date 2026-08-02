package ir.ac.kntu.modules;

import ir.ac.kntu.util.IdGenerator;

import java.io.Serializable;
import java.time.LocalDate;

public class SupportTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    private NormalUser user;
    private String id;
    private Department department;
    private String message;
    private boolean isAnswered;
    private String answer;
    private LocalDate createdDate;
    private LocalDate answeredDate;

    public SupportTicket(NormalUser user, Department department, String message){
        this.user = user;
        this.message = message;
        this.department = department;
        this.createdDate = LocalDate.now();
        this.answeredDate = null;
        isAnswered = false;
        this.id = IdGenerator.generateTicketId();
        this.answer = "No answers has been submitted by the supporters yet.";
    }

    public void setAnswer(String answer){
        this.answer = answer;
        isAnswered = true;
        this.answeredDate = LocalDate.now();
    }

    public NormalUser getUser() {
        return user;
    }

    public String getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }

    public String getMessage() {
        return message;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public String getAnswer() {
        return answer;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getAnsweredDate() {
        return answeredDate;
    }

    @Override
    public String toString() {
        return "SupportTicket{" +
                "user= " + user +
                ", id= " + id +
                ", department= " + department +
                "\n, message='" + message +
                ", answer='" + answer +
                '}';
    }
}
