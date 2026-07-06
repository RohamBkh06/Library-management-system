package ir.ac.kntu.modules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Supporter implements Entity{
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private boolean isActive;
    private Set<Department> departments;

    public Supporter(String firstName, String lastName, String userName, String password, Collection<Department> department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.departments = new HashSet<>();
        departments.addAll(department);
        this.isActive = true;
    }

    public void addItem(LibraryItem item){
        Catalog catalog = Catalog.getInstance();
        catalog.addItem(item);
    }

    public void answer(SupportTicket ticket, String message){
        if (!this.departments.contains(ticket.getDepartment())){
            throw new IllegalStateException("you Don't have the access to answer this ticket.");
        }
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

    public Set<Department> getDepartments() {
        return new HashSet<>(this.departments);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addDepartment(Department department) {
        if (!this.departments.add(department)) {
            throw new IllegalStateException("Supporter already has access to this department");
        }
    }

    public void removeDepartment(Department department){
        if (!this.departments.contains(department)){
            throw new IllegalStateException("Supporter already doesn't have access to this department");
        }
        this.departments.remove(department);
    }

    public boolean isActive() {
        return isActive;
    }

    public void changeState() {
        isActive = !isActive;
    }

    @Override
    public String toString() {
        return " Role: Supporter{" +
                " firstName= " + firstName +
                ", lastName= " + lastName +
                ", userName= " + userName +
                ", password= " + password +
                ", Departments= " + departments +
                "Status= "+ (isActive ? "Active" : "Inactive") +
                " }";
    }
}
