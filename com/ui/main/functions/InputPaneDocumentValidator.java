package ui.main.functions;

import java.awt.Color;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ui.main.InputPane;

public class InputPaneDocumentValidator implements DocumentListener
{
	private InputPane ipane;
	
	public InputPaneDocumentValidator(InputPane ip) { this.ipane = ip; }
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