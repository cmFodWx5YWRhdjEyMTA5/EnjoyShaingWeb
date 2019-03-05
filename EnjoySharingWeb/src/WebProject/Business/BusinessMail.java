package WebProject.Business;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class BusinessMail {

	private String senderEmailID = "enjoysharing.18@gmail.com";
	private String senderPassword = "enjoysharing2018";
	private String emailSMTPserver = "smtp.gmail.com";
	private String emailServerPort = "465";
	
	private Properties props;
	private Authenticator auth;
	
	public BusinessMail()
	{
		props = new Properties();
		props.put("mail.smtp.user",senderEmailID);
		props.put("mail.smtp.host", emailSMTPserver);
		props.put("mail.smtp.port", emailServerPort);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", emailServerPort);
		props.put("mail.smtp.socketFactory.class",
		"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		System.getSecurityManager();
		Autenticate();
	}
	
	private void Autenticate()
	{
		try
		{
			auth = new SMTPAuthenticator();
		}
		catch (Exception e)
		{
			new Exception(e);
		}
	}
	
	public class SMTPAuthenticator extends javax.mail.Authenticator
	{
		public PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(senderEmailID, senderPassword);
		}
	}
	
	public boolean SendMessage(String receiverEmailID, String emailSubject, String emailBody)
	{
		try
		{
			Session session = Session.getInstance(props, auth);
	
			MimeMessage msg = new MimeMessage(session);
			msg.setText(emailBody);
			msg.setSubject(emailSubject);
			msg.setFrom(new InternetAddress(senderEmailID));
			msg.addRecipient(Message.RecipientType.TO,
			new InternetAddress(receiverEmailID));
			Transport.send(msg);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
}