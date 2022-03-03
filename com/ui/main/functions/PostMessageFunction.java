package ui.main.functions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ui.main.KhasengerPanel;

public class PostMessageFunction implements ActionListener
{
	private KhasengerPanel kPanel;
	
	public PostMessageFunction(KhasengerPanel kp) { this.kPanel = kp; }
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{ 
		if (this.kPanel.getInputPane().getText().length() != 0) 
			this.kPanel.sendMessage();
	}
}