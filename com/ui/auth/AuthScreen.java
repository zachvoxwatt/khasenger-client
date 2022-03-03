package ui.auth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.ProgUI;

public class AuthScreen extends JPanel
{
	private static final long serialVersionUID = 3987958248635912669L;
	private int wx = 500, wy = 500;
	
	private ProgUI parent;
	private JPanel margin1, margin2;
	private AuthForm form;
	private JLabel connectionStatus;
	private ScheduledExecutorService dthrds;
	
	public AuthScreen(ProgUI pui)
	{
		super();
			this.dthrds = Executors.newSingleThreadScheduledExecutor(new DiscreteThreadsNamer());
			this.parent = pui;
			setPreferredSize(new Dimension(this.wx, this.wy));
			setLayout(new BorderLayout());
			setBackground(new Color(255, 179, 71, 255));
		
		this.form = new AuthForm(this);
			this.form.setPreferredSize(new Dimension(this.wx, this.wy * (100 - 12 * 2) / 100));
			add(form, BorderLayout.CENTER);
		
		margin1 = new JPanel();
			margin1.setPreferredSize(new Dimension(this.wx, this.wy * 12 / 100));
			margin1.setLayout(new GridBagLayout());
			margin1.setOpaque(false);
			JLabel title = new JLabel("KHASENGER");
			title.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
			margin1.add(title);
			
		margin2 = new JPanel();
			margin2.setOpaque(false);
			margin2.setPreferredSize(new Dimension(this.wx, this.wy * 12 * 2 / 100));
			margin2.setLayout(new GridBagLayout());
			
		this.connectionStatus = new JLabel();
			connectionStatus.setFont(new Font("Consolas", Font.PLAIN, 18));
			connectionStatus.setVisible(false);
			margin2.add(connectionStatus);
			
			add(margin1, BorderLayout.NORTH);
			add(margin2, BorderLayout.SOUTH);

		repaint();
	}
	
	class DiscreteThreadsNamer implements ThreadFactory
	{ 
		private int count = 1;
    	@Override 
    	public Thread newThread(Runnable r) { return new Thread(r, "Discrete Thread #" + ++count); } 
    }
	
	Runnable timeoutMessage = new Runnable() { public void run() {connectionStatus.setVisible(false);}};
	
	public void changeNotice(String message, Color c)
	{
		connectionStatus.setText(message);
		connectionStatus.setForeground(c);
		connectionStatus.setVisible(true);
		connectionStatus.revalidate(); connectionStatus.repaint();
		
		this.dthrds.schedule(timeoutMessage, 2, TimeUnit.SECONDS);	
	}
	
	public ScheduledExecutorService getTimers() { return this.dthrds; }
	public ProgUI getMainProgClass() { return this.parent; }
}