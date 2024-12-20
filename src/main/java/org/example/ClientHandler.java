package org.example;

import org.bson.Document;
import org.example.dao.NodesDAO;
import org.example.dao.UsersDAO;
import org.example.database.DatabaseConfig;
import org.example.encryption.AESEncryption;
import org.example.model.Credentials;
import org.example.model.Node;
import org.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final AESEncryption encryption = new AESEncryption();
    private final UsersDAO usersDAO;
    private final NodesDAO nodesDAO;
    ObjectMapper objectMapper;
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        usersDAO = new UsersDAO(DatabaseConfig.getInstance());
        nodesDAO = new NodesDAO(DatabaseConfig.getInstance());
    }
    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())){
            objectMapper = new ObjectMapper();
            byte[] encryptedData = (byte[])in.readObject();
            Credentials credentials = objectMapper.readValue(encryption.decrypt(encryptedData), Credentials.class);
            if(createNewUser(credentials) != null) {
                String json = objectMapper.writeValueAsString(credentials);
                byte[] encrypted = encryption.encrypt(json);
                out.writeObject(encrypted);
            }
            else
                out.writeObject(null);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Credentials createNewUser(Credentials credentials){
        try {
            if(usersDAO.read("username",credentials.getUsername()) == null){
                User user = new User();
                user.setUsername(credentials.getUsername());
                user.setHashedPassword(credentials.getPassword());
                Document document = nodesDAO.getAppropriateNode();
                Node node = Node.fromDocument(document);
                document.put("numOfUsers",node.getNumOfUsers()+1);
                user.setNodeId(node.getNodeId());
                usersDAO.create(user.toDocument());
                nodesDAO.update("nodeId",node.getNodeId(),document);
                credentials.setNodeAddress(node.getAddress());
                return credentials;
            }
            User user = User.fromDocument(usersDAO.getUser(credentials.getUsername(), credentials.getPassword()));
            if(user != null){
                Node node = Node.fromDocument(nodesDAO.read("nodeId",user.getNodeId()));
                credentials.setNodeAddress(node.getAddress());
                return credentials;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
