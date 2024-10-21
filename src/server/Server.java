package server;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {

	public static void main(String[] args) throws AlreadyBoundException {
		/**
		 * 
		 */
		
		int PORT = 1099;
		try {
			Registry registry = LocateRegistry.createRegistry(PORT);
			
			UIServer uiServer = new UIServer();
			uiServer.setTextPort(PORT);
			
			HashMap<String, User> userList = new HashMap<>();
			List<String> userOnline = new ArrayList<>();
			
		    try (FileInputStream fileIn = new FileInputStream("src\\server\\database.txt");
		               ObjectInputStream in = new ObjectInputStream(fileIn)) {		    			
		               userList = (HashMap<String, User> ) in.readObject();
		               System.out.println("User list has been loaded from userList.dat");
		               userList.forEach((t, u) -> System.out.println(u.getUsername()) );  
		
		    } catch (Exception e) {
		           e.printStackTrace();
		    }
		    
			BankingServiceInterface bankingServiceSkeleton = new BankingService(uiServer, userList, userOnline);
			
			SynchronizationInterface syncSkeleton = new SyncService(uiServer, userList, userOnline);
			
			registry.rebind("bankingService", bankingServiceSkeleton);
			
			registry.rebind("syncService", syncSkeleton);
			
			
			System.out.println("run");
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
