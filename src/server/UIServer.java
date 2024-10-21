package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTextField;
import server.SynchronizationInterface;
public class UIServer {

	private JFrame frame;
	private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
	private JTextArea textArea ;
	private JButton btnSyn;
	private SynchronizationInterface syncStub;
	private JLabel lblPort;
	private JTextField textFieldPort;
	private JLabel lblNewLabel;
	
	public SynchronizationInterface getSync() {
		return syncStub;
	}
	
	public void setSync(SynchronizationInterface sync) {
		this.syncStub = sync;
	}
	
	public void setTextPort(int port) {
		lblPort.setText("Port: "+ port);
	}
	
	public void setNumOnline(int num) {
		lblNewLabel.setText("Số người trên Server: " + num);
	}

	/**
	 * Create the application.
	 */
	public UIServer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 670, 464);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblNewLabel = new JLabel("Số người trên Server:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(25, 10, 291, 33);
		frame.getContentPane().add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(UIManager.getBorder("TextArea.border"));
		scrollPane.setBounds(25, 88, 583, 329);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Dialog", Font.PLAIN, 16));
		textArea.setWrapStyleWord(false);
		textArea.setLineWrap(true);
		textArea.setEditable(true);
		textArea.setText("");
		scrollPane.setViewportView(textArea);
		
		btnSyn = new JButton("Đồng bộ");
		btnSyn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSyn.setBounds(441, 10, 154, 33);
		btnSyn.addActionListener(e->{
			Registry registry;
			try {
				registry = LocateRegistry.getRegistry("localhost", Integer.parseInt(textFieldPort.getText()));		
				syncStub = (SynchronizationInterface)registry.lookup("syncService");
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NotBoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		
		});
		
		frame.getContentPane().add(btnSyn);
		
		lblPort = new JLabel("");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPort.setBounds(25, 53, 120, 21);
		frame.getContentPane().add(lblPort);
		
		textFieldPort = new JTextField();
		textFieldPort.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textFieldPort.setBounds(326, 10, 105, 33);
		textFieldPort.setText("1100");
		
		frame.getContentPane().add(textFieldPort);
		textFieldPort.setColumns(10);
		frame.setVisible(true);
	}
	
	public void appendMessage(String msg) {
		Date date = new Date();
		textArea.append(sdf.format(date) + ": " + msg + "\n");
	}
}
