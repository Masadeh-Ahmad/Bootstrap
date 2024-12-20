package org.example.dao;

import org.bson.Document;
import org.example.database.DatabaseConfig;

import java.util.Comparator;

public class NodesDAO extends MongoDAO {

    public NodesDAO(DatabaseConfig databaseConfig){
        super(databaseConfig,"nodes");
    }
   public Document getAppropriateNode() throws Exception {
       return getAll().stream()
                .min(Comparator.comparingInt(n -> n.getInteger("numOfUsers")))
                .orElse(null);
   }
}
