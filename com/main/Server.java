package main;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class Server
{
	private boolean isRunning = false;
	
	private final String securityKey = "zictopia";
	private Thread recv;
	private Server self;
	private Runnable listener;
	private ServerSocket sSock;
	private ScheduledExecutorService dcpus;
	private List<ServerThread> activeConnections;
	
	public Server()
	{
		this.self = this;
		this.dcpus = Executors.newScheduledThreadPool(10, new ThreadNames());
		this.activeConnections = new ArrayList<>();
		try { this.sSock = new ServerSocket(1765); } catch (IOException e) { e.printStackTrace(); }
		
		this.listener = new Runnable()
		{
			public void run()
			{
				System.out.println("Server is OPERATIONAL and running at port 1765!");
				while (isRunning)
				{
					try
					{
						Socket cSock = sSock.accept();
						System.out.printf("[Server] Client at %s has connected\n", cSock.getInetAddress().getHostAddress());
						ServerThread sThrd = new ServerThread(self, cSock);
						
						activeConnections.add(sThrd);
						dcpus.execute(sThrd);
					}
					catch (Exception e) { e.printStackTrace(); }
				}
			}
		};
		
		this.recv = new Thread(this.listener, "Connection Receptionist");
		startServer();
	}
	
	public void startServer()
	{
		this.isRunning = true;
		this.recv.start();
	}
	
	public void stopServer() { this.isRunning = false; }
	public void terminate(ServerThread svThrd) { this.activeConnections.remove(svThrd); }
	
	public String getSecKey() { return this.securityKey; }
	
	class ThreadNames implements ThreadFactory
	{
		private int count = 0;
		@Override
		public Thread newThread(Runnable r) 
		{
			String name = "Operative Thread #" + ++count;
			return new Thread(r, name);
		}
	}
	
	public static void main(String[] args) { EventQueue.invokeLater(Server::new); }
}

class ServerThread implements Runnable
{
	private boolean isActive = true;
	
	private Socket cSock;
	private Server sv;
	private DataOutputStream sendToClient;
	private DataInputStream getFromClient;
	
	public ServerThread(Server srvr, Socket cSock)
	{
		this.cSock = cSock;
		this.sv = srvr;
		
		try
		{
			this.sendToClient = new DataOutputStream(this.cSock.getOutputStream());
			this.getFromClient = new DataInputStream(this.cSock.getInputStream());
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	void closeConnection()
	{
		try
		{
			this.isActive = false;
			this.cSock.close();
			this.getFromClient.close();
			this.sendToClient.close();
			
			this.sv.terminate(this);
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void run() 
	{
		try
		{
			while (this.isActive)
			{
				byte b = this.getFromClient.readByte();
				System.out.println(b);
				
				switch (b)
				{
					case 1:
						String sec = this.getFromClient.readUTF();
						if (sec.equals(this.sv.getSecKey())) this.sendToClient.writeByte(7);
						else this.sendToClient.writeByte(-1);
						break;
						
					case 2:
						this.sendToClient.writeUTF("[Server] B");
						break;

					case 3:
						this.sendToClient.writeUTF("[Server] C");
						break;
						
					case 4:
						this.sendToClient.writeUTF("[Server] D");
						break;
						
					case 5:
						this.sendToClient.writeUTF("[Server] E");
						break;
						
					case 6:
						this.sendToClient.writeUTF("[Server] F");
						break;
						
					default:
						this.isActive = false;
						break;
				}
			}
			
			closeConnection();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
}