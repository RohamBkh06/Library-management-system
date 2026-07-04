package ir.ac.kntu.util;

import java.util.ArrayList;
import java.util.List;

public final class Pagination<T> {
    private List<T> list;
    private int currentPage;
    private int pageSize = 10;

    public Pagination(List<T> list){
        this.list = list;
        this.currentPage = 0;
    }

    public List<T> getCurrentPage() {
        int start = currentPage*pageSize;
        int end = Math.min(start+pageSize, list.size());
        return new ArrayList<>(list.subList(start, end));
    }

    public boolean hasNextPage(){
        return (currentPage+1)*pageSize < list.size();
    }

    public boolean hasPreviousPage(){
        return currentPage > 0;
    }

    public void nextPage() {
        if (hasNextPage()) {
            currentPage++;
        }
    }

    public void previousPage() {
        if (hasPreviousPage()) {
            currentPage--;
        }
    }

    public int getCurrentPageNumber(){
        return currentPage;
    }

    public int gerTotalPageNumber(){
        return Math.ceilDiv(list.size(), pageSize);
    }
}
