package database;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import server.User;

public class SaveUser {
    public static void main(String[] args) {
        HashMap<String, User> userList = new HashMap<>();
        userList.put("john_doe",new User("john_doe", "securePassword", 1000.0));
        userList.put("jane_doe2", new User("jane_doe2", "anotherPassword", 2000.0));

        try (FileOutputStream fileOut = new FileOutputStream("src\\server\\database.txt");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(userList);
            System.out.println("User list has been saved to userList.dat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

