package ir.ac.kntu.modules;

public class Supporter implements Entity{
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Department department;

    public Supporter(String firstName, String lastName, String userName, String password, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.department = department;
    }

    public void addItem(LibraryItem item){
        Catalog catalog = Catalog.getInstance();
        catalog.addItem(item);
    }

    public void answer(SupportTicket ticket, String message){
        ticket.setAnswer(message);
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Department getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return " Role: Supporter{" +
                " firstName= " + firstName +
                ", lastName= " + lastName +
                ", userName= " + userName +
                ", password= " + password +
                ", Department= " + department +
                '}';
    }
}
