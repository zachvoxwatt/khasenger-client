package ui.context_menu;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ui.main.KhasengerPanel;

public class ConversationPaneContextMenu extends JPopupMenu 
{
	private static final long serialVersionUID = -5959953317761021204L;

	private KhasengerPanel kpanel;
	private JMenuItem copy;
	
	public ConversationPaneContextMenu(KhasengerPanel k)
	{
		super();
			this.kpanel = k;
			this.copy = new JMenuItem("Copy Selected Text");
			
			this.copy.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						StringSelection target = new StringSelection(kpanel.getConvoPane().getSelectedText());
						Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
						clpbrd.setContents(target, null);
					}
				}
			);
			
			this.add(copy);
	}
	
	public KhasengerPanel getKhasengerPanel() { return this.kpanel; }
}