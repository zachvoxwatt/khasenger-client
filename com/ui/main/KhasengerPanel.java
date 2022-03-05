package ui.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import audio.AudioController;
import net.ClientUser;
import net.NetworkClient;
import ui.ProgUI;
import ui.context_menu.ConversationPaneContextMenu;
import ui.context_menu.InputPaneContextMenu;
import ui.main.functions.DisconnectFunction;
import ui.main.functions.PostMessageFunction;

public class KhasengerPanel extends JPanel 
{
	private static final long serialVersionUID = 2384367241632160071L;
	private int width = 1280, height = 720;
	
	private Font chatFont;
	private ProgUI parent;
	private String copiedText = "";
	private JButton send, disconnect;
	private InputPane iPane;
	private ClientUser clusr;
	private JScrollPane convoScroller, inputScroller;
	private NetworkClient cl;
	private ConversationPane convoPane;
	private ScheduledExecutorService timers;
	private InputPaneContextMenu ipCM;
	private ConversationPaneContextMenu cpCM;
	
	public KhasengerPanel(ProgUI pui, NetworkClient ncl, AudioController au)
	{
		super();
		this.parent = pui;
		this.clusr = this.parent.getClientUser();
		this.timers = Executors.newSingleThreadScheduledExecutor(new TimerNamer());
		this.cl = ncl;
			setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
			setPreferredSize(new Dimension(this.width, this.height));
			setBackground(new Color(28, 31, 34, 240));
		
		int scx = 0, scy = 0;
		
		this.chatFont = new Font("Arial", Font.PLAIN, 18);
		this.cpCM = new ConversationPaneContextMenu(this);
		
		this.convoPane = new ConversationPane(this);
			convoPane.setComponentPopupMenu(this.cpCM);
			
		this.convoScroller = new JScrollPane(this.convoPane);
			scx = (int) ((int) this.getPreferredSize().getWidth() * 99.3 / 100);
			scy = (int) ((int) this.getPreferredSize().getHeight() * 76 / 100);
			this.convoScroller.setPreferredSize(new Dimension(scx, scy));
			this.convoScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			this.convoScroller.getViewport().addChangeListener(new ViewChangeListener());
			add(this.convoScroller);
	
		Runnable convoScrollerRefresher = new Runnable() 
			{ public void run() { convoScroller.revalidate(); convoScroller.repaint(); } };
		timers.scheduleWithFixedDelay(convoScrollerRefresher, 0, 5, TimeUnit.MILLISECONDS);
		
		JPanel separator = new JPanel();
			scx = (int) ((int) this.getPreferredSize().getWidth() * 99.3 / 100);
			scy = (int) ((int) this.getPreferredSize().getHeight() * 0.25 / 10);
			separator.setPreferredSize(new Dimension(scx, scy));
			separator.setBackground(Color.GRAY);
			add(separator);
			
		this.iPane = new InputPane(this);
		this.ipCM = new InputPaneContextMenu(this);
			iPane.setComponentPopupMenu(this.ipCM);
			
		this.inputScroller = new JScrollPane(this.iPane);
			scx = (int) ((int) this.getPreferredSize().getWidth() * 66.1 / 100);
			scy = (int) ((int) this.getPreferredSize().getHeight() * 1.925 / 10);
			this.inputScroller.setPreferredSize(new Dimension(scx, scy));
			this.inputScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			add(this.inputScroller);
			
		int buttonWidth = this.width - 5 * 4 - scx + 2;
		this.send = new JButton("Send");
			send.setFont(new Font("Consolas", Font.BOLD, 36));
			send.setPreferredSize(new Dimension(buttonWidth / 2, scy));
			send.setBackground(Color.DARK_GRAY);
			send.setForeground(Color.WHITE);
			send.setRolloverEnabled(false);
			send.setFocusPainted(false);
			send.setEnabled(false);
			send.addActionListener(new PostMessageFunction(this));
			add(this.send);
			
		this.disconnect = new JButton("Leave");
			disconnect.setFont(new Font("Consolas", Font.BOLD, 36));
			disconnect.setPreferredSize(new Dimension(buttonWidth / 2, scy));
			disconnect.setBackground(Color.RED);
			disconnect.setRolloverEnabled(false);
			disconnect.setFocusPainted(false);
			disconnect.addActionListener(new DisconnectFunction(this));
			add(this.disconnect);
	}
	
	public void breakLine() 
	{
		this.iPane.replaceSelection("");
		this.iPane.insert("\n", this.iPane.getCaretPosition());
	}
	
	public void appendTextToPane(String text) 
	{ 
		this.convoPane.append(text); 
		this.convoPane.setCaretPosition(this.convoPane.getText().length());
		this.convoScroller.revalidate(); 
		this.convoScroller.repaint();
	}
	
	public void sendMessage()
	{
		String username = this.clusr.getName();
		String sentText = String.format("<%s> %s\n\n", username, this.getInputPane().getText());
		
		this.cl.postMessage(sentText);
		this.iPane.setText("");
	}

	public void setCopiedText(String text) { this.copiedText = text; }
	
	public String getCopiedText() { return this.copiedText; }
	public NetworkClient getNetClient() { return this.cl; }
	public Font getChatFont() { return this.chatFont; }
	public ProgUI getMainProgUI() { return this.parent; }
	public JButton getSendButton() { return this.send; }
	public JScrollPane getConvoScroller() { return this.convoScroller; }
	public InputPane getInputPane() { return this.iPane; }
	public ConversationPane getConvoPane() { return this.convoPane; }
	
	class TimerNamer implements ThreadFactory
	{
		@Override
		public Thread newThread(Runnable r) 
			{ return new Thread(r, "Khasenger Panel Discrete Processor"); }
	}
	
	class ViewChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e) 
			{ convoScroller.revalidate(); convoScroller.repaint(); }
	}
}