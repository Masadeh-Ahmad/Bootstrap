package org.example.dao;

import org.bson.Document;
import org.example.database.DatabaseConfig;

import java.util.ArrayList;
import java.util.List;

public abstract class MongoDAO {
    protected final String collection;
    protected final DatabaseConfig databaseConfig;
    protected MongoDAO(DatabaseConfig databaseConfig,String collection){
        this.databaseConfig = databaseConfig;
        this.collection = collection;
    }
    public List<Document> getAll() throws Exception {
        try (databaseConfig){
            return databaseConfig.getCollection(collection).find().into(new ArrayList<>());
        }
    }
    public synchronized void create(Document document) throws Exception {
        try (databaseConfig){
            databaseConfig.getCollection(collection).insertOne(document);
        }
    }
    public Document read(String key, Object value) throws Exception {
        try (databaseConfig){
            Document query = new Document(key, value);
            return databaseConfig.getCollection(collection).find(query).first();
        }
    }
    public synchronized void update(String key, Object value, Document updatedDocument) throws Exception {
        try (databaseConfig){
            Document query = new Document(key, value);
            databaseConfig.getCollection(collection).replaceOne(query, updatedDocument);
        }
    }
    public synchronized void delete(String key, Object value) throws Exception {
        try (databaseConfig){
            Document query = new Document(key, value);
            databaseConfig.getCollection(collection).deleteOne(query);
        }
    }

}
