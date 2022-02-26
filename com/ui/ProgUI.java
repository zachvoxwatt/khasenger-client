package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class ProgUI extends JFrame
{
	private static final long serialVersionUID = -577119706971836732L;

	public ProgUI()
	{
		super();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Khasenger");
			setLayout(new BorderLayout());
			
		ProgPanel ppnel = new ProgPanel();
			add(ppnel);
			pack();
			setResizable(false);
			setLocationRelativeTo(null);
			setVisible(true);
	}
	
	public static void main(String[] args) { EventQueue.invokeLater(ProgUI::new); }
}