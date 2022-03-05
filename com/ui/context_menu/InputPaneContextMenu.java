package ui.context_menu;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import ui.main.KhasengerPanel;

public class InputPaneContextMenu extends JPopupMenu 
{
	private static final long serialVersionUID = -5959953317761021204L;

	private KhasengerPanel kpanel;
	private JMenuItem copy, paste;
	
	public InputPaneContextMenu(KhasengerPanel k)
	{
		super();
			this.kpanel = k;
			this.copy = new JMenuItem("Copy Selected Text");
			this.paste = new JMenuItem("Paste Copied Text");
			
			this.copy.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						StringSelection target = new StringSelection(kpanel.getInputPane().getSelectedText());
						Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
						clpbrd.setContents(target, null);
					}
				}
			);
			
			this.paste.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
						String takenContent = "";
						
						try { takenContent = (String) clpbrd.getData(DataFlavor.stringFlavor); }
						catch (Exception exc)
						{
							String message = "Image type pasting is not supported yet!";
							JOptionPane.showMessageDialog(kpanel, message, "Unsupported Operation", JOptionPane.INFORMATION_MESSAGE);
						}
						
						kpanel.getInputPane().replaceSelection("");
						kpanel.getInputPane().insert(takenContent, kpanel.getInputPane().getCaretPosition());
					}
				}
			);
			
			this.add(copy);
			this.add(paste);
	}
	
	public KhasengerPanel getKhasengerPanel() { return this.kpanel; }
}