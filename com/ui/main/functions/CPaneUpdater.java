package ui.main.functions;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ui.main.ConversationPane;

public class CPaneUpdater implements DocumentListener
{
	private ConversationPane cpane;
	public CPaneUpdater(ConversationPane cpane) { this.cpane = cpane; }
	@Override
	public void insertUpdate(DocumentEvent e) { this.cpane.getKhasengerPanel().getConvoScroller().repaint(); }
	@Override
	public void removeUpdate(DocumentEvent e) {}
	@Override
	public void changedUpdate(DocumentEvent e) {}
}