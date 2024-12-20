package org.example;

import org.example.database.DatabaseConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BootStrap {
    private final DatabaseConfig databaseConfig;
    public BootStrap(){
        this.databaseConfig = DatabaseConfig.getInstance();
    }

    public void run() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(4000)){
            ExecutorService executor = new ThreadPoolExecutor(2, 100, 60L, TimeUnit.SECONDS, new java.util.concurrent.LinkedBlockingQueue<Runnable>());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new ClientHandler(clientSocket));
            }
        }
    }
}

