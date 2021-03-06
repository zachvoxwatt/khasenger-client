package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import audio.AudioController;
import audio.JSONDataParser;
import net.ClientUser;
import net.NetworkClient;
import ui.auth.AuthScreen;
import ui.main.KhasengerPanel;

public class ProgUI extends JFrame
{
	private boolean appActive = false;
	private static final long serialVersionUID = -577119706971836732L;

	private ClientUser clusr;
	private AuthScreen authS;
	private NetworkClient cl;
	private KhasengerPanel kPanel;
	private AudioController aud;
	
	public ProgUI()
	{
		super();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Khasenger");
			setLayout(new BorderLayout());
		
		this.kPanel = null;
		this.clusr = new ClientUser();
		this.aud = new AudioController(new JSONDataParser().getDirectoryMap());	
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
		
		this.authS.restart();
		remove(this.kPanel);
		add(this.authS);
		this.kPanel.idle();
		
		pack();
		setLocationRelativeTo(null);
		revalidate();
		repaint();
	}
	
	public void showConversationPane()
	{
		this.appActive = true;
		
		if (Objects.isNull(this.kPanel)) 
			this.kPanel = new KhasengerPanel(this);
		else 
			this.kPanel.restart();
			
		remove(this.authS);
		add(this.kPanel);
		this.authS.idle();
		
		pack();
		setLocationRelativeTo(null);
		revalidate();
		repaint();
	}
	
	public void displayLostConnection(String reason)
	{
		String info = String.format("Lost connection to server\nReason: %s", reason);
		JOptionPane.showMessageDialog(this, info, "Uh-Oh! Something happened...", JOptionPane.WARNING_MESSAGE);
	}
	
	public boolean isAppActive() { return this.appActive; }
	
	public AuthScreen getAuthScreen() { return this.authS; }
	public ClientUser getClientUser() { return this.clusr; }
	public AudioController getAudioController() { return this.aud; }
	public KhasengerPanel getKhasengerPanel() { return this.kPanel; }
	public NetworkClient iniNetClient(String a, String b, String c) 
	{ 
		this.cl = null; System.gc(); 
		this.cl = new NetworkClient(a, b, c, this); 
		return this.cl;
	} 
	public NetworkClient getNetClient() { return this.cl; }
	
	public static void main(String[] args) 
	{
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
		catch (Exception e) { e.printStackTrace(); }
		EventQueue.invokeLater(ProgUI::new); 
	}
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
		if (parent.isAppActive()) this.parent.getNetClient().leaveServerUnexpectedly();
		this.parent.dispose();
		System.exit(0);
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