package org.example.model;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;
import com.google.gson.Gson;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String hashedPassword;
    private String salt;
    private String nodeId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void setHashedPassword(String password) {
        this.salt = BCrypt.gensalt();
        this.hashedPassword = BCrypt.hashpw(password, this.salt);
    }
    public boolean checkPassword(String password) {
        String hashedInput = BCrypt.hashpw(password, this.salt);
        return this.hashedPassword.equals(hashedInput);
    }
    public Document toDocument() {
        return Document.parse(new Gson().toJson(this));
    }

    public static User fromDocument(Document doc) {
        if(doc == null)
            return null;
        return new Gson().fromJson(doc.toJson(), User.class);
    }
}
