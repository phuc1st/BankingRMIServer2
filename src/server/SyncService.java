package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

public class SyncService extends UnicastRemoteObject implements SynchronizationInterface{
	
	private HashMap<String, User> userList;
	private UIServer uiServer;
	private List<String> userOnline;
	
	protected SyncService(UIServer uiServer, HashMap<String, User> userList, List<String> userOnline) throws RemoteException {
		super();
		this.uiServer = uiServer;
		this.userList = userList;
		this.userOnline = userOnline;
	}
	
	@Override
	public void login(String username) throws RemoteException {
		userOnline.add(username);		
	}

	@Override
	public void withdraw(String username, double amount) throws RemoteException {
		double balance = userList.get(username).getBalance();
		userList.get(username).setBalance(balance - amount);
		uiServer.appendMessage("Người dùng: " + username + " rút "
					+ String.valueOf(balance) + ". Số dư mới: "+ String.valueOf(balance - amount));		
		saveData();
	}

	@Override
	public void deposit(String username, double num) throws RemoteException {
		// TODO Auto-generated method stub
		double balance = userList.get(username).getBalance();
		userList.get(username).setBalance(balance + num);
		uiServer.appendMessage("Người dùng: " + username + " nạp "
				+ String.valueOf(balance) + ". Số dư mới: "+ String.valueOf(balance + num));
		saveData();
	}

	@Override
	public void logout(String username) throws RemoteException {
		userOnline.remove(username);		
	}
	@Override
	public void signup(String username, String password) throws RemoteException {
		userList.put(username, new User(username, password, 0));	
		saveData();
	}
	
	private void saveData() {
	      try (FileOutputStream fileOut = new FileOutputStream("src\\server\\database.txt");
	             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
	             out.writeObject(userList);
	             System.out.println("User list has been saved");
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	}
}
