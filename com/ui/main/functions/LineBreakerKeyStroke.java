package ui.main.functions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ui.main.InputPane;

public class LineBreakerKeyStroke extends AbstractAction
{
	private static final long serialVersionUID = 8943916018344357185L;
	
	private InputPane ipane;
	
	public LineBreakerKeyStroke(InputPane ip) { this.ipane = ip; }
	@Override
	public void actionPerformed(ActionEvent e) { this.ipane.getKhasengerPanel().breakLine(); }
}