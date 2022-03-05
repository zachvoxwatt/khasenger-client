package ui.main;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JTextArea;

import ui.main.functions.CPaneUpdater;

public class ConversationPane extends JTextArea
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
			getDocument().addDocumentListener(new CPaneUpdater(this));
			
		JButton blocker = new JButton();
			blocker.setPreferredSize(this.getPreferredSize());
			blocker.setEnabled(false);
			blocker.setVisible(false);
			add(blocker);
	}
	
	public KhasengerPanel getKhasengerPanel() { return this.parent; }
}