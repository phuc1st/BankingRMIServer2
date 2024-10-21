package database;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

import server.User;

public class LoadUser {
    public static void main(String[] args) {
        HashMap<String, User> userList = null;

        try (FileInputStream fileIn = new FileInputStream("src\\server\\database.txt");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            userList = (HashMap<String, User> ) in.readObject();
            System.out.println("User list has been loaded from userList.dat");
            userList.forEach((t, u) -> System.out.println(u.getUsername()) );  

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
