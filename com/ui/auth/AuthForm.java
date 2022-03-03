package ui.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ui.auth.functions.LoginFunction;

public class AuthForm extends JPanel
{
	private static final long serialVersionUID = 5648930300100301604L;
	
	private AuthScreen parent;
	private GridBagConstraints gbc;
	private JTextField inputIP, inputName;
	private JPasswordField inputSecKey;
	private JButton connect;
	private Color formColor = new Color(255,229,180, 255);
	
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
			inputIP.setBackground(this.formColor);
			inputIP.setHorizontalAlignment(JTextField.CENTER);
			inputIP.setPreferredSize(new Dimension(250, 35));
			inputIP.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			gbc.gridx = 1; gbc.gridy = 1; gbc.insets = generateInsets(7, 0, 7, 0);
			add(inputIP, gbc);
			
		this.inputSecKey = new JPasswordField();
			inputSecKey.setBackground(this.formColor);
			inputSecKey.setHorizontalAlignment(JPasswordField.CENTER);
			inputSecKey.setPreferredSize(new Dimension(250, 35));
			gbc.gridx = 1; gbc.gridy = 2; gbc.insets = generateInsets(7, 0, 7, 0);
			add(inputSecKey, gbc);
			
		this.inputName = new JTextField();
			inputName.setBackground(this.formColor);
			inputName.setHorizontalAlignment(JTextField.CENTER);
			inputName.setPreferredSize(new Dimension(250, 35));
			inputName.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			gbc.gridx = 1; gbc.gridy = 3; gbc.insets = generateInsets(7, 0, 7, 0);
			add(inputName, gbc);
			
		this.connect = new JButton("Login and Chat!");
			connect.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			connect.setPreferredSize(new Dimension(250, 35));
			connect.setFocusPainted(false);
			connect.addActionListener(new LoginFunction(this, connect));
			gbc.gridx = 1; gbc.gridy = 4; gbc.insets = generateInsets(7, 0, 0, 0);
			add(connect, gbc);
			
		this.inputIP.setText("192.168.100.17");
		this.inputSecKey.setText("a");
		this.inputName.setText("ZainVoJr");
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
}