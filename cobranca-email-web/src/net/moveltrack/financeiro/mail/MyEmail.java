package net.moveltrack.financeiro.mail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
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

import org.apache.commons.lang.StringUtils;

import com.sun.mail.util.MailSSLSocketFactory;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.MBoleto;

public class MyEmail {

	public MyEmail() {
		
	}
	
	public static void main(String args[]){
		

		sendEmail();
	
		
	}

	  public static boolean sendEmail()
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
			 message.setFrom(new InternetAddress(from,"Cobrança Moveltrack"));
				
	         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
	         message.addRecipient(Message.RecipientType.BCC,new InternetAddress("josefabiosousabarros@gmail.com"));
	         message.setSubject("Cobrança - Moveltrack Rastreamento");
	         
	         Multipart multipart = new MimeMultipart();
	         BodyPart messageBodyPart = new MimeBodyPart();
	         messageBodyPart.setContent("conteudo", "text/html; charset=ISO-8859-1");
	         
	         multipart.addBodyPart(messageBodyPart);
	         

				
	       /*  for (MBoleto boleto : mBoletos) {
				byte[] bs =  boletoUtils.getBoletoIuguInBytes(boleto,servletContext);
				if(bs!=null){
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					String vencimento = sdf.format(boleto.getDataVencimento());
					String fileName = boleto.getContrato().getCliente().getNome()+"_Venc-"+vencimento+".pdf";
					attach(multipart,bs,StringUtils.replace(fileName," ",""));
				}
			 }*/
	         
	         message.setContent(multipart );
	         
	         Transport.send(message);
	         System.out.println("Email enviado para  e-mail numero de boletos - atraso: dias");
	         return true;
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	         return false;
	      }catch(Exception e){
	    	  e.printStackTrace();
	    	  return false;
	      }
	   }
}