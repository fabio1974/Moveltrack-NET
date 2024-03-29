package net.moveltrack.financeiro.mail;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.moveltrack.domain.Cliente;
import net.moveltrack.financeiro.action.BoletoUtils;


@Named
@Stateless
public class SendEmailCliente
{
	
	@Inject BoletoUtils boletoUtils;
	
   public void sendEmail(Cliente cliente, String subject, String content)
   {
		 
		   
	      String to = cliente.getEmailAlarme();
			if(to == null && cliente.getUsuario()!=null)
				to = cliente.getUsuario().getEmail();
			if(to==null)
				return;
			
	        //to = "josefabiosousabarros@gmail.com";
	      
			String from = "suporte@moveltrack.com.br";
			Properties props = System.getProperties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			//props.put("mail.debug", "true");

			Session session = Session.getInstance(props,new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("suporte@moveltrack.com.br","moveltrack02");
				}
			});
      try{
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
         message.addRecipient(Message.RecipientType.BCC,new InternetAddress("josefabiosousabarros@gmail.com"));
         message.setSubject(subject);
         
         Multipart multipart = new MimeMultipart();
         BodyPart messageBodyPart = new MimeBodyPart();
         messageBodyPart.setContent(content, "text/html; charset=ISO-8859-1");
         
         multipart.addBodyPart(messageBodyPart);
         message.setContent(multipart );
         Transport.send(message);
         System.out.println("Email enviado para "+cliente.getNome());
      }catch (Exception mex) {
         mex.printStackTrace();
      }
   }

}