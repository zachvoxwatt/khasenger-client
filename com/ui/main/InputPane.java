package ui.main;

import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import ui.main.functions.InputPaneDocumentValidator;
import ui.main.functions.LineBreakerKeyStroke;
import ui.main.functions.MessageSenderKeystroke;

public class InputPane extends JTextArea
{
	private static final long serialVersionUID = -4506242901739223689L;
	
	private KhasengerPanel parent;
	
	public InputPane(KhasengerPanel ppnl)
	{
		this.parent = ppnl;
		
		setFont(this.parent.getChatFont());
		setWrapStyleWord(true);
		setLineWrap(true);
		getDocument().addDocumentListener(new InputPaneDocumentValidator(this));
		
		this.getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"), "breakLine");
		this.getActionMap().put("breakLine", new LineBreakerKeyStroke(this));
		
		this.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "sendMessage");
		this.getActionMap().put("sendMessage", new MessageSenderKeystroke(this));
	}
	
	public KhasengerPanel getKhasengerPanel() { return this.parent; }
}