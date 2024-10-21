package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SynchronizationInterface extends Remote{
	void login(String username) throws RemoteException;
    void withdraw(String username, double amount) throws RemoteException;
    void deposit(String username, double num) throws RemoteException;
    void logout(String username) throws RemoteException;
    void signup(String username, String password) throws RemoteException;
}
