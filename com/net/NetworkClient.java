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
	private boolean playRecv = true;
	private static final int PORT = 1765;
	
	private ClientUser clusr;
	private ProgUI parent;
    private String ip, seckey;
    private Socket cSock;
    private Runnable listener, vitalCheck;
    private DataOutputStream sendToServer;
    private DataInputStream getFromServer;
    private ScheduledExecutorService cpu;
    
    public NetworkClient(String ip, String seckey, String username, ProgUI pui)
    {
    	this.parent = pui;
    	this.clusr = this.parent.getClientUser();
    	this.ip = ip; this.seckey = seckey; this.clusr.setName(username);
    	this.cpu = Executors.newScheduledThreadPool(2, new NetworkClientThreadNamer());
    	
    	this.listener = new Runnable()
    	{
    		public void run() {
    			try {
	    			if (isValidAssetsReady()) {
	    				String msgType = getFromServer.readUTF();
	    				
	    				switch (msgType)
	    				{
	    					case "newMessage":
	    						String comingText = getFromServer.readUTF();
	    						parent.getKhasengerPanel().appendTextToPane(comingText);
	    						if (playRecv) parent.getAudioController().play("recv");
	    						else playRecv = true;
	    						break;
	    						
	    					case "incomingPrevMessage":
	    						receivePrevTexts();
	    						break;
	    						
	    					case "newJoinedUser":
	    						String notiJoin = getFromServer.readUTF();
	    						parent.getKhasengerPanel().appendTextToPane(notiJoin);
	    						parent.getAudioController().play("join_user");
	    						break;
	    						
	    					case "userLeaving":
	    						String notiLeave = getFromServer.readUTF();
	    						parent.getKhasengerPanel().appendTextToPane(notiLeave);
	    						parent.getAudioController().play("leave_user");
	    						break;
	    						
	    					case "pong":
	    						break;
	    						
	    					case "svShutdown":
	    						sendToServer.writeUTF("confirmShutdown");
	    						sendToServer.flush();
	    						terminateConnection();
	    						disposeLinks();
	    						parent.showAuthScreen();
	    						parent.displayLostConnection("Server shutdown");
	    						break;
	    				}
	    			}
    			}
    			catch (Exception e) {}
    		}
    	};
    	
    	this.vitalCheck = new Runnable()
    	{
    		public void run()
    		{
    			try { if (isValidAssetsReady()) { sendToServer.writeUTF("ping"); sendToServer.flush(); }}
    			catch (Exception e) { e.printStackTrace(); }
    		}
    	};
    	
    	this.cpu.scheduleWithFixedDelay(listener, 0, 30, TimeUnit.MILLISECONDS);
    	this.cpu.scheduleWithFixedDelay(vitalCheck, 0, 15, TimeUnit.SECONDS);
    }
    
    private boolean isValidAssetsReady()
    { return Objects.nonNull(cSock) && Objects.nonNull(sendToServer) && Objects.nonNull(getFromServer); }
    
    class NetworkClientThreadNamer implements ThreadFactory
	{ 
    	@Override 
    	public Thread newThread(Runnable r) { return new Thread(r, "Server Listener"); } 
    }
    
    public boolean connect()
    {
    	boolean success = false;
    	try
    	{
    		this.cSock = new Socket(this.ip, PORT);
    		this.sendToServer = new DataOutputStream(this.cSock.getOutputStream());
    		this.getFromServer = new DataInputStream(this.cSock.getInputStream());
    		success = true;
    	}
    	catch (Exception e) { e.printStackTrace(); }
    	
    	return success;
    }
    
    public boolean attemptAuth()
    {
    	boolean success = false;
    	try
    	{
    		this.sendToServer.writeUTF("requestAuth");
    		this.sendToServer.writeUTF(this.seckey);
    		this.sendToServer.flush();
    		
    		String returnMsg = this.getFromServer.readUTF();
    		if (returnMsg.equals("accepted")) success = true;
    	}
    	catch (Exception e ) { e.printStackTrace(); }
    	
    	return success;
    }
    
    public boolean requestNameValidation()
    {
    	boolean success = false;
    	try
    	{
    		this.sendToServer.writeUTF("requestValidateName");
    		this.sendToServer.writeUTF(this.clusr.getName());
    		this.sendToServer.flush();
    		
    		String returnMsg = this.getFromServer.readUTF();
    		if (returnMsg.equals("accepted")) success = true;
    	}
    	catch (Exception e ) { e.printStackTrace(); }
    	
    	return success;
    }
    
    public void postMessage(String content)
    {
    	try
    	{
    		this.playRecv = false;
    		this.sendToServer.writeUTF("requestPostMSG");
    		this.sendToServer.writeUTF(content);
    		this.sendToServer.flush();
    		this.parent.getAudioController().play("send");
    	}
    	catch (Exception e) { e.printStackTrace(); }
    }
    
    public void receivePrevTexts()
    {
    	Runnable recv = new Runnable()
    	{
    		public void run()
    		{
    			boolean done = false;
    			try
    			{
    				sendToServer.writeUTF("ready");
    				sendToServer.flush();
    				
    				while (!done)
    				{
    					String msgType = getFromServer.readUTF();
    					
    					switch (msgType)
    					{
    						case "newOneHere":
    							String message = getFromServer.readUTF();
    							parent.getKhasengerPanel().appendTextToPane(message);
    							break;
    							
    						case "opCompleted":
    							done = true;
    							break;
    					}
    					
    					if (!done)
    					{
	    					sendToServer.writeUTF("ready");
	    					sendToServer.flush();
    					}
    				}
    			}
    			catch (Exception e) { e.printStackTrace(); }
    		}
    	};
    	
    	this.cpu.execute(recv);
    }
    
    public void leaveServer()
    {
    	try
    	{
    		this.sendToServer.writeUTF("userLeave");
    		this.sendToServer.writeUTF(this.clusr.getName());
    		this.sendToServer.flush();
    		
    		terminateConnection();
    		disposeLinks();
    	}
    	
    	catch (Exception e) { e.printStackTrace(); }
    }
    
    public void leaveServerUnexpectedly()
    {
    	try
    	{
    		this.sendToServer.writeUTF("userLeaveUnexpected");
    		this.sendToServer.writeUTF(this.clusr.getName());
    		this.sendToServer.flush();
    		
    		terminateConnection();
    		disposeLinks();
    	}
    	
    	catch (Exception e) { e.printStackTrace(); }
    }
    
    public void terminateConnection()
    {
    	try
    	{
    		this.cSock.close();
    		this.sendToServer.close();
    		this.getFromServer.close();
    	}
    	catch (Exception e) { e.printStackTrace(); }
    }
    
    public void disposeLinks()
    {
    	this.cSock = null;
    	this.sendToServer = null;
    	this.getFromServer = null;
    }
    
    public void resetInputs(String ip, String seckey, String name)
    { 
    	this.ip = ip; 
    	this.seckey = seckey; 
    	this.clusr.setName(name);
    }
    
    public NetworkClient getNetClient() { return this; }
    public NetworkClient iniNetClient(String ip, String seckey, String name)
    {
    	resetInputs(ip, seckey, name);
    	return this;
    }
}