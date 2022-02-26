package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ProgPanel extends JPanel 
{
	private static final long serialVersionUID = 2384367241632160071L;
	
	private InputPane iPane;
	private ConversationPane convoPane;
	private JScrollPane convoScroller, inputScroller;
	private Font chatFont;
	private JButton send, disconnect;
	
	public ProgPanel()
	{
		super();
			setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
			setPreferredSize(new Dimension(1280, 720));
			setBackground(new Color(28, 31, 34, 240));
		
		this.chatFont = new Font("Arial", Font.PLAIN, 18);
		this.convoPane = new ConversationPane(this);
		this.convoScroller = new JScrollPane(this.convoPane);
			this.convoScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			add(this.convoScroller);
	
		JPanel separator = new JPanel();
			separator.setPreferredSize(new Dimension(1270, 10));
			separator.setBackground(Color.GRAY);
			add(separator);
			
		this.iPane = new InputPane(this);
		this.inputScroller = new JScrollPane(this.iPane);
			this.inputScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			add(this.inputScroller);
			
		this.send = new JButton("Send");
			send.setFont(new Font("Consolas", Font.BOLD, 36));
			send.setPreferredSize(new Dimension(212, 147));
			send.setBackground(Color.GREEN);
			send.setRolloverEnabled(false);
			add(this.send);
			
		this.disconnect = new JButton("Leave");
			disconnect.setFont(new Font("Consolas", Font.BOLD, 36));
			disconnect.setPreferredSize(new Dimension(212, 147));
			disconnect.setBackground(Color.RED);
			disconnect.setRolloverEnabled(false);
			add(this.disconnect);
	}
	
	public Font getChatFont() { return this.chatFont; }
}

class ConversationPane extends JTextArea
{
	private static final long serialVersionUID = 4653119383864500640L;
	private int width, height;
	
	private ProgPanel parent;
	private Font chatFont;
	
	public ConversationPane(ProgPanel ppnl)
	{
		super();
			this.parent = ppnl;
			this.width = (int) this.parent.getPreferredSize().getWidth() * 99 / 100;
			this.height = (int) this.parent.getPreferredSize().getHeight() * 75 / 100;
			
			setFont(this.parent.getChatFont());
			setWrapStyleWord(true);
			setLineWrap(true);
			setEditable(false);
			setFont(this.chatFont);
			setForeground(Color.WHITE);
			setPreferredSize(new Dimension(this.width, this.height));
			setBackground(new Color(44, 49, 53, 200));
			
		JButton blocker = new JButton();
			blocker.setPreferredSize(this.getPreferredSize());
			blocker.setEnabled(false);
			blocker.setVisible(false);
			add(blocker);
	}
}

class InputPane extends JTextArea
{
	private static final long serialVersionUID = -4506242901739223689L;
	private int width, height;
	
	private ProgPanel parent;
	
	public InputPane(ProgPanel ppnl)
	{
		this.parent = ppnl;
		this.width = (int) this.parent.getPreferredSize().getWidth() * 65 / 100;
		this.height = (int) this.parent.getPreferredSize().getHeight() * 20 / 100;
		
		setFont(this.parent.getChatFont());
		setWrapStyleWord(true);
		setLineWrap(true);
		setPreferredSize(new Dimension(this.width, this.height));
	}
}