package ir.ac.kntu.modules;

import java.util.*;
import java.util.function.Predicate;

public class Catalog {
    private static Catalog instance;
    private final List<LibraryItem> items;
    private final Map<String, LibraryItem> itemsByTitle;


    private Catalog(){
        this.items = new ArrayList<>();
        this.itemsByTitle = new HashMap<>();
    }

    public Catalog getInstance(){
        if (instance == null){
            instance = new Catalog();
        }
        return instance;
    }

    public void addItem(LibraryItem item){
        this.items.add(item);
        this.itemsByTitle.put(item.getTitle(), item);
    }

    public List<LibraryItem> getItems(){
        return new ArrayList<>(this.items);
    }

    public List<LibraryItem> getItemByTitle(String title){
        List<LibraryItem> ans = new ArrayList<>();
        for (String s : itemsByTitle.keySet()) {
            if (s.contains(title)){
                ans.add(this.itemsByTitle.get(s));
            }
        }
        return ans;
    }

    public List<LibraryItem> filteredSearch(Predicate<LibraryItem> predicate){
        List<LibraryItem> ans = new ArrayList<>();
        for (LibraryItem item : items) {
            if (predicate.test(item)){
                ans.add(item);
            }
        }
        return ans;
    }
}
