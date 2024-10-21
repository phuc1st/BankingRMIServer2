package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankingServiceInterface extends Remote {
    boolean login(String username, String password) throws RemoteException;
    boolean withdraw(String username, double amount) throws RemoteException;
    double getBalance(String username) throws RemoteException;
    double deposit(String username, double num) throws RemoteException;
    void logout(String username) throws RemoteException;
}
