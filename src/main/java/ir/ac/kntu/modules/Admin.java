package ir.ac.kntu.modules;

import java.util.Objects;

public class Admin implements Entity{
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private boolean isActive;
    private Admin creator;
    public static final Admin NULL_ADMIN = new Admin("", "","", "", null);

    public Admin(String firstName, String lastName, String userName, String password, Admin creator) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.creator = creator;
        this.isActive = true;
    }


    public Admin getCreator() {
        return creator;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public boolean canEdit(Admin target) {
        if (target.getCreator().equals(this) || this.equals(target)) {
            return true;
        } else if (target.getCreator().equals(Admin.NULL_ADMIN)) {
            return false;
        }
        return this.canEdit(target.getCreator());
    }

    @Override
    public String toString() {
        return "Role: Admin{" +
                " firstName= " + firstName +
                ", lastName= " + lastName +
                ", userName= " + userName +
                ", password= " + password +
                (this.getCreator().equals(Admin.NULL_ADMIN) ? ", Root Admin" : ", creator= " + creator) +
                "Status= "+ (isActive ? "Active" : "Inactive") +
                " }";
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

    public void setCreator(Admin creator) {
        this.creator = creator;
    }

    public boolean isActive() {
        return isActive;
    }

    public void changeState(Admin editor) {
        if (editor.canEdit(this)){
            isActive = !isActive;
        } else {
            throw new IllegalStateException("You don't have the access to edit this admin's status.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Admin admin = (Admin) obj;
        return Objects.equals(firstName, admin.firstName) && Objects.equals(lastName, admin.lastName) && Objects.equals(userName, admin.userName) && Objects.equals(password, admin.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, userName, password);
    }
}
