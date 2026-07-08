package ir.ac.kntu.util;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.Catalog;

import java.io.Serializable;

public class LibraryState implements Serializable {

    private static final long serialVersionUID = 1L;

    private final LibraryManger libraryManager;
    private final Catalog catalog;
    private final SystemProperties systemProperties;

    public LibraryState(LibraryManger libraryManager, Catalog catalog, SystemProperties systemProperties) {
        this.libraryManager = libraryManager;
        this.catalog = catalog;
        this.systemProperties = systemProperties;
    }

    public LibraryManger getLibraryManager() {
        return libraryManager;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public SystemProperties getSystemProperties() {
        return systemProperties;
    }
}