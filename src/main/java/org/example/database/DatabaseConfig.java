package org.example.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.NoSuchElementException;

public class DatabaseConfig implements AutoCloseable {
    private static DatabaseConfig databaseConfig;
    private final String URL;
    private final String DB_NAME;
    private MongoClient mongoClient;

    private DatabaseConfig(String url, String databaseName){
        this.URL = url;
        this.DB_NAME = databaseName;
    }
    public static DatabaseConfig of(String url, String databaseName){
        if(databaseConfig == null)
            databaseConfig = new DatabaseConfig(url,databaseName);
        return databaseConfig;
    }
    public static DatabaseConfig getInstance(){
        if(databaseConfig == null)
            throw new NoSuchElementException();
        return databaseConfig;
    }
    public MongoDatabase getDatabase(){
        this.mongoClient = MongoClients.create(URL);
        return mongoClient.getDatabase(DB_NAME);
    }
    public MongoCollection<Document> getCollection(String collection){
        return getDatabase().getCollection(collection);
    }
    @Override
    public void close() throws Exception {
        if(mongoClient != null){
            mongoClient.close();
            mongoClient = null;
        }
    }
}
