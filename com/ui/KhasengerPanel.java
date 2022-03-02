package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import audio.AudioController;
import net.NetworkClient;

public class KhasengerPanel extends JPanel 
{
	private static final long serialVersionUID = 2384367241632160071L;
	private int width = 1280, height = 720;
	
	private AudioController aud;
	private ScheduledExecutorService timers;
	private ProgUI parent;
	private InputPane iPane;
	private ConversationPane convoPane;
	private JScrollPane convoScroller, inputScroller;
	private Font chatFont;
	private JButton send, disconnect;
	private NetworkClient cl;
	
	public KhasengerPanel(ProgUI pui, NetworkClient ncl, AudioController au)
	{
		super();
		this.aud = au;
		this.timers = Executors.newSingleThreadScheduledExecutor(new TimerNamer());
		this.parent = pui;
		this.cl = ncl;
			setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
			setPreferredSize(new Dimension(this.width, this.height));
			setBackground(new Color(28, 31, 34, 240));
		
		int scx = 0, scy = 0;
		
		this.chatFont = new Font("Arial", Font.PLAIN, 18);
		this.convoPane = new ConversationPane(this);
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
		this.inputScroller = new JScrollPane(this.iPane);
			scx = (int) ((int) this.getPreferredSize().getWidth() * 66.1 / 100);
			scy = (int) ((int) this.getPreferredSize().getHeight() * 1.925 / 10);
			this.inputScroller.setPreferredSize(new Dimension(scx, scy));
			this.inputScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			add(this.inputScroller);
			
		int buttonWidth = this.width - 5 * 4 - scx;
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
		String username = this.cl.getUsername();
		String sentText = String.format("<%s> %s\n\n", username, this.getInputPane().getText());
		
		this.aud.play("send");
		this.cl.postMessage(username, sentText);
		this.iPane.setText("");
	}
	
	public void playSound(String key) { this.aud.play(key); }
	
	public ProgUI getMainProgUI() { return this.parent; }
	public JScrollPane getConvoScroller() { return this.convoScroller; }
	public JButton getSendButton() { return this.send; }
	public ConversationPane getConvoPane() { return this.convoPane; }
	public InputPane getInputPane() { return this.iPane; }
	public Font getChatFont() { return this.chatFont; }
	public NetworkClient getNetClient() { return this.cl; }
	
	class TimerNamer implements ThreadFactory
	{
		@Override
		public Thread newThread(Runnable r) { return new Thread(r, "Khasenger Panel Timer"); }
	}
	
	class PostMessageFunction implements ActionListener
	{
		private KhasengerPanel kPanel;
		
		public PostMessageFunction(KhasengerPanel kp) { this.kPanel = kp; }
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{ 
			if (this.kPanel.getInputPane().getText().length() != 0) 
				this.kPanel.sendMessage();
		}
	}
	
	class DisconnectFunction implements ActionListener
	{
		private KhasengerPanel kPanel;
		
		public DisconnectFunction(KhasengerPanel kp) { this.kPanel = kp; }
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			this.kPanel.getNetClient().notifyLeave();
			this.kPanel.getMainProgUI().showAuthScreen();
		}
	}
	
	class ViewChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e) { convoScroller.revalidate(); convoScroller.repaint(); }
	}
}

class ConversationPane extends JTextArea
{
	private static final long serialVersionUID = 4653119383864500640L;
	
	private KhasengerPanel parent;
	
	public ConversationPane(KhasengerPanel ppnl)
	{
		super();
			this.parent = ppnl;
			
			setText("[Khasenger v1.0 by Zaineyy]\n\n");
			setFont(this.parent.getChatFont());
			setWrapStyleWord(true);
			setLineWrap(true);
			setEditable(false);
			setForeground(Color.WHITE);
			setBackground(new Color(44, 49, 53, 175));
			getDocument().addDocumentListener(new ConversationRefresher(this));
			
		JButton blocker = new JButton();
			blocker.setPreferredSize(this.getPreferredSize());
			blocker.setEnabled(false);
			blocker.setVisible(false);
			add(blocker);
	}
	
	public KhasengerPanel getKhasengerPanel() { return this.parent; }
	
	class ConversationRefresher implements DocumentListener
	{
		private ConversationPane cpane;
		public ConversationRefresher(ConversationPane cpane) { this.cpane = cpane; }
		@Override
		public void insertUpdate(DocumentEvent e) { this.cpane.getKhasengerPanel().getConvoScroller().repaint(); }
		@Override
		public void removeUpdate(DocumentEvent e) {}
		@Override
		public void changedUpdate(DocumentEvent e) {}
	}
}

class InputPane extends JTextArea
{
	private static final long serialVersionUID = -4506242901739223689L;
	
	private KhasengerPanel parent;
	
	public InputPane(KhasengerPanel ppnl)
	{
		this.parent = ppnl;
		
		setFont(this.parent.getChatFont());
		setWrapStyleWord(true);
		setLineWrap(true);
		getDocument().addDocumentListener(new DocumentValidator(this));
		
		this.getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"), "breakLine");
		this.getActionMap().put("breakLine", new LineBreaker(this));
		
		this.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "sendMessage");
		this.getActionMap().put("sendMessage", new Sender(this));
	}
	
	public KhasengerPanel getKhasengerPanel() { return this.parent; }
	
	class Sender extends AbstractAction
	{
		private static final long serialVersionUID = 8943916018344357185L;
		
		private InputPane ipane;
		
		public Sender(InputPane ip) { this.ipane = ip; }
		@Override
		public void actionPerformed(ActionEvent e) 
		{ 
			if (this.ipane.getText().isBlank()) return;
			else this.ipane.getKhasengerPanel().sendMessage();
		}
	}
	
	class LineBreaker extends AbstractAction
	{
		private static final long serialVersionUID = 8943916018344357185L;
		
		private InputPane ipane;
		
		public LineBreaker(InputPane ip) { this.ipane = ip; }
		@Override
		public void actionPerformed(ActionEvent e) { this.ipane.getKhasengerPanel().breakLine(); }
	}
	
	class DocumentValidator implements DocumentListener
	{
		private InputPane ipane;
		
		public DocumentValidator(InputPane ip) { this.ipane = ip; }
		@Override
		public void insertUpdate(DocumentEvent e) { update(); }
		@Override
		public void removeUpdate(DocumentEvent e) { update(); }
		@Override
		public void changedUpdate(DocumentEvent e) {}
		
		void update()
		{
			String text = this.ipane.getText();
			
			if (text.length() == 0)
			{
				this.ipane.getKhasengerPanel().getSendButton().setEnabled(false);
				this.ipane.getKhasengerPanel().getSendButton().setForeground(Color.WHITE);
				this.ipane.getKhasengerPanel().getSendButton().setBackground(Color.DARK_GRAY);
			}
			else
			{
				this.ipane.getKhasengerPanel().getSendButton().setEnabled(true);
				this.ipane.getKhasengerPanel().getSendButton().setForeground(Color.BLACK);
				this.ipane.getKhasengerPanel().getSendButton().setBackground(Color.GREEN);
			}
			
		}
	}
}