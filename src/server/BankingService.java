package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BankingService extends UnicastRemoteObject implements BankingServiceInterface{

	private HashMap<String, User> userList;
	private UIServer uiServer;
	private List<String> userOnline;
	private int numUserOnline = 0;
	
	protected BankingService(UIServer uiServer, HashMap<String, User> userList, List<String> userOnline) throws RemoteException {
		super();
		this.uiServer = uiServer;
		this.userList = userList;
		this.userOnline = userOnline;
	}
	
	@Override
	public boolean withdraw(String username, double amount) throws RemoteException {
		double balance = userList.get(username).getBalance();
		if(userList.get(username).getBalance() < amount)		
			return false;
		else {
			userList.get(username).setBalance(balance - amount);
			uiServer.appendMessage("Người dùng: " + username + " rút "
						+ String.valueOf(balance) + ". Số dư mới: "+ String.valueOf(balance - amount));
			uiServer.getSync().withdraw(username, amount);
			saveData();
		}
			
		return true;
	}

	@Override
	public double getBalance(String username) throws RemoteException {		
		return userList.get(username).getBalance();
	}


	@Override
	public boolean login(String username, String password) throws RemoteException {
		boolean result = true;
		if(userOnline.contains(username))
			result = false;
		else
			if(userList.containsKey(username))
			{	
				String truePassword = userList.get(username).getPassword();
				if(!password.equalsIgnoreCase(truePassword))
					result = false;			
			}
			else 
			{
				userList.put(username, new User(username, password, 0));
				uiServer.getSync().signup(username, password);
				saveData();
			}
				
		if(result) {
				userOnline.add(username);
				uiServer.appendMessage(username + " đăng nhập");
				uiServer.getSync().login(username);
				numUserOnline +=1;
				uiServer.setNumOnline(numUserOnline);
		}	
		return result;
	}

	@Override
	public double deposit(String username, double num) throws RemoteException {
		double balance = userList.get(username).getBalance();
		userList.get(username).setBalance(balance + num);
		uiServer.appendMessage("Người dùng: " + username + " nạp "
				+ String.valueOf(balance) + ". Số dư mới: "+ String.valueOf(balance + num));
		
		uiServer.getSync().deposit(username, num);
		
		saveData();
		
		return balance + num;
	}
	@Override
	public void logout(String username) throws RemoteException {
		// TODO Auto-generated method stub
		userOnline.remove(username);		
		
		uiServer.getSync().logout(username);
		
		uiServer.appendMessage(username + " đăng xuất");
		
		numUserOnline -=1;
		uiServer.setNumOnline(numUserOnline);
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
