package ir.ac.kntu.util;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.Catalog;

import java.io.*;

public final class SaveLoadManager {

    private static final String FILE_NAME = "library.dat";

    private SaveLoadManager() {
    }

    public static void save() {
        LibraryState state = new LibraryState(LibraryManger.getInstance(), Catalog.getInstance(), SystemProperties.getInstance());

        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FILE_NAME)))) {
            out.writeObject(state);

        } catch (IOException e) {
            throw new RuntimeException("Saving data failed.", e);
        }
    }

    public static void load() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {

            LibraryState state = (LibraryState) in.readObject();
            LibraryManger.setInstance(state.getLibraryManager());
            Catalog.setInstance(state.getCatalog());
            SystemProperties.setInstance(state.getSystemProperties());

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Loading data failed.", e);
        }
    }
}