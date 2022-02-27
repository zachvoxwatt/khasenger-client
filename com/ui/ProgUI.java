package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import net.NetworkClient;

public class ProgUI extends JFrame
{
	private boolean appActive = false;
	private static final long serialVersionUID = -577119706971836732L;

	private NetworkClient cl;
	private KhasengerPanel kPanel;
	private AuthScreen authS;
	
	public ProgUI()
	{
		super();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Khasenger");
			setLayout(new BorderLayout());
			
		this.authS = new AuthScreen(this);
			add(this.authS, BorderLayout.CENTER);
			pack();
			setResizable(false);
			setLocationRelativeTo(null);
			setVisible(true);
			addWindowListener(new InteractiveWindowActions(this));
	}
	
	public void showAuthScreen()
	{
		this.appActive = false;
		remove(this.kPanel);
		add(this.authS);
		pack();
		setLocationRelativeTo(null);
		revalidate();
		repaint();
	}
	
	public void showConversationPane()
	{
		this.appActive = true;
		this.kPanel = new KhasengerPanel(this, this.cl);
		remove(this.authS);
		add(this.kPanel);
		pack();
		setLocationRelativeTo(null);
		revalidate();
		repaint();
	}
	
	public boolean isAppActive() { return this.appActive; }
	
	public KhasengerPanel getKhasengerPanel() { return this.kPanel; }
	public NetworkClient iniNetClient(String a, String b, String c) { this.cl = new NetworkClient(a, b, c, this); return this.cl; } 
	public NetworkClient getNetClient() { return this.cl; }
	
	public static void main(String[] args) { EventQueue.invokeLater(ProgUI::new); }
}

class InteractiveWindowActions implements WindowListener
{
	private ProgUI parent;
	
	public InteractiveWindowActions(ProgUI pui)
	{
		this.parent = pui;
	}

	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) 
	{ 
		if (parent.isAppActive()) this.parent.getNetClient().terminateConnection();
		this.parent.dispose();
	}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
}