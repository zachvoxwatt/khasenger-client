package ui.auth.functions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;

import net.NetworkClient;
import ui.auth.AuthForm;

public class LoginFunction implements ActionListener
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
					b.setText("Connect");
					
					if (cl.attemptAuth())
					{
						if (cl.requestNameValidation())
						{
							form.getAuthScreen().changeNotice("Connected!", new Color(0, 127, 0, 255));
							form.getAuthScreen().getMainProgClass().getAudioController().play("join_client");
							
							Runnable showConvo = new Runnable()
							{
								public void run()
								{
									form.getAuthScreen().getMainProgClass().showConversationPane();
									b.setText("Connect");
									form.enableInputs();
								}
							};
							
							form.getAuthScreen().getTimers().schedule(showConvo, 500, TimeUnit.MILLISECONDS);
						}
						else
						{
							String msg = "<html><body>Connection Refused!<br>This name has already been taken!</body></html>";
							form.getAuthScreen().changeNotice(msg, new Color(255, 69, 0));
							form.enableInputs();
							b.setText("Connect");
							return;
						}
					}
					else
					{
						String msg = "<html><body>Connection Refused!<br>Invalid Authentication Key</body></html>";
						form.getAuthScreen().changeNotice(msg, Color.RED);
						form.enableInputs();
						b.setText("Connect");
						return;
					}
				}
				
				else
				{
					String msg = "<html><body>Connection Refused!<br>Unknown Host: " + ip + "</body></html>";
					form.getAuthScreen().changeNotice(msg, Color.RED);
					form.enableInputs();
					b.setText("Connect");
					cl.disposeLinks();
					return;
				}
			}
		};
		this.form.getAuthScreen().getTimers().schedule(action, 1, TimeUnit.SECONDS);
	}
}