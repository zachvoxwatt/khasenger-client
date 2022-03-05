package ui.main.functions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ui.main.KhasengerPanel;

public class DisconnectFunction implements ActionListener
{
	private KhasengerPanel kPanel;
	
	public DisconnectFunction(KhasengerPanel kp) { this.kPanel = kp; }
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.kPanel.getNetClient().leaveServer();
		this.kPanel.getMainProgUI().showAuthScreen();
	}
}