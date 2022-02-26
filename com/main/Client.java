package main;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;

@SuppressWarnings(value="unused")
public class Client
{
    private boolean isActive = false;

    private String ip, seckey, username;
    private Socket cSock;
    private Runnable listener;
    private DataOutputStream sendToServer;
    private DataInputStream getFromServer;
    
    public Client(String ip, String seckey, String username)
    {
    	this.ip = ip; this.seckey = seckey; this.username = username;
    	
        try
        {
            this.cSock = new Socket(ip, 1765);
            this.sendToServer = new DataOutputStream(this.cSock.getOutputStream());
            this.getFromServer = new DataInputStream(this.cSock.getInputStream());
        }
        catch (Exception e) { e.printStackTrace(); } 
        
        if (attemptAuth())
        	System.out.println("Successful Auth");
        else 
        	System.out.println("Failed Auth");
        closeConnection();
    }
    
    boolean attemptAuth()
    {
    	boolean success = false;
    	try
    	{
    		this.sendToServer.writeByte(1);
    		this.sendToServer.writeUTF(this.seckey);
    		if (this.getFromServer.readByte() == 7) success = true;
    	}
    	
    	catch (Exception e) { e.printStackTrace(); }
    	
    	return success;
    }

    public void closeConnection()
    {
        try
        {
        	this.sendToServer.writeByte(-1);
            this.isActive = false;

            this.cSock.close();
            this.getFromServer.close();
            this.sendToServer.close();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) 
    { 
    	EventQueue.invokeLater(new Runnable()
    		{
    			public void run() { Client cl = new Client(args[0], args[1], args[2]); }
    		}
    	); 
    }
}