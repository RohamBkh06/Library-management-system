package ir.ac.kntu.modules;

public class Admin implements Entity{
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Admin creator;
    public static final Admin NULL_ADMIN = new Admin("", "","", "", null);

    public Admin(String firstName, String lastName, String userName, String password, Admin creator) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.creator = creator;
    }


    public Admin getCreator() {
        return creator;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean canEdit(Admin target) {
        if (target.getCreator().equals(this) || this.equals(target)) {
            return true;
        } else if (target.getCreator() == null) {
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
                '}';
    }
}
