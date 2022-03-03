package ui.context_menu;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ui.main.KhasengerPanel;

public class ContextMenuItems
{
	private JMenuItem copy, paste;
	private ContextMenu cmu;
	
	public ContextMenuItems(ContextMenu jpmf)
	{
		this.cmu = jpmf;
		
		copy = new JMenuItem("Copy Selected Text");
		paste = new JMenuItem("Paste Copied Text");
		
		copy.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					KhasengerPanel tempp = cmu.getKhasengerPanel();
					StringSelection copiedText = null;
					if (tempp.getConvoPaneMD().isMouseInside())
					{
						System.out.println("Im in Convo!");
						copiedText = new StringSelection(tempp.getConvoPane().getSelectedText());
					}
					else if (tempp.getInputPaneMD().isMouseInside())
					{
						System.out.println("Im in Input!");
						copiedText = new StringSelection(tempp.getInputPane().getSelectedText());
					}
					
					else return;
					Clipboard clipbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipbrd.setContents(copiedText, null);
				}
			}
		);
		
		paste.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					KhasengerPanel tempp = cmu.getKhasengerPanel();
					Clipboard clipbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
					String pastedText = "";
					
					try
					{
						if (tempp.getInputPaneMD().isMouseInside())
						{
							System.out.println("Pasted Input!");
							pastedText = (String) clipbrd.getData(DataFlavor.stringFlavor);
							tempp.getInputPane().insert(pastedText, tempp.getInputPane().getCaretPosition());
						}
						
						else return;
					}
					catch (Exception exc) {}
				}
			}
		);
	}
	
	public void addItems()
	{
		this.cmu.add(copy);
		this.cmu.add(paste);
	}
}