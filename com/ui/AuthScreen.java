package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.NetworkClient;

public class AuthScreen extends JPanel
{
	private static final long serialVersionUID = 3987958248635912669L;
	private int wx = 500, wy = 500;
	
	private ProgUI parent;
	private JPanel margin1, margin2;
	private AuthForm form;
	private JLabel connectionStatus;
	private ScheduledExecutorService timers;
	
	public AuthScreen(ProgUI pui)
	{
		super();
			this.timers = Executors.newSingleThreadScheduledExecutor(new TimersNamer());
			this.parent = pui;
			setPreferredSize(new Dimension(this.wx, this.wy));
			setLayout(new BorderLayout());
			setBackground(Color.ORANGE);
		
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
			connectionStatus.setFont(new Font("Segoe UI", Font.PLAIN, 22));
			connectionStatus.setVisible(false);
			margin2.add(connectionStatus);
			
			add(margin1, BorderLayout.NORTH);
			add(margin2, BorderLayout.SOUTH);
	}
	
	class TimersNamer implements ThreadFactory
	{ 
    	@Override 
    	public Thread newThread(Runnable r) { return new Thread(r, "Timer Namer Thread"); } 
    }
	
	Runnable timeoutMessage = new Runnable()
	{
		public void run()
		{
			connectionStatus.setVisible(false);
		}
	};
	
	public void changeNotice(String message, Color c)
	{
		connectionStatus.setText(message);
		connectionStatus.setForeground(c);
		connectionStatus.setVisible(true);
		connectionStatus.revalidate(); connectionStatus.repaint();
		
		this.timers.schedule(timeoutMessage, 2, TimeUnit.SECONDS);	
	}
	
	public ScheduledExecutorService getTimers() { return this.timers; }
	public ProgUI getMainProgClass() { return this.parent; }
}

class AuthForm extends JPanel
{
	private static final long serialVersionUID = 5648930300100301604L;
	
	private AuthScreen parent;
	private GridBagConstraints gbc;
	private JTextField inputIP, inputName;
	private JPasswordField inputSecKey;
	private JButton connect;
	
	public AuthForm(AuthScreen as)
	{
		super();
			this.gbc = new GridBagConstraints();
			this.parent = as;
			setLayout(new GridBagLayout());
			setOpaque(false);
			
		JLabel ipLabel = new JLabel("Target IP: ");
			ipLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
			gbc.gridx = 0; gbc.gridy = 1; gbc.insets = generateInsets(7, 0, 7, 0);
			add(ipLabel, gbc);
			
		JLabel seckeyLabel = new JLabel("Room Key: ");
			seckeyLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
			gbc.gridx = 0; gbc.gridy = 2; gbc.insets = generateInsets(7, 0, 7, 0);
			add(seckeyLabel, gbc);
			
		JLabel usernameLabel = new JLabel("Username: ");
			usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
			gbc.gridx = 0; gbc.gridy = 3; gbc.insets = generateInsets(7, 0, 0, 0);
			add(usernameLabel, gbc);
			
		this.inputIP = new JTextField();
			inputIP.setBackground(Color.YELLOW);
			inputIP.setHorizontalAlignment(JTextField.CENTER);
			inputIP.setPreferredSize(new Dimension(250, 35));
			inputIP.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			gbc.gridx = 1; gbc.gridy = 1; gbc.insets = generateInsets(7, 0, 7, 0);
			add(inputIP, gbc);
			
		this.inputSecKey = new JPasswordField();
			inputSecKey.setBackground(Color.YELLOW);
			inputSecKey.setHorizontalAlignment(JPasswordField.CENTER);
			inputSecKey.setPreferredSize(new Dimension(250, 35));
			gbc.gridx = 1; gbc.gridy = 2; gbc.insets = generateInsets(7, 0, 7, 0);
			add(inputSecKey, gbc);
			
		this.inputName = new JTextField();
			inputName.setBackground(Color.YELLOW);
			inputName.setHorizontalAlignment(JTextField.CENTER);
			inputName.setPreferredSize(new Dimension(250, 35));
			inputName.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			gbc.gridx = 1; gbc.gridy = 3; gbc.insets = generateInsets(7, 0, 7, 0);
			add(inputName, gbc);
			
		this.connect = new JButton("Login and Chat!");
			connect.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			connect.setPreferredSize(new Dimension(250, 35));
			connect.setFocusPainted(false);
			connect.addActionListener(new LoginFunction(this, connect));
			gbc.gridx = 1; gbc.gridy = 4; gbc.insets = generateInsets(7, 0, 0, 0);
			add(connect, gbc);
	}
	
