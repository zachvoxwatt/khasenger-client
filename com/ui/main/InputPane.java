package ui.main;

import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import ui.main.functions.IPaneCopyKeyStroke;
import ui.main.functions.IPaneDocumentValidator;
import ui.main.functions.IPaneLineBreakerKeyStroke;
import ui.main.functions.IPanePasteKeyStroke;
import ui.main.functions.IPaneSendKeyStroke;

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
		getDocument().addDocumentListener(new IPaneDocumentValidator(this));
		
		this.getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"), "breakLine");
		this.getActionMap().put("breakLine", new IPaneLineBreakerKeyStroke(this));
		
		this.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "sendMessage");
		this.getActionMap().put("sendMessage", new IPaneSendKeyStroke(this));
		
		this.getInputMap().put(KeyStroke.getKeyStroke("ctrl C"), "copyText");
		this.getActionMap().put("copyText", new IPaneCopyKeyStroke(this));
		
		this.getInputMap().put(KeyStroke.getKeyStroke("ctrl V"), "pasteText");
		this.getActionMap().put("pasteText", new IPanePasteKeyStroke(this));
	}
	
	public KhasengerPanel getKhasengerPanel() { return this.parent; }
}