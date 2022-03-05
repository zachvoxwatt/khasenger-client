package ui.main.functions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import ui.main.InputPane;

public class IPanePasteKeyStroke extends AbstractAction
{
	private static final long serialVersionUID = 8943916018344357185L;
	
	private InputPane ipane;
	
	public IPanePasteKeyStroke(InputPane ip) { this.ipane = ip; }
	@Override
	public void actionPerformed(ActionEvent e) 
	{ 
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		String pastedText = "";
		
		try { pastedText = (String) clpbrd.getData(DataFlavor.stringFlavor); }
		catch (Exception exc)
		{
			String message = "Image type pasting is not supported yet!";
			JOptionPane.showMessageDialog(ipane.getKhasengerPanel(), message, "Unsupported Operation", JOptionPane.INFORMATION_MESSAGE);
		}
		
		ipane.replaceSelection("");
		ipane.insert(pastedText, ipane.getCaretPosition());
	}
}