	public void disableInputs()
	{
		this.inputIP.setEditable(false);
		this.inputSecKey.setEditable(false);
		this.inputName.setEditable(false);
		this.connect.setEnabled(false);
		revalidate(); repaint();
	}
	
	public void enableInputs()
	{
		this.inputIP.setEditable(true);
		this.inputSecKey.setEditable(true);
		this.inputName.setEditable(true);
		this.connect.setEnabled(true);
		revalidate(); repaint();
	}
	
	Insets generateInsets(int top, int left, int bott, int right) { return new Insets(top, left, bott, right); }
	
	public JTextField getIPField() { return this.inputIP; }
	public JTextField getNameField() { return this.inputName; }
	public JPasswordField getPassField() { return this.inputSecKey; }
	public AuthScreen getAuthScreen() { return this.parent; }
	
	class LoginFunction implements ActionListener
	{
		private JButton b;
		private AuthForm form;
		
		public LoginFunction(AuthForm af, JButton b)
		{
			this.b = b;
			this.form = af;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			String ip = form.getIPField().getText();
			String key = String.valueOf(form.getPassField().getPassword());
			String name = form.getNameField().getText();
			
			if (ip.length() == 0 || key.length() == 0 || name.length() == 0)
			{
				String msg = "<html><body>Invalid Input!<br>One or more fields are empty!</body></html>";
				form.getAuthScreen().changeNotice(msg, Color.RED);
				return;
			}
			
			else
			{
				if (name.length() > 16)
				{
					String msg = "<html><body>Invalid Name!<br>Given name is too long!</body></html>";
					form.getAuthScreen().changeNotice(msg, Color.RED);
					return;
				}
				
				else
				{
					Pattern p = Pattern.compile("[^A-Za-z0-9_ ]");
					Matcher m1 = p.matcher(key);
					Matcher m2 = p.matcher(name);
					
					if (m1.find() || m2.find())
					{
						String msg = "<html><body>Invalid Input!<br>One or more fields contain special characters!</body></html>";
						form.getAuthScreen().changeNotice(msg, Color.RED);
						return;
					}
				}
			}
			b.setText("Connecting...");
			form.disableInputs();
			
			Runnable action = new Runnable()
			{
				public void run()
				{
					NetworkClient cl;
					if (Objects.isNull(form.getAuthScreen().getMainProgClass().getNetClient()))
						cl = form.getAuthScreen().getMainProgClass().iniNetClient(ip, key, name);
					else
					{
						cl = form.getAuthScreen().getMainProgClass().getNetClient();
						cl.resetInputs(ip, key, name);
					}
					
					if (cl.connect())
					{
						b.setText("Login and Chat!");
						
						if (cl.attemptAuth())
						{
							form.getAuthScreen().changeNotice("Connected!", new Color(0, 127, 0, 255));
							
							Runnable showConvo = new Runnable()
							{
								public void run()
								{
									form.getAuthScreen().getMainProgClass().showConversationPane();
									b.setText("Login and Chat!");
									form.getIPField().setText("");
									form.getPassField().setText("");
									form.getNameField().setText("");
									form.enableInputs();
								}
							};
							
							form.getAuthScreen().getTimers().schedule(showConvo, 500, TimeUnit.MILLISECONDS);
						}
						else
						{
							String msg = "<html><body>Connection Refused!<br>Invalid Authentication Key</body></html>";
							form.getAuthScreen().changeNotice(msg, Color.RED);
							form.enableInputs();
							b.setText("Login and Chat!");
							cl.disconnect(true);
							return;
						}
					}
					
					else
					{
						String msg = "<html><body>Connection Refused!<br>Unknown Host: " + ip + "</body></html>";
						form.getAuthScreen().changeNotice(msg, Color.RED);
						form.enableInputs();
						b.setText("Login and Chat!");
						cl.disposeLinks();
						return;
					}
				}
			};
			this.form.getAuthScreen().getTimers().schedule(action, 1, TimeUnit.SECONDS);
		}
	}
}