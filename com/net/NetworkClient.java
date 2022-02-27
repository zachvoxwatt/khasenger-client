package net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import ui.ProgUI;

public class NetworkClient
{
	private ProgUI parent;
    private String ip, seckey, username, errorMsg;
    private Socket cSock;
    private Runnable listener;
    private DataOutputStream sendToServer;
    private DataInputStream getFromServer;
    private ScheduledExecutorService cpu;
    
    public NetworkClient(String ip, String seckey, String username, ProgUI pui)
    {
    	this.parent = pui;
    	this.ip = ip; this.seckey = seckey; this.username = username;
    	this.cpu = Executors.newSingleThreadScheduledExecutor(new ThreadNamer());
    	
    	this.listener = new Runnable()
    	{
    		public void run()
    		{
    			try
    			{
	    			if (isValidAssetsReady())
	    			{
	    				byte b = getFromServer.readByte();
	    				
	    				switch (b)
	    				{
	    					case 71:
	    						String incomingText = getFromServer.readUTF();
	    						parent.getKhasengerPanel().appendTextToPane(incomingText);
	    						break;
	    				
	    					case -1:
	    						disconnect();
	    						break;
	    				
	    					case -127:
	    						parent.showAuthScreen();
	    						terminateConnection();
	    						break;
	    						
	    					default:
	    						break;
	    				}
	    			}
    			}
    			catch (Exception e) {}
    		}
    	};
    	
    	this.cpu.scheduleWithFixedDelay(listener, 0, 100, TimeUnit.MILLISECONDS);
    }
    
    private boolean isValidAssetsReady()
    { return Objects.nonNull(cSock) && Objects.nonNull(sendToServer) && Objects.nonNull(getFromServer); }
    
    class ThreadNamer implements ThreadFactory
	{ 
    	@Override 
    	public Thread newThread(Runnable r) { return new Thread(r, "Server Listener"); } 
    }
   
    public boolean connect()
    {
    	boolean success = false;
    	
    	try
        {
            this.cSock = new Socket(this.ip, 1765);
            this.sendToServer = new DataOutputStream(this.cSock.getOutputStream());
            this.getFromServer = new DataInputStream(this.cSock.getInputStream());
            success = true;
        }
        catch (Exception e) { this.errorMsg = e.getMessage(); } 
    	return success;
    }
    
    public boolean attemptAuth()
    {
    	boolean success = false;
    	try
    	{
    		this.sendToServer.writeByte(1);
    		this.sendToServer.writeUTF(this.seckey);
    		this.sendToServer.writeUTF(this.username);
    		this.sendToServer.flush();
    		if (this.getFromServer.readByte() == 7) success = true;
    	}
    	
    	catch (Exception e) { e.printStackTrace(); }
    	
    	return success;
    }
    
    public void postMessage(String sender, String content)
    {
    	try
    	{
    		this.sendToServer.writeByte(17);
    		this.sendToServer.writeUTF(sender);
    		this.sendToServer.writeUTF(content);
    		this.sendToServer.flush();
    	}
    	catch (Exception e) { e.printStackTrace(); }
    }
    
    public void notifyLeave()
    {
    	try
    	{
    		this.sendToServer.writeByte(-69);
    		this.sendToServer.writeUTF(this.username);
    		this.sendToServer.flush();
    		disconnect(true);
    	}
    	catch (Exception e) { e.printStackTrace(); }
    }
    
    public void disconnect() { disconnect(false); }

    public void disconnect(boolean b)
    {
        try
        {
        	this.sendToServer.writeByte(-1);
        	this.sendToServer.writeUTF(this.username);
        	this.sendToServer.flush();
        	
            this.cSock.close();
            this.getFromServer.close();
            this.sendToServer.close();
            
            if (b)
            {
            	this.cSock = null;
            	this.getFromServer = null;
            	this.sendToServer = null;
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }
    
    public void terminateConnection()
    {
    	try
    	{
    		this.sendToServer.writeByte(-127);
    		this.sendToServer.flush();
    		
    		this.cSock.close();
            this.getFromServer.close();
            this.sendToServer.close();
            
            this.cSock = null;
        	this.getFromServer = null;
        	this.sendToServer = null;
    	}
    	catch (Exception e) { e.printStackTrace(); }
    }
    
    public void resetInputs(String a, String b, String c) { this.ip = a; this.seckey = b; this.username = c; }
    
    public String getUsername() { return this.username; }
    public String getErrorMessage() { return this.errorMsg; }
}