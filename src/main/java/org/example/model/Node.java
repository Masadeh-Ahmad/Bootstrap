package org.example.model;

import com.google.gson.Gson;
import org.bson.Document;

import java.io.Serializable;

public class Node implements Serializable {
    private String nodeId;
    private String address;
    private int numOfUsers;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(int numOfUsers) {
        this.numOfUsers = numOfUsers;
    }
    public Document toDocument() {
        return Document.parse(new Gson().toJson(this));
    }

    public static Node fromDocument(Document doc) {
        if(doc == null)
            return null;
        return new Gson().fromJson(doc.toJson(), Node.class);
    }
}
