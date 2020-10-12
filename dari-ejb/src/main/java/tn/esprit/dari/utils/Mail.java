package tn.esprit.dari.utils;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
	public static void sendMail (String destmail  , String message , String subject)
	{
	  
	      String smtphost = "smtp.gmail.com";
	      Properties propvls = new Properties();
	      propvls.put("mail.smtp.auth", "true");
	      propvls.put("mail.smtp.starttls.enable", "true");
	      propvls.put("mail.smtp.host", smtphost);
	      propvls.put("mail.smtp.port", "587");
	      Session sessionobj = Session.getInstance(propvls,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication("dari.pi.nids@gmail.com", "dari@123");
		   }
	         });
	 
	      try {
		   Message messageobj = new MimeMessage(sessionobj);
		   messageobj.setFrom(new InternetAddress("dari.pi.nids@gmail.com"));
		   messageobj.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destmail));
		   messageobj.setSubject(subject);
		   messageobj.setText(message);
		   
		   Transport.send(messageobj);
		   System.out.println("Your email was sent successfully....");
	      } catch (MessagingException exp) {
	         throw new RuntimeException(exp);
	      }
	}

}
