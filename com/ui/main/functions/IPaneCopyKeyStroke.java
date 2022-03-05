package ui.main.functions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;

import ui.main.InputPane;

public class IPaneCopyKeyStroke extends AbstractAction
{
	private static final long serialVersionUID = 8943916018344357185L;
	
	private InputPane ipane;
	
	public IPaneCopyKeyStroke(InputPane ip) { this.ipane = ip; }
	@Override
	public void actionPerformed(ActionEvent e) 
	{ 
		String selectedText = this.ipane.getSelectedText();
		
		if (Objects.isNull(selectedText)) return;
		
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(new StringSelection(selectedText), null);
	}
}