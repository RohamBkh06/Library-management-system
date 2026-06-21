package ir.ac.kntu.modules;

public class Supporter{
    private String name;
    private String userName;
    private String password;

    public Supporter(String name, String userName, String password) {
        this.name = name;
        this.userName = userName;
        this.password = password;
    }

    public void addItem(LibraryItem item){
        Catalog catalog = Catalog.getInstance();
        catalog.addItem(item);
    }
}
