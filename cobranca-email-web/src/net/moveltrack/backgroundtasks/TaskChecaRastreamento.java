package net.moveltrack.backgroundtasks;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
import javax.servlet.ServletContext;

import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.financeiro.mail.SendEmailLembretes;
import net.moveltrack.util.Task;

@ApplicationScoped
public class TaskChecaRastreamento extends Task  {
	
	@PostConstruct
	public void init() {
	}
	
	@Inject	LocationDao locationDao;
	@Inject SendEmailLembretes sendEmailLembretes;
	@Inject ClienteDao clienteDao;
	
	@Override
    public void run() {
		Date lastUpdate = locationDao.getLastUpdate();
		System.out.println("rastreamento ok "+ (lastUpdate!=null?sdf.format(lastUpdate):"null"));
		try {
			if((Calendar.getInstance().getTimeInMillis() - lastUpdate.getTime()> 1000*120) || lastUpdate==null) 
				sendEmail(getServletContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public boolean sendEmail(ServletContext servletContext)
	{
		String to = "josefabiosousabarros@gmail.com";

		String from = "suporte@moveltrack.com.br";
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.debug", "true");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("suporte@moveltrack.com.br","moveltrack02");
			}
		});
		try{
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from,"Lembrete Moveltrack"));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.addRecipient(Message.RecipientType.BCC,new InternetAddress("josefabiosousabarros@gmail.com"));
			Date lastUpdate = locationDao.getLastUpdate();
			
			System.err.println("RASTREAMENTO PARADO DESDE "+ (lastUpdate!=null?sdf.format(lastUpdate):"null"));
			message.setSubject("RASTREAMENTO PARADO DESDE "+ (lastUpdate!=null?sdf.format(lastUpdate):"null"));
			
			
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("RASTREAMENTO PARADO DESDE "+ sdf.format(lastUpdate ),"text/html; charset=ISO-8859-1");
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart );
			Transport.send(message);
			System.out.println("Email de teste enviado para e-mail:");
			return true;
		}catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}


}