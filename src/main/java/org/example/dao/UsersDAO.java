package org.example.dao;

import org.bson.Document;
import org.example.database.DatabaseConfig;
import org.example.model.User;

public class UsersDAO extends MongoDAO {

    public UsersDAO(DatabaseConfig databaseConfig){
        super(databaseConfig,"users");
    }
    public Document getUser(String username, String password) throws Exception {
        try (databaseConfig){
            Document document = read("username",username);
            User user = User.fromDocument(document);
            if(user.checkPassword(password))
                return document;
            return null;
        }
    }

    @Override
    public synchronized void create(Document document) throws Exception {
        Document user = read("username",User.fromDocument(document).getUsername());
        if(user == null)
            super.create(document);
    }
}
