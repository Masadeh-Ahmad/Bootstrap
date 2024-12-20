package org.example;

import org.example.database.DatabaseConfig;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DatabaseConfig.of("mongodb://localhost:27017","bootstrap");
        BootStrap bootStrap = new BootStrap();
        bootStrap.run();
    }
}