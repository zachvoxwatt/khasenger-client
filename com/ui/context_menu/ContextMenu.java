package ui.context_menu;

import javax.swing.JPopupMenu;

import ui.main.KhasengerPanel;

public class ContextMenu extends JPopupMenu 
{
	private static final long serialVersionUID = -5959953317761021204L;

	private ContextMenuItems items;
	private KhasengerPanel kpanel;
	
	public ContextMenu(KhasengerPanel k)
	{
		super();
			this.kpanel = k;
			this.items = new ContextMenuItems(this);
			this.items.addItems();
	}
	
	public KhasengerPanel getKhasengerPanel() { return this.kpanel; }
}