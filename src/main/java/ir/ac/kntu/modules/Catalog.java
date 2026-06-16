package ir.ac.kntu.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catalog {
    private static Catalog instance;
    private final List<LibraryItem> items;
    private final Map<String, LibraryItem> itemsById;


    private Catalog(){
        this.items = new ArrayList<>();
        this.itemsById = new HashMap<>();
    }

    public Catalog getInstance(){
        if (instance == null){
            instance = new Catalog();
        }
        return instance;
    }

    public void addItem(LibraryItem item){
        this.items.add(item);
        this.itemsById.put(item.getId(), item);
    }

    public List<LibraryItem> getItems(){
        return new ArrayList<>(this.items);
    }

    public LibraryItem getItemById(String id){
        return this.itemsById.get(id);
    }
}
