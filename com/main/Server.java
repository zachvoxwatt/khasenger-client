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
						System.out.printf("[Server / Incoming] Client at %s has connected\n\n", cSock.getInetAddress().getHostAddress());
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
	
	public void sendAllClient(String content)
	{
		for (ServerThread itor: this.activeConnections) itor.sendMessage(content);
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
			this.sv.terminate(this);
			
			this.cSock.close();
			this.getFromClient.close();
			this.sendToClient.close();

			this.cSock = null;
			this.getFromClient = null;
			this.sendToClient = null;
			
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
				
				switch (b)
				{
					case 1:
						String sec = this.getFromClient.readUTF();
						String name = this.getFromClient.readUTF();
						String info = String.format("[Server / Info] Client @ %s is authenticating...\n\n", this.cSock.getInetAddress().getHostAddress());
						logConsole(info);
						
						if (sec.equals(this.sv.getSecKey()))
						{
							this.sendToClient.writeByte(7);
							this.sendToClient.flush();
							String success = String.format("[Server / Info] Client @ %s authenticated successfully\n\n", this.cSock.getInetAddress().getHostAddress());
							logConsole(success);
							
							String announce = String.format("\t>>>> %s has joined the chat!\n\n", name);
							this.sv.sendAllClient(announce);
							break;
						}
						else
						{
							this.sendToClient.writeByte(-1);
							String fail = String.format("[Server / Info] Client @ %s failed authentication\n\n", this.cSock.getInetAddress().getHostAddress());
							logConsole(fail);
						}
						break;
						
					case -69:
						String username = this.getFromClient.readUTF();
						String onleaveconsole = String.format("[Server / Info] User %s of client @ %s has left the chat...\n\n", username, this.cSock.getInetAddress().getHostAddress());
						logConsole(onleaveconsole);
						break;

					case 17:
						String content = this.getFromClient.readUTF();
						this.sv.sendAllClient(content);
						break;
						
					case -127:
						String onleftconsole = String.format("[Server / Info] Client @ %s has left the chat...\n\n", this.cSock.getInetAddress().getHostAddress());
						logConsole(onleftconsole);
						this.isActive = false;
						break;
						
					case 5:
						this.sendToClient.writeUTF("[Server] E");
						break;
						
					case 6:
						this.sendToClient.writeUTF("[Server] F");
						break;
						
					default:
						String leaveName = this.getFromClient.readUTF();
						String dis = String.format("[Server / Info] Client @ %s disconnected\n\n", this.cSock.getInetAddress().getHostAddress());
						logConsole(dis);
						
						String onleave = String.format("\t>>>> %s has left the chat\n\n", leaveName);
						this.sv.sendAllClient(onleave);
						
						this.isActive = false;
						break;
				}
			}
			
			closeConnection();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public void sendMessage(String text)
	{
		try
		{
			this.sendToClient.writeByte(71);
			this.sendToClient.writeUTF(text);
			this.sendToClient.flush();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	void logConsole(String s) { System.out.printf(s); }
}