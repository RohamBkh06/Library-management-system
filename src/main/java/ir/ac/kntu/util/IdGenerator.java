package ir.ac.kntu.util;

public final class IdGenerator {
    private static int borrowedNextId = 1;
    private static int fineNextId = 1;


    public static String generateBorrowedId(){
        return String.format("BRW-%06d", borrowedNextId++);
    }

    public static String generateFineId(){
        return String.format("FN-%06d", fineNextId++);
    }
}
