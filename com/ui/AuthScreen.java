package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AuthScreen extends JPanel
{
	private static final long serialVersionUID = 3987958248635912669L;
	private int wx = 500, wy = 500;
	
	private ProgUI parent;
	private JPanel margin1, margin2;
	private AuthForm form;
	
	public AuthScreen(ProgUI pui)
	{
		super();
			this.parent = pui;
			setPreferredSize(new Dimension(this.wx, this.wy));
			setLayout(new BorderLayout());
		
		this.form = new AuthForm(this);
			this.form.setPreferredSize(new Dimension(this.wx, this.wy * (100 - 12 * 2) / 100));
			add(form, BorderLayout.CENTER);
		
		margin1 = new JPanel();
			margin1.setPreferredSize(new Dimension(this.wx, this.wy * 12 / 100));
			margin1.setLayout(new GridBagLayout());
			JLabel title = new JLabel("KHASENGER");
			title.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
			margin1.add(title);
			
		margin2 = new JPanel();
			margin2.setPreferredSize(new Dimension(this.wx, this.wy * 12 / 100));
			
			add(margin1, BorderLayout.NORTH);
			add(margin2, BorderLayout.SOUTH);
	}
	
	public void paintComponent(Graphics g)
	{
		
	}
}

class AuthForm extends JPanel
{
	private static final long serialVersionUID = 5648930300100301604L;
	
	private AuthScreen parent;
	private GridBagConstraints gbc;
	private Insets i;
	private JTextField inputIP, inputName;
	private JPasswordField inputSecKey;
	
	public AuthForm(AuthScreen as)
	{
		super();
			this.gbc = new GridBagConstraints();
			this.parent = as;
			setLayout(new GridBagLayout());
			
		JLabel ipLabel = new JLabel("Target IP: ");
			ipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			gbc.gridx = 0; gbc.gridy = 1; gbc.insets = generateInsets(7, 0, 7, 0);
			add(ipLabel, gbc);
			
		JLabel seckeyLabel = new JLabel("Room Sec. Key: ");
			seckeyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			gbc.gridx = 0; gbc.gridy = 2; gbc.insets = generateInsets(7, 0, 7, 0);
			add(seckeyLabel, gbc);
			
		JLabel usernameLabel = new JLabel("Username: ");
			usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			gbc.gridx = 0; gbc.gridy = 3; gbc.insets = generateInsets(7, 0, 0, 0);
			add(usernameLabel, gbc);
			
		this.inputIP = new JTextField();
			inputIP.setHorizontalAlignment(JTextField.CENTER);
			inputIP.setPreferredSize(new Dimension(250, 35));
			inputIP.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			gbc.gridx = 1; gbc.gridy = 1; gbc.insets = generateInsets(7, 0, 7, 0);
			add(inputIP, gbc);
			
		this.inputSecKey = new JPasswordField();
			inputSecKey.setHorizontalAlignment(JPasswordField.CENTER);
			inputSecKey.setPreferredSize(new Dimension(250, 35));
			gbc.gridx = 1; gbc.gridy = 2; gbc.insets = generateInsets(7, 0, 7, 0);
			add(inputSecKey, gbc);
			
		this.inputName = new JTextField();
			inputName.setHorizontalAlignment(JTextField.CENTER);
			inputName.setPreferredSize(new Dimension(250, 35));
			inputName.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			gbc.gridx = 1; gbc.gridy = 3; gbc.insets = generateInsets(7, 0, 0, 0);
			add(inputName, gbc);
	}
	
	Insets generateInsets(int top, int left, int bott, int right) { return new Insets(top, left, bott, right); }
	public void paintComponent(Graphics g)
	{
		
	}
}