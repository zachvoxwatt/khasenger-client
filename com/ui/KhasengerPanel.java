package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class KhasengerPanel extends JPanel 
{
	private static final long serialVersionUID = 2384367241632160071L;
	
	private InputPane iPane;
	private ConversationPane convoPane;
	private JScrollPane convoScroller, inputScroller;
	private Font chatFont;
	private JButton send, disconnect;
	
	public KhasengerPanel()
	{
		super();
			setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
			setPreferredSize(new Dimension(1280, 720));
			setBackground(new Color(28, 31, 34, 240));
		
		int scx = 0, scy = 0;
		
		this.chatFont = new Font("Arial", Font.PLAIN, 18);
		this.convoPane = new ConversationPane(this);
		this.convoScroller = new JScrollPane(this.convoPane);
			scx = (int) this.getPreferredSize().getWidth();
			scy = (int) ((int) this.getPreferredSize().getHeight() * 76 / 100);
			this.convoScroller.setPreferredSize(new Dimension(scx, scy));
			this.convoScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			add(this.convoScroller);
	
		JPanel separator = new JPanel();
			scx = (int) this.getPreferredSize().getWidth();
			scy = (int) ((int) this.getPreferredSize().getHeight() * 0.25 / 10);
			separator.setPreferredSize(new Dimension(scx, scy));
			separator.setBackground(Color.GRAY);
			add(separator);
			
		this.iPane = new InputPane(this);
		this.inputScroller = new JScrollPane(this.iPane);
			scx = (int) ((int) this.getPreferredSize().getWidth() * 66.1 / 100);
			scy = (int) ((int) this.getPreferredSize().getHeight() * 2.05 / 10);
			this.inputScroller.setPreferredSize(new Dimension(scx, scy));
			this.inputScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			add(this.inputScroller);
			
		this.send = new JButton("Send");
			send.setFont(new Font("Consolas", Font.BOLD, 36));
			send.setPreferredSize(new Dimension(212, 147));
			send.setBackground(Color.GREEN);
			send.setRolloverEnabled(false);
			send.setFocusPainted(false);
			add(this.send);
			
		this.disconnect = new JButton("Leave");
			disconnect.setFont(new Font("Consolas", Font.BOLD, 36));
			disconnect.setPreferredSize(new Dimension(212, 147));
			disconnect.setBackground(Color.RED);
			disconnect.setRolloverEnabled(false);
			disconnect.setFocusPainted(false);
			add(this.disconnect);
	}
	
	public Font getChatFont() { return this.chatFont; }
}

class ConversationPane extends JTextArea
{
	private static final long serialVersionUID = 4653119383864500640L;
	
	private KhasengerPanel parent;
	
	public ConversationPane(KhasengerPanel ppnl)
	{
		super();
			this.parent = ppnl;
			
			setFont(this.parent.getChatFont());
			setWrapStyleWord(true);
			setLineWrap(true);
			setEditable(false);
			setForeground(Color.WHITE);
			setBackground(new Color(44, 49, 53, 200));
			
		JButton blocker = new JButton();
			blocker.setPreferredSize(this.getPreferredSize());
			blocker.setEnabled(false);
			blocker.setVisible(false);
			add(blocker);
	}
	
	public void paintComponent(Graphics g) { super.paintComponent(g); }
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
	}
}